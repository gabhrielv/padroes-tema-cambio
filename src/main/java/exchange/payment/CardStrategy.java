package exchange.payment;

public class CardStrategy implements PaymentStrategy {

    public double total(double amountInBrl) {
        return amountInBrl * 1.04;
    }

    public String getName() {
        return "Card";
    }
}
