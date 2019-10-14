package com.gocrisp.assignment.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class ReadColumnTest {

    private Command readColumn;
    private Map<String, String> row;

    @BeforeEach
    void setUp() {
        readColumn = new ReadColumn("name");
        row = new HashMap<>();
    }

    @Test
    void shouldUpdateNullInput() {
        row.put("name", "hat");
        try {
            String output = readColumn.execute(row, null);
            assertEquals("hat", output);
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }
    }

    @Test
    void shouldUpdateNonNullInput() {
        row.put("name", "bar");
        try {
            String output = readColumn.execute(row, "foo");
            assertEquals("foobar", output);
        } catch (TransformationFailed transformationFailed) {
            transformationFailed.printStackTrace();
            fail();
        }
    }

    @Test
    void shouldFailReadInvalidColumn() {
        assertThrows(TransformationFailed.class,
            () -> readColumn.execute(row, "age")
        );
    }
}