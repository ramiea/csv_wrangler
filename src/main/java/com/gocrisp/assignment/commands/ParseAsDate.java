package com.gocrisp.assignment.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Takes a date and formats it from given input locale to output locale.
 */
@JsonTypeName("parse_date")
public class ParseAsDate implements Command {

    private final SimpleDateFormat inputFormat;
    private final SimpleDateFormat outputFormat;

    @JsonCreator
    public ParseAsDate(
        @JsonProperty("input_format") String inputFormatPattern,
        @JsonProperty("output_format") String outputFormatPattern
    ) {
        this.inputFormat = new SimpleDateFormat(inputFormatPattern);
        this.outputFormat = new SimpleDateFormat(outputFormatPattern);
    }

    @Override
    public String execute(Map<String, String> inputRow, String currentColumnValue) throws TransformationFailed {
        try {
            Date date = inputFormat.parse(currentColumnValue);
            return outputFormat.format(date);
        } catch (ParseException e) {
            throw new TransformationFailed(e.getMessage());
        }
    }
}
