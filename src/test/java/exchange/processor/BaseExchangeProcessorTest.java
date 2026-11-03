package exchange.processor;

import exchange.cache.RateCache;
import exchange.model.ExchangeResult;
import exchange.model.Quote;
import exchange.payment.CardStrategy;
import exchange.payment.PaymentCalculator;
import exchange.payment.PaymentStrategy;
import exchange.payment.PixStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseExchangeProcessorTest {

    private final Quote dollar = new Quote("USD", 5.00, 5.50);

    @BeforeEach
    void clearCache() {
        RateCache.getInstance().clear();
    }

    // Processor that always returns the given quote and counts fetches
    private static class FakeProcessor extends BaseExchangeProcessor {
        private final String source;
        private final Quote quote;
        int fetchCount = 0;

        FakeProcessor(String source, Quote quote, PaymentStrategy payment) {
            super(new PaymentCalculator(payment));
            this.source = source;
            this.quote = quote;
        }

        @Override
        protected Quote fetch(String currency) {
            fetchCount++;
            return quote;
        }

        @Override
        protected String source() {
            return source;
        }
    }

    // Processor that records each step it runs
    private static class StepsProcessor extends FakeProcessor {
        final List<String> steps = new ArrayList<>();

        StepsProcessor(Quote quote) {
            super("Fake", quote, new PixStrategy());
        }

        @Override
        protected Quote fetch(String currency) {
            steps.add("fetch");
            return super.fetch(currency);
        }

        @Override
        protected void validate(Quote quote) {
            steps.add("validate");
            super.validate(quote);
        }

        @Override
        protected void cache(String key, Quote quote) {
            steps.add("cache");
            super.cache(key, quote);
        }

        @Override
        protected double convert(Quote quote, double amount) {
            steps.add("convert");
            return super.convert(quote, amount);
        }

        @Override
        protected double charge(double subtotal) {
            steps.add("charge");
            return super.charge(subtotal);
        }
    }

    private FakeProcessor processor(Quote quote) {
        return new FakeProcessor("Fake", quote, new PixStrategy());
    }

    @Test
    void shouldExecuteStepsInOrder() {
        StepsProcessor processor = new StepsProcessor(dollar);
        processor.process("USD", 100.0);
        assertEquals(Arrays.asList("fetch", "validate", "cache", "convert", "charge"), processor.steps);
    }

    @Test
    void shouldSkipFetchValidateAndCacheWhenQuoteIsCached() {
        StepsProcessor processor = new StepsProcessor(dollar);
        processor.process("USD", 100.0);
        processor.steps.clear();
        processor.process("USD", 200.0);
        assertEquals(Arrays.asList("convert", "charge"), processor.steps);
    }

    @Test
    void shouldConvertWithTheSellRate() {
        ExchangeResult result = processor(dollar).process("USD", 100.0);
        assertEquals("USD", result.getCurrency());
        assertEquals(100.0, result.getAmount());
        assertEquals(5.50, result.getRate());
        assertEquals(550.0, result.getSubtotal(), 0.001);
    }

    @Test
    void shouldChargeUsingStrategy() {
        FakeProcessor processor = new FakeProcessor("Fake", dollar, new CardStrategy());
        ExchangeResult result = processor.process("USD", 100.0);
        assertEquals(550.0, result.getSubtotal(), 0.001);
        assertEquals(572.0, result.getTotal(), 0.001);
        assertEquals("Card", result.getPayment());
    }

    @Test
    void shouldStoreQuoteInCache() {
        processor(dollar).process("USD", 100.0);
        assertEquals(1, RateCache.getInstance().size());
        assertTrue(RateCache.getInstance().contains("Fake|USD"));
        assertSame(dollar, RateCache.getInstance().get("Fake|USD"));
    }

    @Test
    void shouldNotFetchAgainWhenQuoteIsCached() {
        FakeProcessor processor = processor(dollar);
        processor.process("USD", 100.0);
        ExchangeResult second = processor.process(" usd ", 10.0);
        assertEquals(1, processor.fetchCount);
        assertEquals("USD", second.getCurrency());
        assertEquals(55.0, second.getSubtotal(), 0.001);
    }

    @Test
    void shouldShareCacheBetweenProcessorsOfTheSameSource() {
        FakeProcessor first = processor(dollar);
        FakeProcessor second = processor(new Quote("USD", 9.00, 9.90));
        first.process("USD", 100.0);
        ExchangeResult result = second.process("USD", 100.0);
        assertEquals(0, second.fetchCount);
        assertEquals(5.50, result.getRate());
    }

    @Test
    void shouldCacheDifferentSourcesSeparately() {
        FakeProcessor bank = new FakeProcessor("Bank", dollar, new PixStrategy());
        FakeProcessor office = new FakeProcessor("Office", new Quote("USD", 5.20, 5.80), new PixStrategy());
        bank.process("USD", 100.0);
        ExchangeResult result = office.process("USD", 100.0);
        assertEquals(1, office.fetchCount);
        assertEquals(5.80, result.getRate());
        assertEquals(2, RateCache.getInstance().size());
    }

    @Test
    void shouldRejectBlankCurrency() {
        FakeProcessor processor = processor(dollar);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("  ", 100.0));
        assertEquals("Currency is required", e.getMessage());
        assertEquals(0, processor.fetchCount);
    }

    @Test
    void shouldRejectNullCurrency() {
        FakeProcessor processor = processor(dollar);
        assertThrows(IllegalArgumentException.class, () -> processor.process(null, 100.0));
    }

    @Test
    void shouldRejectZeroAmount() {
        FakeProcessor processor = processor(dollar);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("USD", 0.0));
        assertEquals("Amount must be positive", e.getMessage());
        assertEquals(0, processor.fetchCount);
    }

    @Test
    void shouldRejectNegativeAmount() {
        FakeProcessor processor = processor(dollar);
        assertThrows(IllegalArgumentException.class, () -> processor.process("USD", -10.0));
    }

    @Test
    void shouldRejectNaNAmount() {
        FakeProcessor processor = processor(dollar);
        assertThrows(IllegalArgumentException.class, () -> processor.process("USD", Double.NaN));
    }

    @Test
    void shouldRejectMissingQuote() {
        FakeProcessor processor = processor(null);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("USD", 100.0));
        assertEquals("Missing quote", e.getMessage());
    }

    @Test
    void shouldRejectInvalidBuyRate() {
        FakeProcessor processor = processor(new Quote("USD", 0.0, 5.50));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("USD", 100.0));
        assertEquals("Invalid buy rate", e.getMessage());
    }

    @Test
    void shouldRejectSellRateBelowBuyRate() {
        FakeProcessor processor = processor(new Quote("USD", 5.50, 5.00));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("USD", 100.0));
        assertEquals("Invalid sell rate", e.getMessage());
    }

    @Test
    void shouldRejectNaNRate() {
        FakeProcessor processor = processor(new Quote("USD", 5.00, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> processor.process("USD", 100.0));
    }

    @Test
    void shouldAcceptSellRateEqualToBuyRate() {
        ExchangeResult result = processor(new Quote("USD", 5.00, 5.00)).process("USD", 100.0);
        assertEquals(500.0, result.getSubtotal(), 0.001);
    }

    @Test
    void shouldNotCacheInvalidQuote() {
        FakeProcessor processor = processor(new Quote("USD", 0.0, 5.50));
        assertThrows(IllegalArgumentException.class, () -> processor.process("USD", 100.0));
        assertEquals(0, RateCache.getInstance().size());
    }
}
