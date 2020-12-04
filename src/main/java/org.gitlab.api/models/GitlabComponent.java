package org.gitlab.api.models;

import org.gitlab.api.http.Config;

public interface GitlabComponent {
    Config getConfig();

    GitlabComponent withConfig(Config config);
}