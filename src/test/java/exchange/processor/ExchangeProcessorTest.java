package exchange.processor;

import exchange.cache.RateCache;
import exchange.model.ExchangeResult;
import exchange.payment.CardStrategy;
import exchange.payment.PaymentCalculator;
import exchange.payment.PaymentStrategy;
import exchange.payment.PixStrategy;
import exchange.provider.CommercialProviderFactory;
import exchange.provider.PtaxProviderFactory;
import exchange.provider.RateProviderFactory;
import exchange.provider.TourismProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeProcessorTest {

    @BeforeEach
    void clearCache() {
        RateCache.getInstance().clear();
    }

    private ExchangeProcessor processorFor(RateProviderFactory factory, PaymentStrategy payment) {
        return new ExchangeProcessor(factory.create(), new PaymentCalculator(payment));
    }

    @Test
    void shouldProcessWithPtax() {
        ExchangeResult result = processorFor(new PtaxProviderFactory(), new PixStrategy()).process("USD", 100.0);
        assertEquals(5.41, result.getRate());
        assertEquals(541.0, result.getTotal(), 0.001);
    }

    @Test
    void shouldProcessWithCommercialRate() {
        ExchangeResult result = processorFor(new CommercialProviderFactory(), new PixStrategy()).process("USD", 100.0);
        assertEquals(5.45, result.getRate());
        assertEquals(545.0, result.getTotal(), 0.001);
    }

    @Test
    void shouldProcessWithTourismRate() {
        ExchangeResult result = processorFor(new TourismProviderFactory(), new PixStrategy()).process("USD", 100.0);
        assertEquals(5.65, result.getRate());
        assertEquals(565.0, result.getTotal(), 0.001);
    }

    @Test
    void shouldApplyPaymentStrategyForAnyProvider() {
        ExchangeResult result = processorFor(new TourismProviderFactory(), new CardStrategy()).process("EUR", 100.0);
        assertEquals("EUR", result.getCurrency());
        assertEquals(610.0, result.getSubtotal(), 0.001);
        assertEquals(634.4, result.getTotal(), 0.001);
        assertEquals("Card", result.getPayment());
    }

    @Test
    void shouldAcceptCurrencyInAnyCase() {
        ExchangeResult result = processorFor(new PtaxProviderFactory(), new PixStrategy()).process(" eur ", 10.0);
        assertEquals("EUR", result.getCurrency());
        assertEquals(58.6, result.getTotal(), 0.001);
    }

    @Test
    void shouldCacheFetchedQuote() {
        ExchangeProcessor processor = processorFor(new PtaxProviderFactory(), new PixStrategy());
        processor.process("USD", 100.0);
        processor.process("USD", 50.0);
        assertEquals(1, RateCache.getInstance().size());
        assertTrue(RateCache.getInstance().contains("PTAX|USD"));
    }

    @Test
    void shouldThrowForUnsupportedCurrencyAndCacheNothing() {
        ExchangeProcessor processor = processorFor(new PtaxProviderFactory(), new PixStrategy());
        assertThrows(IllegalArgumentException.class, () -> processor.process("JPY", 100.0));
        assertEquals(0, RateCache.getInstance().size());
    }
}
