package org.gitlab.api.models;

public class GitLabException extends RuntimeException{

    private int status;

    public GitLabException(Throwable cause) {
        super(cause);
    }
}
