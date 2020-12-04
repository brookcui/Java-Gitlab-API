package org.gitlab.api.models;

import org.gitlab.api.http.Config;

public interface AuthComponent {
    Config getConfig();

    AuthComponent withConfig(Config config);
}
