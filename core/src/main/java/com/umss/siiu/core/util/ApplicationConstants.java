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

    // File
    public static final String FILE_WITHOUT_PREVIEW = "File cannot be displayed";
    public static final String VIEW_AND_DOWNLOAD_FILE = "filename=\"%s\"";

    // Oauth2
    public static final String GRANT_TYPE = "password";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    // JPA-Data
    public static final int ONE_RESULT_MODIFIED = 1;

    public static final String BLANK_SPACE_SEPARATOR = " ";

    // CORS
    public static final List<String> ALLOWED_DOMAIN = Collections
            .unmodifiableList(Arrays.asList("http://localhost:3000", "http://dashboardmetrics", "http://192.168.65.54",
                    "http://192.168.0.1"));

    // Generic exception messages
    public static final String ERROR_PROCESSING_REQUEST = "No preview available";
}
