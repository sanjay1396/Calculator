package com.ebay.calculator.controller;

import com.ebay.calculator.dto.CalculationInput;
import com.ebay.calculator.model.Operation;
import com.ebay.calculator.service.CalculatorService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Autowired
    private CalculatorService calculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<Number> calculate(@RequestParam Operation operation,
                                            @RequestParam String num1,
                                            @RequestParam String num2) {
        try {
            // Parsing the numbers to avoid type issues
            Number n1 = parseNumber(num1);
            Number n2 = parseNumber(num2);
            logger.info("Received basic calculation request with operation: {}, num1: {}, num2: {}", operation, n1, n2);
            Number result = calculatorService.calculate(operation, n1, n2);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error occurred during calculation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(0);
        }
    }

    @PostMapping("/chain")
    public ResponseEntity<String> chainCalculate(@RequestParam String initialValue,
                                                 @RequestBody List<Pair<Operation, String>> operations) {
        try {
            // Parsing initial value and numbers for operations
            Number initialNum = parseNumber(initialValue);
            List<Pair<Operation, Number>> processedOperations = new ArrayList<>();
            for (Pair<Operation, String> operationPair : operations) {
                processedOperations.add(Pair.of(operationPair.getLeft(), parseNumber(operationPair.getRight())));
            }
            Number result = calculatorService.chainCalculate(initialNum, processedOperations);
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            logger.error("Error in chained calculation: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Bad input format or value");
        }
    }

    @PostMapping("/v1/evaluate")
    public ResponseEntity<String> evaluateExpression(@RequestBody CalculationInput input) {
        try {
            String[] parts = input.getExpression().split("\\s+");
            Number initialResult = parseNumber(parts[0]);
            List<Pair<Operation, Number>> operations = new ArrayList<>();

            for (int i = 1; i < parts.length; i += 2) {
                if (i + 1 < parts.length) {
                    Operation operation = Operation.getBySymbol(parts[i]);
                    Number value = parseNumber(parts[i + 1]);
                    operations.add(Pair.of(operation, value));
                }
            }

            Number result = calculatorService.chainCalculate(initialResult, operations);
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            logger.error("Error evaluating expression: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Bad input format or value");
        }
    }

    private Number parseNumber(String numberStr) {
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(numberStr);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid number format: " + numberStr);
            }
        }
    }
}
