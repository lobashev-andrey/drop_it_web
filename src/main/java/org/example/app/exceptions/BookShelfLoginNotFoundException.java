package org.example.app.exceptions;

public class BookShelfLoginNotFoundException extends Exception {

    private final String message;

    public BookShelfLoginNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
