package exchange.cache;

import exchange.model.Quote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton: one in-memory store of quotes shared by the whole application,
 * so the same quote is not fetched twice.
 */
public class RateCache {

    private static final RateCache instance = new RateCache();

    private final Map<String, Quote> quotes = new ConcurrentHashMap<>();

    private RateCache() {}

    public static RateCache getInstance() {
        return instance;
    }

    public void put(String key, Quote quote) {
        quotes.put(key, quote);
    }

    public Quote get(String key) {
        return quotes.get(key);
    }

    public boolean contains(String key) {
        return quotes.containsKey(key);
    }

    public int size() {
        return quotes.size();
    }

    public void clear() {
        quotes.clear();
    }
}
