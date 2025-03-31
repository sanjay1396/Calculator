package com.ebay.calculator;

import com.ebay.calculator.controller.CalculatorController;
import com.ebay.calculator.model.Operation;
import com.ebay.calculator.service.CalculatorService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

public class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculate_Addition() {
        // Given
        when(calculatorService.calculate(Operation.ADD, 5, 3)).thenReturn(8);

        // When
        Number result = calculatorController.calculate(Operation.ADD, 5, 3);

        // Then
        assertEquals("Addition should return 8", 8, result);
        verify(calculatorService).calculate(Operation.ADD, 5, 3);
    }

    @Test
    public void testCalculate_Subtraction() {
        // Given
        when(calculatorService.calculate(Operation.SUBTRACT, 5, 3)).thenReturn(2);

        // When
        Number result = calculatorController.calculate(Operation.SUBTRACT, 5, 3).toString();

        // Then
        assertEquals("Subtraction should return 2", 2, result);
        verify(calculatorService).calculate(Operation.SUBTRACT, 5, 3);
    }

    @Test
    public void testCalculate_Multiplication() {
        // Given
        when(calculatorService.calculate(Operation.MULTIPLY, 5, 3)).thenReturn(15);

        // When
        Number result = calculatorController.calculate(Operation.MULTIPLY, 5, 3).toString();

        // Then
        assertEquals("Multiplication should return 15", 15, result);
        verify(calculatorService).calculate(Operation.MULTIPLY, 5, 3);
    }

    @Test
    public void testCalculate_Division() {
        // Given
        when(calculatorService.calculate(Operation.DIVIDE, 6, 3)).thenReturn(2);

        // When
        Number result = calculatorController.calculate(Operation.DIVIDE, 6, 3).toString();

        // Then
        assertEquals("Division should return 2", 2, result);
        verify(calculatorService).calculate(Operation.DIVIDE, 6, 3);
    }

    @Test
    public void testCalculate_DivideByZero() {
        // Given
        when(calculatorService.calculate(Operation.DIVIDE, 5, 0)).thenThrow(new IllegalArgumentException("Cannot divide by zero"));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorController.calculate(Operation.DIVIDE, 5, 0);
        });
        assertEquals("Cannot divide by zero", exception.getMessage());
        verify(calculatorService).calculate(Operation.DIVIDE, 5, 0);
    }

    @Test
    public void testChainCalculate_ValidOperations() {
        // Given
        List<Pair<Operation, Number>> operations = Arrays.asList(
                Pair.of(Operation.ADD, 3),
                Pair.of(Operation.MULTIPLY, 2)
        );
        when(calculatorService.chainCalculate(5, operations)).thenReturn(16);

        // When
        Number result = calculatorController.chainCalculate(5, operations);

        // Then
        assertEquals("Chained operations should result in 16", 16, result);
        verify(calculatorService).chainCalculate(5, operations);
    }

    @Test
    public void testChainCalculate_WithSubtraction() {
        // Given
        List<Pair<Operation, Number>> operations = Arrays.asList(
                Pair.of(Operation.SUBTRACT, 3),
                Pair.of(Operation.ADD, 5)
        );
        when(calculatorService.chainCalculate(10, operations)).thenReturn(12);

        // When
        Number result = calculatorController.chainCalculate(10, operations);

        // Then
        assertEquals("Chained subtraction and addition should result in 12", 12, result);
        verify(calculatorService).chainCalculate(10, operations);
    }

    @Test
    public void testChainCalculate_EmptyOperations() {
        // Given
        List<Pair<Operation, Number>> operations = Arrays.asList();  // No operations
        when(calculatorService.chainCalculate(10, operations)).thenReturn(10);

        // When
        Number result = calculatorController.chainCalculate(10, operations);

        // Then
        assertEquals("Chaining with no operations should return the initial value", 10, result);
        verify(calculatorService).chainCalculate(10, operations);
    }

}

