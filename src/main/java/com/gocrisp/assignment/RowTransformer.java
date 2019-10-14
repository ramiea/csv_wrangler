package com.gocrisp.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DSL that defines how to generate an output row for a given input row.
 */
public final class RowTransformer {

    private final List<ColumnTransformer> columnTransformers;
    private final String[] outputHeaders;

    /**
     *
     * @param columnTransformers the list of transformers for building output columns.
     */
    @JsonCreator
    public RowTransformer(@JsonProperty("columns") List<ColumnTransformer> columnTransformers) {
        if (columnTransformers == null || columnTransformers.isEmpty()) {
            throw new NullPointerException("row transformer requires non empty column list");
        }
        this.columnTransformers = columnTransformers;
        outputHeaders = columnTransformers.stream().map(ColumnTransformer::getColumnName)
            .toArray(String[]::new);
    }

    String[] getOutputHeaders() {
        return outputHeaders;
    }

    Map<String, String> transformRow(int rowNumber, Map<String, String> inputRow) {
        HashMap<String, String> outputRow = new HashMap<>();
        for (ColumnTransformer columnTransformer : columnTransformers) {
            String key = columnTransformer.getColumnName();
            String value = columnTransformer.transformColumn(rowNumber, inputRow);
            outputRow.put(key, value);
        }
        return Collections.unmodifiableMap(outputRow);
    }

}
