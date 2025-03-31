package com.ebay.calculator.model;

/**
 * Enum representing the supported operations in the calculator.
 * Each operation is associated with a symbol and a priority level for order of operations.
 */
public enum Operation {
    ADD("+", 1),              // Addition operation, lowest priority
    SUBTRACT("-", 1),         // Subtraction operation, same priority as addition
    MULTIPLY("*", 2),         // Multiplication operation, higher priority than add/subtract
    DIVIDE("/", 2),           // Division operation, same priority as multiplication
    POWER("^", 3);            // Exponentiation, high priority

    private final String symbol;
    private final int priority;

    /**
     * Constructor to initialize the operation with a symbol and priority.
     * 
     * @param symbol the symbol representing the operation (e.g., "+" for addition).
     * @param priority the priority level of the operation (higher number indicates higher priority).
     */
    Operation(String symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    /**
     * Getter method to retrieve the symbol of the operation.
     * 
     * @return the symbol representing the operation.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Getter method to retrieve the priority of the operation.
     * 
     * @return the priority level of the operation.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Static method to retrieve an operation by its symbol.
     * 
     * @param symbol the symbol of the operation.
     * @return the corresponding Operation.
     * @throws IllegalArgumentException if the symbol doesn't correspond to any operation.
     */
    public static Operation getBySymbol(String symbol) {
        for (Operation op : Operation.values()) {
            if (op.getSymbol().equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operation symbol: " + symbol);
    }
}

