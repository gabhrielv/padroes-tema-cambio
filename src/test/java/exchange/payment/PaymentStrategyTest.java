package exchange.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStrategyTest {

    @Test
    void shouldAddOnePercentForCash() {
        PaymentStrategy strategy = new CashStrategy();
        assertEquals(1010.0, strategy.total(1000.0), 0.001);
        assertEquals("Cash", strategy.getName());
    }

    @Test
    void shouldAddFourPercentForCard() {
        PaymentStrategy strategy = new CardStrategy();
        assertEquals(1040.0, strategy.total(1000.0), 0.001);
        assertEquals("Card", strategy.getName());
    }

    @Test
    void shouldAddHalfPercentForTransfer() {
        PaymentStrategy strategy = new TransferStrategy();
        assertEquals(1005.0, strategy.total(1000.0), 0.001);
        assertEquals("Transfer", strategy.getName());
    }

    @Test
    void shouldNotAddAnyFeeForPix() {
        PaymentStrategy strategy = new PixStrategy();
        assertEquals(1000.0, strategy.total(1000.0));
        assertEquals("Pix", strategy.getName());
    }

    @Test
    void shouldKeepZeroForEveryMethod() {
        assertEquals(0.0, new CashStrategy().total(0.0));
        assertEquals(0.0, new CardStrategy().total(0.0));
        assertEquals(0.0, new TransferStrategy().total(0.0));
        assertEquals(0.0, new PixStrategy().total(0.0));
    }
}
