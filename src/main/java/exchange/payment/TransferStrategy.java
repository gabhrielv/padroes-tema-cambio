package exchange.payment;

public class TransferStrategy implements PaymentStrategy {

    public double total(double amountInBrl) {
        return amountInBrl * 1.005;
    }

    public String getName() {
        return "Transfer";
    }
}
