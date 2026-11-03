package exchange.payment;

/**
 * Strategy: computes the final price in BRL for one payment method.
 * The fees are fictitious.
 */
public interface PaymentStrategy {

    double total(double amountInBrl);

    String getName();
}
