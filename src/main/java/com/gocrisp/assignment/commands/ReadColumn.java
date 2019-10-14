package com.gocrisp.assignment.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Map;

/**
 * Appends the value of the given input column to the output.
 */
@JsonTypeName("column")
public class ReadColumn implements Command {

    private String key;

    @JsonCreator
    public ReadColumn(@JsonProperty("column_name") String key) {
        if (key == null) {
            throw new NullPointerException("column name required");
        }
        this.key = key;
    }

    @Override
    public String execute(Map<String, String> inputRow, String currentColumnValue) throws TransformationFailed {
        if (!inputRow.containsKey(key)) {
            throw new TransformationFailed(key + " not found int input file");
        }
        String value = inputRow.get(key);
        return (currentColumnValue == null) ? value : currentColumnValue + value;
    }
}
