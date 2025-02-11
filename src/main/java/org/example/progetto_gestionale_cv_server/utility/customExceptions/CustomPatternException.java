package org.example.progetto_gestionale_cv_server.utility.customExceptions;

public class CustomPatternException extends RuntimeException {

    private final String keyField;

    public CustomPatternException(String message, String key) {
        super(message);
        this.keyField = key;
    }

    public String getKeyField() {
        return keyField;
    }
}
