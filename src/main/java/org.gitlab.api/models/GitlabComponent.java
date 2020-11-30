package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHTTPRequestor;

public abstract class GitlabComponent {
    @JsonIgnore
    private GitlabHTTPRequestor requestor;

    public GitlabComponent() {
    }

    public GitlabComponent(GitlabHTTPRequestor requestor) {
        this.requestor = requestor;
    }

    public Config getConfig() {
        return requestor.getConfig();
    }

    public GitlabHTTPRequestor getHTTPRequestor() {
        return requestor;
    }

    public  GitlabComponent withHTTPRequestor(GitlabHTTPRequestor requestor){
        this.requestor = requestor;
        return this;
    }
}
