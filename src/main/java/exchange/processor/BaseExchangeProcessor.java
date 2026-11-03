package exchange.processor;

import exchange.cache.RateCache;
import exchange.model.ExchangeResult;
import exchange.model.Quote;
import exchange.payment.PaymentCalculator;

import java.util.Locale;

/**
 * Template Method: process() fixes the order
 * fetch -> validate -> cache -> convert -> charge.
 * fetch, validate and cache are skipped when the quote is already cached.
 * Subclasses supply fetch and source; the other steps have default
 * implementations that can be overridden.
 */
public abstract class BaseExchangeProcessor {

    private final PaymentCalculator calculator;

    protected BaseExchangeProcessor(PaymentCalculator calculator) {
        this.calculator = calculator;
    }

    // The template method: buys `amount` units of `currency`
    public final ExchangeResult process(String currency, double amount) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency is required");
        }
        // Written as !(amount > 0) so NaN is rejected too
        if (!(amount > 0)) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        String code = currency.trim().toUpperCase(Locale.ROOT);
        String key = source() + "|" + code;
        Quote quote = RateCache.getInstance().get(key);
        if (quote == null) {
            quote = fetch(code);
            validate(quote);
            cache(key, quote);
        }

        double subtotal = convert(quote, amount);
        double total = charge(subtotal);
        return new ExchangeResult(code, amount, quote.getSell(), subtotal, total, calculator.getPayment());
    }

    protected abstract Quote fetch(String currency);

    // Name of the rate source; quotes from different sources must not share a cache entry
    protected abstract String source();

    protected void validate(Quote quote) {
        if (quote == null) {
            throw new IllegalArgumentException("Missing quote");
        }
        if (!(quote.getBuy() > 0)) {
            throw new IllegalArgumentException("Invalid buy rate");
        }
        if (!(quote.getSell() >= quote.getBuy())) {
            throw new IllegalArgumentException("Invalid sell rate");
        }
    }

    protected void cache(String key, Quote quote) {
        RateCache.getInstance().put(key, quote);
    }

    // The customer buys foreign currency, so the office's sell rate applies
    protected double convert(Quote quote, double amount) {
        return amount * quote.getSell();
    }

    protected double charge(double subtotal) {
        return calculator.total(subtotal);
    }
}
