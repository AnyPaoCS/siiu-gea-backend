package com.umss.siiu.core.util;

public class ApiPath {

    private ApiPath() {
        super();
    }

    public static final String BASE_URL = "/";

    //Api path
    public static final String API_URL = "api" + BASE_URL;

    //Constants for User
    public static final String USER_PATH = BASE_URL + "users";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String EMPLOYEE_URL = API_URL + USER_PATH + BASE_URL + "employee";
    public static final String REFERRAL_INVITATION = "sendSignUpInvitation";
    public static final String FORGOTTEN_PASSWORD = BASE_URL + "forgottenPassword";
    public static final String RESTORE_PASSWORD = BASE_URL + "restorePassword";

    //Constants for Web Scraping API
    public static final String GET_TRUSTED_SITES_BY_LOCATION = "getTrustedSitesByLocation";
    public static final String GET_TRUSTED_SITES_BY_SITE_TYPE = "getTrustedSitesBySiteType";
    public static final String GET_TRUSTED_SITES = "getTrustedSites";
}
