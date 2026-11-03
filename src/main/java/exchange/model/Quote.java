package exchange.model;

/**
 * Price of one unit of a foreign currency, in reais (BRL).
 * buy: what the exchange office pays; sell: what it charges.
 */
public class Quote {

    private final String currency;
    private final double buy;
    private final double sell;

    public Quote(String currency, double buy, double sell) {
        this.currency = currency;
        this.buy = buy;
        this.sell = sell;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }

    @Override
    public String toString() {
        return currency + "{buy=" + buy + ", sell=" + sell + "}";
    }
}
