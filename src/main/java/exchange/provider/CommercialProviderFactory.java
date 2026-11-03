package exchange.provider;

public class CommercialProviderFactory extends RateProviderFactory {

    @Override
    protected RateProvider createProvider() {
        return new CommercialProvider();
    }
}
