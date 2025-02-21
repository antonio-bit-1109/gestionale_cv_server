package org.example.progetto_gestionale_cv_server.utility.customExceptions;

public class FileDoesntExist extends RuntimeException {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FileDoesntExist(String message, String key) {
        super(message);
        this.key = key;
    }
}
