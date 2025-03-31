package com.ebay.calculator.service;

import com.ebay.calculator.model.Operation;
import com.ebay.calculator.model.OperationStrategy;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class CalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    private final Map<Operation, OperationStrategy> operationMap;

    @Autowired
    public CalculatorService(Map<Operation, OperationStrategy> operationMap) {
        this.operationMap = operationMap;
    }

    public Number calculate(Operation op, Number num1, Number num2) {
        OperationStrategy strategy = operationMap.get(op);
        if (strategy != null) {
            return strategy.apply(num1, num2);
        } else {
            logger.error("Unsupported operation: {}", op);
            throw new UnsupportedOperationException("Operation not supported: " + op);
        }
    }

    public Number chainCalculate(Number initialValue, List<Pair<Operation, Number>> operations) {
        Stack<Number> numbers = new Stack<>();
        Stack<Operation> operators = new Stack<>();
        numbers.push(initialValue);

        for (Pair<Operation, Number> op : operations) {
            Operation currentOperation = op.getLeft();
            Number currentValue = op.getRight();

            while (!operators.isEmpty() && currentOperation.getPriority() <= operators.peek().getPriority()) {
                if (numbers.size() < 2) {
                    logger.error("Insufficient operands for operation: {}", operators.peek());
                    throw new IllegalStateException("Invalid calculation sequence.");
                }
                Number num2 = numbers.pop();
                Number num1 = numbers.pop();
                Operation lastOp = operators.pop();
                numbers.push(calculate(lastOp, num1, num2));
            }

            operators.push(currentOperation);
            numbers.push(currentValue);
        }

        while (!operators.isEmpty()) {
            if (numbers.size() < 2) {
                logger.error("Insufficient operands at final computation step.");
                throw new IllegalStateException("Invalid final calculation sequence.");
            }
            Number num2 = numbers.pop();
            Number num1 = numbers.pop();
            Operation lastOp = operators.pop();
            numbers.push(calculate(lastOp, num1, num2));
        }

        return numbers.pop();
    }
}

