package exchange.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateProviderFactoryTest {

    @Test
    void shouldCreatePtaxProvider() {
        RateProvider provider = new PtaxProviderFactory().create();
        assertTrue(provider instanceof PtaxProvider);
        assertEquals("PTAX", provider.getName());
    }

    @Test
    void shouldCreateCommercialProvider() {
        RateProvider provider = new CommercialProviderFactory().create();
        assertTrue(provider instanceof CommercialProvider);
        assertEquals("Commercial", provider.getName());
    }

    @Test
    void shouldCreateTourismProvider() {
        RateProvider provider = new TourismProviderFactory().create();
        assertTrue(provider instanceof TourismProvider);
        assertEquals("Tourism", provider.getName());
    }

    @Test
    void shouldCreateANewProviderOnEachCall() {
        RateProviderFactory factory = new PtaxProviderFactory();
        assertNotSame(factory.create(), factory.create());
    }

    @Test
    void shouldUseTheSubclassFactoryMethod() {
        RateProvider custom = new PtaxProvider();
        RateProviderFactory factory = new RateProviderFactory() {
            @Override
            protected RateProvider createProvider() {
                return custom;
            }
        };
        assertSame(custom, factory.create());
    }

    @Test
    void shouldThrowWhenFactoryMethodCreatesNothing() {
        RateProviderFactory factory = new RateProviderFactory() {
            @Override
            protected RateProvider createProvider() {
                return null;
            }
        };
        IllegalStateException e = assertThrows(IllegalStateException.class, factory::create);
        assertEquals("Factory created no provider", e.getMessage());
    }
}
