package com.ora.notebook.model;

public class NoteBookException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoteBookException() {
    }

    public NoteBookException(String message) {
        super(message);
    }
}
