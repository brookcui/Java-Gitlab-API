package org.gitlab.api.core;

/**
 * This is a custom exception class that wraps other exceptions to provide user with more detailed exceptions.
 */
public class GitlabException extends RuntimeException {

    /**
     * Constructor of the gitlab exception
     * @param cause cause of the exception
     */
    public GitlabException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor of the gitlab exception
     * @param message error message
     * @param cause cause of the exception
     */
    public GitlabException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of the gitlab exception
     * @param message error message
     */
    public GitlabException(String message) {
        super(message);
    }
}
