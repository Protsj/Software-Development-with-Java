package com.university.system;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

class RevenueCalculatorExtension implements BeforeEachCallback, AfterEachCallback {
    private final RevenueCalculator calculator;

    RevenueCalculatorExtension(RevenueCalculator calculator) {
        this.calculator = calculator;
    }

    RevenueCalculator calculator() {
        return calculator;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        calculator.reset();
        calculator.start();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        calculator.stop();
    }
}
