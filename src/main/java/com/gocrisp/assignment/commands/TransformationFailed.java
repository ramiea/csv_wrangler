package com.gocrisp.assignment.commands;

public class TransformationFailed extends Exception {

    private final String message;

    public TransformationFailed(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
