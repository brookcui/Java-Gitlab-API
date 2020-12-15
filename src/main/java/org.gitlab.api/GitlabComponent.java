package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * This is an abstract class that Gitlab component classes must extend from.
 * This supports binding component instance with HTTP client helper and
 * converts component attributes into JSON format. This is made abstract
 * because it should not be instantiated.
 */
abstract class GitlabComponent {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @JsonIgnore
    protected HttpClient httpClient;

    /**
     * Binds given HTTP client helper to this component.
     *
     * @param httpClient the HTTP client to send HTTP requests
     * @return this component
     */
    GitlabComponent withHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * Returns the serialized attributes of this {@code GitlabComponent} in
     * JSON format string.
     *
     * @return the JSON string
     * @throws {@code JsonProcessingException} if failed to parse attributes into JSON
     */
    public String toJsonString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // should never happen
            throw new GitlabException(e);
        }
    }
}