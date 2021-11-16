package com.umss.siiu.core.exceptions;

public class RepositoryValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RepositoryValidationException() {
        super();
    }

    public RepositoryValidationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public RepositoryValidationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public RepositoryValidationException(String arg0) {
        super(arg0);
    }

    public RepositoryValidationException(Throwable arg0) {
        super(arg0);
    }
}
