package exchange.provider;

import exchange.model.Quote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourismProviderTest {

    private final RateProvider provider = new TourismProvider();

    @Test
    void shouldReturnDollarQuote() {
        Quote quote = provider.fetch("USD");
        assertEquals("Tourism", provider.getName());
        assertEquals("USD", quote.getCurrency());
        assertEquals(5.30, quote.getBuy());
        assertEquals(5.65, quote.getSell());
    }

    @Test
    void shouldReturnEuroQuote() {
        Quote quote = provider.fetch("EUR");
        assertEquals("EUR", quote.getCurrency());
        assertEquals(5.75, quote.getBuy());
        assertEquals(6.10, quote.getSell());
    }

    @Test
    void shouldThrowForUnsupportedCurrency() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> provider.fetch("ARS"));
        assertEquals("Unsupported currency", e.getMessage());
    }

    @Test
    void shouldThrowForNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> provider.fetch(null));
    }
}
