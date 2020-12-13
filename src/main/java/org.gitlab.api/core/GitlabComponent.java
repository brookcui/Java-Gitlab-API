package org.gitlab.api.core;

import org.gitlab.api.http.Config;

/**
 * Top level interface that all of the gitlab components must implement.
 * By default read only, meaning it cannot be created, modified or deleted.
 */
public interface GitlabComponent {

    /**
     * Get the Gitlab configuration in this current component
     *
     * @return the Gitlab configuration in this current component
     */
    Config getConfig();

    /**
     * Associate a Gitlab configuration to this component
     *
     * @param config the Gitlab configuration
     * @return this component
     */
    GitlabComponent withConfig(Config config);
}