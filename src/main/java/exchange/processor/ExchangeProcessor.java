package exchange.processor;

import exchange.model.Quote;
import exchange.payment.PaymentCalculator;
import exchange.provider.RateProvider;

/**
 * Concrete processor: quotes come from a provider (usually created by a
 * RateProviderFactory).
 */
public class ExchangeProcessor extends BaseExchangeProcessor {

    private final RateProvider provider;

    public ExchangeProcessor(RateProvider provider, PaymentCalculator calculator) {
        super(calculator);
        this.provider = provider;
    }

    @Override
    protected Quote fetch(String currency) {
        return provider.fetch(currency);
    }

    @Override
    protected String source() {
        return provider.getName();
    }
}
