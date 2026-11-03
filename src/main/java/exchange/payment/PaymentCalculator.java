package exchange.payment;

/**
 * Strategy context: applies the chosen payment strategy to an amount in BRL.
 */
public class PaymentCalculator {

    private final PaymentStrategy strategy;

    public PaymentCalculator(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public double total(double amountInBrl) {
        return strategy.total(amountInBrl);
    }

    public String getPayment() {
        return strategy.getName();
    }
}
