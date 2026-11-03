package exchange.provider;

import exchange.model.Quote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PtaxProviderTest {

    private final RateProvider provider = new PtaxProvider();

    @Test
    void shouldReturnDollarQuote() {
        Quote quote = provider.fetch("USD");
        assertEquals("PTAX", provider.getName());
        assertEquals("USD", quote.getCurrency());
        assertEquals(5.40, quote.getBuy());
        assertEquals(5.41, quote.getSell());
    }

    @Test
    void shouldReturnEuroQuote() {
        Quote quote = provider.fetch("EUR");
        assertEquals("EUR", quote.getCurrency());
        assertEquals(5.85, quote.getBuy());
        assertEquals(5.86, quote.getSell());
    }

    @Test
    void shouldThrowForUnsupportedCurrency() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> provider.fetch("JPY"));
        assertEquals("Unsupported currency", e.getMessage());
    }

    @Test
    void shouldThrowForNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> provider.fetch(null));
    }
}
