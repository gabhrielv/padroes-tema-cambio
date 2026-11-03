package exchange.provider;

import exchange.model.Quote;

/**
 * Tourism rate, used for cash and travel cards: the widest spread.
 */
public class TourismProvider implements RateProvider {

    public String getName() {
        return "Tourism";
    }

    // Simulates the call to the exchange office
    public Quote fetch(String currency) {
        if ("USD".equals(currency)) {
            return new Quote("USD", 5.30, 5.65);
        }
        if ("EUR".equals(currency)) {
            return new Quote("EUR", 5.75, 6.10);
        }
        throw new IllegalArgumentException("Unsupported currency");
    }
}
