package exchange.provider;

import exchange.model.Quote;

/**
 * A source of exchange rates (product of the Factory Method).
 */
public interface RateProvider {

    String getName();

    // Returns the quote for a currency code such as "USD"
    Quote fetch(String currency);
}
