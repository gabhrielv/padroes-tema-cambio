package exchange.model;

public class ExchangeResult {

    private final String currency;
    private final double amount;
    private final double rate;
    private final double subtotal;
    private final double total;
    private final String payment;

    // amount is in the foreign currency; rate, subtotal and total are in BRL
    public ExchangeResult(String currency, double amount, double rate,
                          double subtotal, double total, String payment) {
        this.currency = currency;
        this.amount = amount;
        this.rate = rate;
        this.subtotal = subtotal;
        this.total = total;
        this.payment = payment;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public double getRate() {
        return rate;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotal() {
        return total;
    }

    public String getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return amount + " " + currency + "{rate=" + rate + ", subtotal=" + subtotal +
                ", total=" + total + ", payment=" + payment + "}";
    }
}
