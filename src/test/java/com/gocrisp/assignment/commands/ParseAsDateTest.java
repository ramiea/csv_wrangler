package com.gocrisp.assignment.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParseAsDateTest {

    private ParseAsDate parseAsDate;
    private Map<String, String> row;


    @BeforeEach
    void setUp() {
        parseAsDate = new ParseAsDate("yyyy-dd-mm", "dd-mm-yyyy");
        row = new HashMap<>();
    }

    @Test
    void shouldFailInvalidInput() {
        Assertions.assertThrows(
            TransformationFailed.class, () -> parseAsDate.execute(row, "invalid date")
        );
    }

    @Test
    void shouldSucceedValidInput() {
        try {
            String output = parseAsDate.execute(row, "1990-10-03");
            assertEquals("10-03-1990", output);
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }

    }

}