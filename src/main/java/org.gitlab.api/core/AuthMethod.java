package org.gitlab.api.core;

public enum AuthMethod {
    OAUTH2("Authorization", "Bearer %s"),
    ACCESS_TOKEN("PRIVATE-TOKEN", "%s"),
    PASSWORD("", "");

    private final String tokenHeaderName;


    private final String tokenHeaderFormat;

    AuthMethod(String tokenHeaderName, String tokenHeaderFormat) {
        this.tokenHeaderName = tokenHeaderName;
        this.tokenHeaderFormat = tokenHeaderFormat;
    }

    public String headerName() {
        return tokenHeaderName;
    }

    public String headerFormat() {
        return tokenHeaderFormat;
    }
}
