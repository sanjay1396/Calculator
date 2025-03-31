package com.ebay.calculator;

import com.ebay.calculator.controller.CalculatorController;
import com.ebay.calculator.model.Operation;
import com.ebay.calculator.service.CalculatorService;
import com.ebay.calculator.dto.CalculationInput;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
        ResponseEntity<Number> response = calculatorController.calculate(Operation.ADD, "5", "3");

        // Then
        assertNotNull(response.getBody());
        assertEquals(8, response.getBody().intValue());
        verify(calculatorService).calculate(Operation.ADD, 5, 3);
    }

    @Test
    public void testCalculate_Subtraction() {
        // Given
        when(calculatorService.calculate(Operation.SUBTRACT, 5, 3)).thenReturn(2);

        // When
        ResponseEntity<Number> response = calculatorController.calculate(Operation.SUBTRACT, "5", "3");

        // Then
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().intValue());
        verify(calculatorService).calculate(Operation.SUBTRACT, 5, 3);
    }

    @Test
    public void testCalculate_Multiplication() {
        // Given
        when(calculatorService.calculate(Operation.MULTIPLY, 5, 3)).thenReturn(15);

        // When
        ResponseEntity<Number> response = calculatorController.calculate(Operation.MULTIPLY, "5", "3");

        // Then
        assertNotNull(response.getBody());
        assertEquals(15, response.getBody().intValue());
        verify(calculatorService).calculate(Operation.MULTIPLY, 5, 3);
    }

    @Test
    public void testCalculate_Division() {
        // Given
        when(calculatorService.calculate(Operation.DIVIDE, 6, 3)).thenReturn(2);

        // When
        ResponseEntity<Number> response = calculatorController.calculate(Operation.DIVIDE, "6", "3");

        // Then
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().intValue());
        verify(calculatorService).calculate(Operation.DIVIDE, 6, 3);
    }

    @Test
    public void testCalculate_DivideByZero() {
        // Given
        when(calculatorService.calculate(Operation.DIVIDE, 5, 0)).thenThrow(new IllegalArgumentException("Cannot divide by zero"));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            calculatorController.calculate(Operation.DIVIDE, "5", "0")
        );
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
        ResponseEntity<Number> response = calculatorController.chainCalculate("5", operations);

        // Then
        assertNotNull(response.getBody());
        assertEquals(16, response.getBody().intValue());
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
        ResponseEntity<Number> response = calculatorController.chainCalculate("10", operations);

        // Then
        assertNotNull(response.getBody());
        assertEquals(12, response.getBody().intValue());
        verify(calculatorService).chainCalculate(10, operations);
    }

    @Test
    public void testChainCalculate_EmptyOperations() {
        // Given
        List<Pair<Operation, Number>> operations = Collections.emptyList();
        when(calculatorService.chainCalculate(10, operations)).thenReturn(10);

        // When
        ResponseEntity<Number> response = calculatorController.chainCalculate("10", operations);

        // Then
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().intValue());
        verify(calculatorService).chainCalculate(10, operations);
    }

    @Test
    public void testEvaluateExpression_Valid() {
        // Given
        String expression = "5 + 3 * 2";
        CalculationInput input = new CalculationInput(expression);
        when(calculatorService.evaluateExpression(expression)).thenReturn("11");

        // When
        ResponseEntity<String> response = calculatorController.evaluateExpression(input);

        // Then
        assertNotNull(response.getBody());
        assertEquals("11", response.getBody());
        verify(calculatorService).evaluateExpression(expression);
    }

    @Test
    public void testEvaluateExpression_Invalid() {
        // Given
        String expression = "5 + + 3";
        CalculationInput input = new CalculationInput(expression);

        // When
        ResponseEntity<String> response = calculatorController.evaluateExpression(input);

        // Then
        assertNotNull(response.getBody());
        assertEquals("bad input format or value", response.getBody());
    }
}

