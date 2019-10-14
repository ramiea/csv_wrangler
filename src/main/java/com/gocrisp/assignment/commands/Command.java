package com.gocrisp.assignment.commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "name"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EnterValue.class, name = "value"),
    @JsonSubTypes.Type(value = ReadColumn.class, name = "read"),
    @JsonSubTypes.Type(value = ParseAsDate.class, name = "parse_date"),
    @JsonSubTypes.Type(value = FormatNumber.class, name = "parse_number"),
    @JsonSubTypes.Type(value = PerformRegex.class, name = "perform_regex")
})
public interface Command {

    /**
     * Execute a transformation on a given row.
     * @param inputRow a map which represents the current input row, where key is column name and value is column value.
     * @param currentColumnValue the current value of the column output
     * @return the result after the transformation
     * @throws TransformationFailed an error happened during transformation process
     */
    String execute(Map<String, String> inputRow, String currentColumnValue) throws TransformationFailed;
}
