package exchange.provider;

import exchange.model.Quote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommercialProviderTest {

    private final RateProvider provider = new CommercialProvider();

    @Test
    void shouldReturnDollarQuote() {
        Quote quote = provider.fetch("USD");
        assertEquals("Commercial", provider.getName());
        assertEquals("USD", quote.getCurrency());
        assertEquals(5.38, quote.getBuy());
        assertEquals(5.45, quote.getSell());
    }

    @Test
    void shouldReturnEuroQuote() {
        Quote quote = provider.fetch("EUR");
        assertEquals("EUR", quote.getCurrency());
        assertEquals(5.83, quote.getBuy());
        assertEquals(5.90, quote.getSell());
    }

    @Test
    void shouldThrowForUnsupportedCurrency() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> provider.fetch("GBP"));
        assertEquals("Unsupported currency", e.getMessage());
    }

    @Test
    void shouldThrowForNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> provider.fetch(null));
    }
}
