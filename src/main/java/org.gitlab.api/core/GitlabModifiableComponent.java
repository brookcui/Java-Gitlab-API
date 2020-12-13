package org.gitlab.api.core;

/**
 * All of the modifiable {@link GitlabComponent} must implement this interface and support update method.
 * Modifiable means the component can not only be created and deleted, but also modified
 *
 * @param <T> type of the subtype that implements this interface
 */
interface GitlabModifiableComponent<T extends GitlabComponent> extends GitlabWritableComponent<T> {
    /**
     * Issue a HTTP request to the Gitlab API endpoint to update this {@link GitlabComponent} based on
     * the fields in this {@link GitlabComponent} currently
     *
     * @return the updated {@link GitlabComponent} component
     */
    T update();
}