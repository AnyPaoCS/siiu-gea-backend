package com.umss.siiu.core.exceptions;

public class BlockedFileException extends RuntimeException {

    private static final long serialVersionUID = 3430537040580475911L;

    public BlockedFileException() {
        super();
    }

    public BlockedFileException(String message) {
        super(message);
    }

    public BlockedFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockedFileException(Throwable cause) {
        super(cause);
    }

    protected BlockedFileException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
