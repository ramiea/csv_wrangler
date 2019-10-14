package com.gocrisp.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RowTransformerTest {

    @Mock
    ColumnTransformer transformer1;

    @Mock
    ColumnTransformer transformer2;

    RowTransformer rowTransformer;

    @BeforeEach
    void setUp() {
        when(transformer1.getColumnName()).thenReturn("foo");
        when(transformer2.getColumnName()).thenReturn("bar");
        rowTransformer = new RowTransformer(Arrays.asList(transformer1, transformer2));
    }

    @Test
    void shouldFailIfInvalidConstructor(){
        assertThrows(NullPointerException.class, () -> new RowTransformer(null));
        assertThrows(NullPointerException.class, () -> new RowTransformer(Collections.emptyList()));
    }
    @Test
    void shouldGetHeaders() {
        assertArrayEquals(new String[]{"foo", "bar"}, rowTransformer.getOutputHeaders());
    }

    @Test
    void shouldTransformRow() {
        Map<String, String> input = new HashMap<>();
        when(transformer1.transformColumn(eq(1), eq(input))).thenReturn("val1");
        when(transformer2.transformColumn(eq(1), eq(input))).thenReturn("val2");
        Map<String, String> output = rowTransformer.transformRow(1, input);
        assertEquals("val1", output.get("foo"));
        assertEquals("val2", output.get("bar"));
    }

}