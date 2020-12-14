package org.gitlab.api;

/**
 * Top level interface that all of the gitlab components must implement.
 * By default read only, meaning it cannot be created, modified or deleted.
 */
abstract class GitlabComponent {
    protected HttpClient httpClient;


    /**
     * Associate a Gitlab http client to this component
     *
     * @param httpClient the HTTP client to be used
     * @return this component
     */
    GitlabComponent withHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }
}