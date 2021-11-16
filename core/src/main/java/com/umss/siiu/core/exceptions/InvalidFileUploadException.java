package com.umss.siiu.core.exceptions;

public class InvalidFileUploadException extends RuntimeException {

    private static final long serialVersionUID = 3430537040580475911L;

    public InvalidFileUploadException() {
        super();
    }

    public InvalidFileUploadException(String message) {
        super(message);
    }

    public InvalidFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileUploadException(Throwable cause) {
        super(cause);
    }

    protected InvalidFileUploadException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
