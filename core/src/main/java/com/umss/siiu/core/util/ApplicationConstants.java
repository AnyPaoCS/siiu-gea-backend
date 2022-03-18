package com.umss.siiu.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApplicationConstants {

    private ApplicationConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Spring Security
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String UNAUTHORIZED_USER_MESSAGE = "User not authorized to perform the action, please sign in" +
            " again";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String TOKEN = "token";
    public static final String UNDEFINED = "undefined";

    // JWT
    public static final String ISSUER_INFO = "https://www.avantica.net/";
    public static final String SUPER_SECRET_KEY = "1234";
    public static final long TOKEN_EXPIRATION_TIME = 864_000_000; // 10 day

    // Oauth2
    public static final String GRANT_TYPE = "password";

    // Export
    public static final String EXCEL_APPLICATION = "application/xlsx";
    public static final String WORD_APPLICATION = "application/msword";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    // JPA-Data
    public static final int ONE_RESULT_MODIFIED = 1;

    public static final String BLANK_SPACE_SEPARATOR = " ";

    // CORS
    public static final List<String> ALLOWED_DOMAIN = Collections
            .unmodifiableList(Arrays.asList("http://localhost:4200", "http://dashboardmetrics", "http://192.168.65.54",
                    "http://192.168.0.1"));

    // Generic exception messages
    public static final String ERROR_PROCESSING_REQUEST = "No preview available";
}
