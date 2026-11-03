package exchange.provider;

import exchange.model.Quote;

/**
 * Central Bank reference rate: almost no spread.
 */
public class PtaxProvider implements RateProvider {

    public String getName() {
        return "PTAX";
    }

    // Simulates the call to the Central Bank
    public Quote fetch(String currency) {
        if ("USD".equals(currency)) {
            return new Quote("USD", 5.40, 5.41);
        }
        if ("EUR".equals(currency)) {
            return new Quote("EUR", 5.85, 5.86);
        }
        throw new IllegalArgumentException("Unsupported currency");
    }
}
