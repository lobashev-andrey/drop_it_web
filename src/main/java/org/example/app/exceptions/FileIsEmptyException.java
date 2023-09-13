package org.example.app.exceptions;

public class FileIsEmptyException extends Exception{

    private String message;

    public FileIsEmptyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
