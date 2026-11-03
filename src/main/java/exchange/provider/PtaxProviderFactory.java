package exchange.provider;

public class PtaxProviderFactory extends RateProviderFactory {

    @Override
    protected RateProvider createProvider() {
        return new PtaxProvider();
    }
}
