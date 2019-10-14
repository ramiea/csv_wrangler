package com.gocrisp.assignment;

import com.gocrisp.assignment.commands.Command;
import com.gocrisp.assignment.commands.TransformationFailed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColumnTransformerTest {

    private ColumnTransformer columnTransformer;

    @Mock
    Command command1;

    @Mock
    Command command2;

    @BeforeEach
    void setUp() {
        List<Command> commands = Arrays.asList(command1, command2);
        columnTransformer = new ColumnTransformer("name", commands);
    }

    @Test
    void shouldNotInitializeEmptyColumnTransformer() {
        assertThrows(
            NullPointerException.class, () -> new ColumnTransformer(null, Collections.singletonList(command1))
        );

        assertThrows(
            NullPointerException.class, () -> new ColumnTransformer("name", Collections.emptyList())
        );

    }


    @Test
    void shouldTransformColumn() throws TransformationFailed {
        when(command1.execute(any(), eq(""))).thenReturn("foo");
        when(command2.execute(any(), eq("foo"))).thenReturn("bar");
        Map<String, String> inputRow = new HashMap<>();
        Map<String, String> outputRow = new HashMap<>();
        String result = columnTransformer.transformColumn(1, inputRow);
        verify(command1, times(1)).execute(any(), eq(""));
        verify(command2, times(1)).execute(any(), eq("foo"));
        assertEquals("bar", result);
    }

    @Test
    void shouldSetBlankRowIfFailure() throws TransformationFailed {
        when(command1.execute(any(), any())).thenThrow(TransformationFailed.class);
        Map<String, String> inputRow = new HashMap<>();
        Map<String, String> outputRow = new HashMap<>();
        String result = columnTransformer.transformColumn(1, inputRow);
        assertEquals("", result);
        verify(command2, never()).execute(any(), any());
    }
}