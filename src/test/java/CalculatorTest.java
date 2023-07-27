import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void testAdd() {
        assertEquals(15.0, calculator.add(10, 5));
    }

    @Test
    public void testSubtract() {
        assertEquals(5.0, calculator.subtract(10, 5));
    }

    @Test
    public void testMultiply() {
        assertEquals(50.0, calculator.multiply(10, 5));
    }

    @Test
    public void testDivide() {
        assertEquals(2.0, calculator.divide(10, 5));
    }

    @Test
    public void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calculator.divide(10, 0));
    }
}
