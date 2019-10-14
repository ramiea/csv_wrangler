package com.gocrisp.assignment.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs a regex and returns the given group.
 */
@JsonTypeName("perform_regex")
public class PerformRegex implements Command {

    private final Pattern pattern;
    private final int group;


    @JsonCreator
    public PerformRegex(
        @JsonProperty("regex") String regex,
        @JsonProperty("group") int group
    ) {
        if (regex == null) {
            throw new NullPointerException("column name required");
        }
        this.pattern = Pattern.compile(regex);
        this.group = group;
    }

    @Override
    public String execute(Map<String, String> inputRow, String currentColumnValue) throws TransformationFailed {
        Matcher matcher = pattern.matcher(currentColumnValue);
        if (matcher.matches()) {
            return matcher.group(group);
        }
        throw new TransformationFailed("pattern: " + pattern.pattern() + " not matched in:" + currentColumnValue);
    }
}
