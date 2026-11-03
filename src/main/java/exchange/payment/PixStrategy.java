package exchange.payment;

public class PixStrategy implements PaymentStrategy {

    public double total(double amountInBrl) {
        return amountInBrl;
    }

    public String getName() {
        return "Pix";
    }
}
