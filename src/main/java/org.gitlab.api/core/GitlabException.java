package org.gitlab.api.core;

public class GitlabException extends RuntimeException {

    public GitlabException(Throwable cause) {
        super(cause);
    }

    public GitlabException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitlabException(String message) {
        super(message);
    }
}
