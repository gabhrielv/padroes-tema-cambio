package exchange.provider;

public class TourismProviderFactory extends RateProviderFactory {

    @Override
    protected RateProvider createProvider() {
        return new TourismProvider();
    }
}
