package com.gocrisp.assignment.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Map;

/**
 * Adds a value to the existing output.
 */
@JsonTypeName("value")
public class EnterValue implements Command {

    private String value;

    @JsonCreator
    public EnterValue(@JsonProperty(value = "value", required = true) String value) {
        if (value == null) {
            throw new NullPointerException("value property required");
        }
        this.value = value;
    }

    @Override
    public String execute(Map<String,String> inputRow, String currentColumnValue) {
        return (currentColumnValue == null) ? value : currentColumnValue + value;
    }
}
