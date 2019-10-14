package com.gocrisp.assignment.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;


class EnterValueTest {

    private Command command;
    private Map<String, String> row;

    @BeforeEach
    void setUp() {
        command = new EnterValue("bar");
        row = new HashMap<>();
    }


    @Test
    void valueRequired() {
        Assertions.assertThrows(NullPointerException.class,
            () -> new EnterValue(null));
    }

    @Test
    void executeWithNonNull() {
        String output = null;
        try {
            output = command.execute(row, "foo");
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }
        Assertions.assertEquals("foobar", output);
    }

    @Test
    void executeWithNull() {
        String output = null;
        try {
            output = command.execute(row, null);
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }
        Assertions.assertEquals("bar", output);
    }
}