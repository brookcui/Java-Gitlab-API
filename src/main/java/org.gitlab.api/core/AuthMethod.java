package org.gitlab.api.core;

/**
 * This is a enum class to represent different types of authentication method to the gitlab api
 */
public enum AuthMethod {
    OAUTH2("Authorization", "Bearer %s"),
    ACCESS_TOKEN("PRIVATE-TOKEN", "%s");

    private final String tokenHeaderName;


    private final String tokenHeaderFormat;

    AuthMethod(String tokenHeaderName, String tokenHeaderFormat) {
        this.tokenHeaderName = tokenHeaderName;
        this.tokenHeaderFormat = tokenHeaderFormat;
    }

    /**
     * Get the token header name of the auth method
     * @return token header name associated with current auth method
     */
    public String headerName() {
        return tokenHeaderName;
    }

    /**
     * Get the token header format of the auth method
     * @return token header format associated with current auth method
     */
    public String headerFormat() {
        return tokenHeaderFormat;
    }
}
