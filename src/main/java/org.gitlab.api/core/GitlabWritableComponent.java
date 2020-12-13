package org.gitlab.api.core;


/**
 * All of the writable {@link GitlabComponent} must implement this interface and support create and delete methods.
 * Writable means the component can only be created and deleted, bu but notodified
 *
 * @param <T> type of the subtype that implements this interface
 */
interface GitlabWritableComponent<T extends GitlabComponent> extends GitlabComponent {
    /**
     * Issue a HTTP request to the Gitlab API endpoint to create this {@link GitlabComponent} based on
     * the fields in this {@link GitlabComponent} currently
     *
     * @return the created Gitlab API component
     */
    T create();

    /**
     * Issue a HTTP request to the Gitlab API endpoint to delete this {@link GitlabComponent}
     *
     * @return the Gitlab API component before deleted
     */
    T delete();
}
