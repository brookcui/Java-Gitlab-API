package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Top level interface that all of the gitlab components must extend.
 * By default read only, meaning it cannot be created, modified or deleted.
 */
abstract class GitlabComponent {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @JsonIgnore
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

    /**
     * Return the the JSON representation of this {@link GitlabComponent}
     *
     * @return the JSON representation of this {@link GitlabComponent}
     */
    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // should never happen
            throw new GitlabException(e);
        }
    }
}