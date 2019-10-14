package com.gocrisp.assignment.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

/**
 * Takes a number and formats it from given input locale to output locale.
 */
@JsonTypeName("parse_number")
public class FormatNumber implements Command {

    private final NumberFormat inputFormat;
    private final NumberFormat outputFormat;

    @JsonCreator
    public FormatNumber(
        @JsonProperty("input_lang") String inputLang,
        @JsonProperty("input_country") String inputCountry,
        @JsonProperty("output_lang") String outputLang,
        @JsonProperty("output_country") String outputCountry,
        @JsonProperty("min_decimal_places") Integer minDecimalPlaces,
        @JsonProperty("max_decimal_places") Integer maxDecimalPlaces,
        @JsonProperty(value = "is_grouping_used") boolean isGroupingUsed
        ) {
        if (inputLang == null || inputCountry == null) {
            throw new NullPointerException("input country and language required");
        }
        if (outputLang == null || outputCountry == null) {
            throw new NullPointerException("output country and language required");
        }
        Locale inputLocale = new Locale(inputLang, inputCountry);
        Locale outputLocale = new Locale(outputLang, outputCountry);
        inputFormat = NumberFormat.getInstance(inputLocale);
        outputFormat = NumberFormat.getInstance(outputLocale);
        outputFormat.setGroupingUsed(isGroupingUsed);
        if (minDecimalPlaces != null) {
            outputFormat.setMinimumFractionDigits(minDecimalPlaces);
        }
        if (maxDecimalPlaces != null) {
            outputFormat.setMaximumFractionDigits(maxDecimalPlaces);
            outputFormat.setRoundingMode(RoundingMode.HALF_UP);
        }
    }

    @Override
    public String execute(Map<String, String> inputRow, String currentColumnValue) throws TransformationFailed {
        try {
            Number n = inputFormat.parse(currentColumnValue);
            return outputFormat.format(n);
        } catch (ParseException | IllegalArgumentException e) {
            throw new TransformationFailed(e.getMessage());
        }
    }
}
