package com.gocrisp.assignment.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class FormatNumberTest {

    private FormatNumber formatNumber;
    private Map<String, String> row;

    @BeforeEach
    void setUp() {
        formatNumber = new FormatNumber(
            "en",
            "GB",
            "de",
            "DE",
            2,
            2,
            false
        );
        row = new HashMap<>();
    }

    @Test
    void shouldFailIfInvalidInput() {
        assertThrows(TransformationFailed.class,
            () -> formatNumber.execute(row, "")
        );
    }

    @Test
    void shouldReturnCorrectFormat() {
        try {
            String output = formatNumber.execute(row, "2.123");
            assertEquals("2,12", output);
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }


    }




}