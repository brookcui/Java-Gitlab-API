package org.gitlab.api;

/**
 * This is an Enum class to represent all types of authentication methods
 * to authenticate with Gitlab API.
 */
public enum AuthMethod {
    /**
     * The authentication method using OAuth2 token.
     */
    OAUTH2("Authorization", "Bearer %s"),
    /**
     * The authentication method using personal access token.
     */
    ACCESS_TOKEN("PRIVATE-TOKEN", "%s");
    /**
     * The header name for this authentication method.
     */
    private final String headerName;
    /**
     * The header value format for this authentication method.
     */
    private final String headerFormat;

    /**
     * Constructs the {@code AuthMethod} with header name and value.
     *
     * @param headerName   the header name
     * @param headerFormat the header value format
     */
    AuthMethod(String headerName, String headerFormat) {
        this.headerName = headerName;
        this.headerFormat = headerFormat;
    }

    /**
     * Returns the token header name of this {@code AuthMethod}.
     *
     * @return token header name
     */
    public String getHeaderName() {
        return headerName;
    }

    /**
     * Returns the token header format of this {@code AuthMethod}.
     *
     * @return token header format
     */
    public String getHeaderFormat() {
        return headerFormat;
    }
}
