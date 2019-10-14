package com.gocrisp.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gocrisp.assignment.commands.Command;
import com.gocrisp.assignment.commands.TransformationFailed;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * DSL that defines how to generate an output column for a given input row.
 */
public class ColumnTransformer {

    private final String columnName;
    private final List<Command> commands;

    private final Logger log;

    /**
     * DSL that defines how to generate an output column for a given input row.
     * @param name the name of the output column to be generated
     * @param commands the list of commands that need to be executed to produce output
     */
    @JsonCreator
    public ColumnTransformer(
        @JsonProperty("name") String name,
        @JsonProperty("commands") List<Command> commands
    ) {
        if (name == null) {
            throw new NullPointerException("column name required");
        }
        if (commands == null || commands.isEmpty()) {
            throw new NullPointerException("commands required");
        }
        this.columnName = name;
        this.commands = commands;
        this.log = Logger.getLogger(ColumnTransformer.class.getName());
    }

    String getColumnName() {
        return columnName;
    }

    String transformColumn(int row, Map<String, String> inputRow) {
        String result = "";
        for (Command command : commands) {
            try {
                result = command.execute(inputRow, result);
            } catch (TransformationFailed e) {
                log.warning(getMessage(row, e));
                return "";
            }
        }
        return result;
        //outputRow.put(columnName, result);
    }

    private String getMessage(int row, TransformationFailed transformationFailed) {
        return "transformation for row: " + row
            + ", column: " + columnName + " failed. Reason: "
            + transformationFailed.getMessage();
    }

}
