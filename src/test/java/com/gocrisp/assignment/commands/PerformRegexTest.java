package com.gocrisp.assignment.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class PerformRegexTest {

    private Command regexCommand;
    private Map<String, String> row;

    @BeforeEach
    void setUp() {
        regexCommand = new PerformRegex("PC-(\\d*)", 1);
        row = new HashMap<>();

    }

    @Test
    void shouldFailRegexMismatch() {
        assertThrows(TransformationFailed.class,
            () -> regexCommand.execute(row, "PC123 "));
    }

    @Test
    void shouldSucceedRegexMatch() {
        try {
            String output = regexCommand.execute(row, "PC-123");
            assertEquals("123", output);
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }
    }

}