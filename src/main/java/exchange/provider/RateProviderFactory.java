package exchange.provider;

/**
 * Factory Method: each subclass decides which RateProvider to create.
 */
public abstract class RateProviderFactory {

    public final RateProvider create() {
        RateProvider provider = createProvider();
        if (provider == null) {
            throw new IllegalStateException("Factory created no provider");
        }
        return provider;
    }

    /** The factory method. */
    protected abstract RateProvider createProvider();
}
