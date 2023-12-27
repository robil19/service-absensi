package com.intelsysdata.absensi.exception;

public class ElementAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ElementAlreadyExistsException() {
        super();
    }

    public ElementAlreadyExistsException(String message) {
        super(message);
    }

    public ElementAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
