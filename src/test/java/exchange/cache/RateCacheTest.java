package exchange.cache;

import exchange.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateCacheTest {

    @BeforeEach
    void clearCache() {
        RateCache.getInstance().clear();
    }

    @Test
    void shouldAlwaysReturnTheSameInstance() {
        assertSame(RateCache.getInstance(), RateCache.getInstance());
    }

    @Test
    void shouldShareQuotesBetweenCallsToGetInstance() {
        Quote quote = new Quote("USD", 5.40, 5.41);
        RateCache.getInstance().put("PTAX|USD", quote);
        assertSame(quote, RateCache.getInstance().get("PTAX|USD"));
    }

    @Test
    void shouldReturnNullForUnknownKey() {
        assertNull(RateCache.getInstance().get("unknown"));
        assertFalse(RateCache.getInstance().contains("unknown"));
    }

    @Test
    void shouldClearAllQuotes() {
        RateCache.getInstance().put("PTAX|USD", new Quote("USD", 5.40, 5.41));
        RateCache.getInstance().put("PTAX|EUR", new Quote("EUR", 5.85, 5.86));
        assertEquals(2, RateCache.getInstance().size());
        RateCache.getInstance().clear();
        assertEquals(0, RateCache.getInstance().size());
    }
}
