package com.umss.siiu.core.exceptions;

public class PageNotFound extends RuntimeException {

    private static final long serialVersionUID = 2271771643751286526L;

    public PageNotFound() {
        super();
    }

    public PageNotFound(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public PageNotFound(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public PageNotFound(String arg0) {
        super(arg0);
    }

    public PageNotFound(Throwable arg0) {
        super(arg0);
    }
}
