package org.gitlab.api;

/**
 * Signals that an exception occurring during invoking Gitlab REST API. This
 * class is the general class of exceptions produced by failed or interrupted
 * Gitlab API calls. This supports providing detailed error message and root
 * causes.
 */
public class GitlabException extends RuntimeException {

    /**
     * Constructs {@code GitlabException} with given root cause.
     *
     * @param cause the cause of this exception
     */
    public GitlabException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs {@code GitlabException} with given message and root cause.
     *
     * @param message error message
     * @param cause   the cause of this exception
     */
    public GitlabException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs {@code GitlabException} with error message.
     *
     * @param message error message
     */
    public GitlabException(String message) {
        super(message);
    }
}
