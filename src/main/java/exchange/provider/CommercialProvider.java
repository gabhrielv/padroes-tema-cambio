package exchange.provider;

import exchange.model.Quote;

/**
 * Commercial rate, used for transfers between companies.
 */
public class CommercialProvider implements RateProvider {

    public String getName() {
        return "Commercial";
    }

    // Simulates the call to the bank
    public Quote fetch(String currency) {
        if ("USD".equals(currency)) {
            return new Quote("USD", 5.38, 5.45);
        }
        if ("EUR".equals(currency)) {
            return new Quote("EUR", 5.83, 5.90);
        }
        throw new IllegalArgumentException("Unsupported currency");
    }
}
