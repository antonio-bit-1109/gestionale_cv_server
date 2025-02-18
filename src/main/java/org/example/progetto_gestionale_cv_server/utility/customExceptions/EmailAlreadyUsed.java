package org.example.progetto_gestionale_cv_server.utility.customExceptions;

public class EmailAlreadyUsed extends RuntimeException {

    private final String key;

    public EmailAlreadyUsed(String message, String key) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
