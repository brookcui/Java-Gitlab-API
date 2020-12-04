package org.gitlab.api.models;

public class GitlabException extends RuntimeException {

    public GitlabException(Throwable cause) {
        super(cause);
    }

    public GitlabException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public GitlabException(String message) {
        super(message);
    }
}
