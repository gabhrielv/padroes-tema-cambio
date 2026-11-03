package exchange.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentCalculatorTest {

    @Test
    void shouldKeepAmountWithPix() {
        PaymentCalculator calculator = new PaymentCalculator(new PixStrategy());
        assertEquals(1000.0, calculator.total(1000.0));
        assertEquals("Pix", calculator.getPayment());
    }

    @Test
    void shouldApplyTheChosenStrategy() {
        PaymentCalculator calculator = new PaymentCalculator(new CardStrategy());
        assertEquals(1040.0, calculator.total(1000.0), 0.001);
        assertEquals("Card", calculator.getPayment());
    }

    @Test
    void shouldGiveDifferentTotalsForDifferentStrategies() {
        assertEquals(1010.0, new PaymentCalculator(new CashStrategy()).total(1000.0), 0.001);
        assertEquals(1005.0, new PaymentCalculator(new TransferStrategy()).total(1000.0), 0.001);
    }

    @Test
    void shouldUseTheSameStrategyOnEveryCall() {
        PaymentCalculator calculator = new PaymentCalculator(new CashStrategy());
        assertEquals(101.0, calculator.total(100.0), 0.001);
        assertEquals(202.0, calculator.total(200.0), 0.001);
    }
}
