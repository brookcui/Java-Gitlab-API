package org.gitlab.api;

/**
 * This is a enum class to represent different types of authentication method to the gitlab api
 */
public enum AuthMethod {
    /**
     * The authentication method for OAuth2 token
     */
    OAUTH2("Authorization", "Bearer %s"),
    /**
     * The authentication method for personal access token
     */
    ACCESS_TOKEN("PRIVATE-TOKEN", "%s");
    /**
     * The header name for this authentication method
     */
    private final String headerName;
    /**
     * The header value format for this authentication method
     */
    private final String headerFormat;

    /**
     * Initialize the authentication method
     *
     * @param headerName   the header name for this authentication method
     * @param headerFormat the header value format for this authentication method
     */
    AuthMethod(String headerName, String headerFormat) {
        this.headerName = headerName;
        this.headerFormat = headerFormat;
    }

    /**
     * Get the token header name of the auth method
     *
     * @return token header name associated with current auth method
     */
    public String headerName() {
        return headerName;
    }

    /**
     * Get the token header format of the auth method
     *
     * @return token header format associated with current auth method
     */
    public String headerFormat() {
        return headerFormat;
    }
}
