package exchange.payment;

public class CashStrategy implements PaymentStrategy {

    public double total(double amountInBrl) {
        return amountInBrl * 1.01;
    }

    public String getName() {
        return "Cash";
    }
}
