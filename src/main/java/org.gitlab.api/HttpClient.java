package org.gitlab.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to send HTTP request to the with the given Gitlab httpClient,
 * and then deserialize the JSON response to the corresponding {@link GitlabComponent} class.
 */
class HttpClient {
    /**
     * The Jackson Mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    /**
     * The default media type to be sent in PUT and POST
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * The empty body to be used for PUT and POST
     */
    private static final RequestBody EMPTY_BODY = RequestBody.create("", null);
    /**
     * The internal OkHttpClient
     */
    private final OkHttpClient client;
    private final String apiPrefix;
    private final String authHeaderName;
    private final String authHeaderValue;

    /**
     * Initialize the {@link HttpClient} based on timeouts, proxy, api endpoint namespace as well as the authentication.
     *
     * @param gitlabAPIClient the {@link GitlabAPIClient} for creating this {@link HttpClient}
     */
    HttpClient(GitlabAPIClient gitlabAPIClient) {
        client = new OkHttpClient.Builder()
                .connectTimeout(gitlabAPIClient.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(gitlabAPIClient.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(gitlabAPIClient.getReadTimeout(), TimeUnit.MILLISECONDS)
                .proxy(gitlabAPIClient.getProxy())
                .build();
        apiPrefix = gitlabAPIClient.getEndpoint() + gitlabAPIClient.getApiNamespace();
        if (gitlabAPIClient.getAuthMethod() != null) {
            authHeaderName = gitlabAPIClient.getAuthMethod().headerName();
            authHeaderValue = String.format(gitlabAPIClient.getAuthMethod().headerFormat(), gitlabAPIClient.getToken());
        } else {
            authHeaderName = null;
            authHeaderValue = null;
        }

    }

    /**
     * Create a new object of the given type from the JSON response
     *
     * @param response the JSON response
     * @param type     the type for deserialization
     * @param <T>      the type
     * @return the newly created {@link GitlabComponent}
     * @throws GitlabException if {@link IOException} occurs
     */
    private static <T> T create(String response, Class<T> type) {
        try {
            return MAPPER.readValue(response, type);
        } catch (IOException e) {
            throw new GitlabException("Response cannot be parsed", e);
        }
    }

    /**
     * Update a given object from the JSON response
     *
     * @param response the JSON response
     * @param object   the object to be updated
     * @param <T>      the type
     * @return the updated object
     * @throws GitlabException if {@link IOException} occurs
     */
    private static <T> T update(String response, T object) {
        try {
            return MAPPER.readerForUpdating(object).readValue(response);
        } catch (IOException e) {
            throw new GitlabException("Response cannot be parsed", e);
        }
    }

    /**
     * Given the tailUrl, e.g. /projects/1234,
     * return the entire API url based on {@link #apiPrefix}
     * e.g. https://gitlab.com/api/v4/projects/1234
     *
     * @param tailUrl the API tail Url, e.g.  /projects/1234
     * @return the entire API url based on apiPrefix
     * e.g. https://gitlab.com/api/v4/projects/1234
     */
    private String getAPIUrl(String tailUrl) {
        if (!tailUrl.startsWith("/")) {
            tailUrl = "/" + tailUrl;
        }
        return apiPrefix + tailUrl;
    }

    /**
     * Attach a httpClient to a given {@link GitlabComponent} then return it
     *
     * @param component the {@link GitlabComponent} to be attached the httpClient with
     * @param <T>       the type of the {@link GitlabComponent}
     * @return the component after attaching the httpClient
     */
    private <T extends GitlabComponent> T attachHttpClient(T component) {
        component.withHttpClient(this);
        return component;
    }

    /**
     * Given the Gitlab httpClienturation, the endpoint tail url and the expected return type,
     * issue a GET request to the endpoint and deserialize the JSON response to a object with the given type
     *
     * @param tailUrl the tail url of the endpoint
     * @param type    the class of the expected result
     * @param <T>     the type
     * @return a {@link GitlabComponent} with the given type
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    <T extends GitlabComponent> T get(String tailUrl, Class<T> type) {
        return attachHttpClient(create(request(tailUrl, Method.GET, null), type));
    }

    /**
     * Given the Gitlab httpClienturation, the endpoint tail url and the expected return type (which expects a list)
     * issue a get request to the endpoint and deserialize the JSON response to a list of object with the given type
     *
     * @param tailUrl the tail url of the endpoint
     * @param type    the class of the expected result
     * @param <T>     the type
     * @return a list of {@link GitlabComponent} with the given type
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    <T extends GitlabComponent> List<T> getList(String tailUrl, Class<T[]> type) {
        return createList(request(tailUrl, Method.GET, null), type);
    }

    /**
     * Given the Gitlab httpClienturation, the endpoint tail url, the body and the component to be updated,
     * issue a PUT request to the endpoint and deserialize the JSON response to update the given component
     *
     * @param tailUrl   the tail url of the endpoint
     * @param body      the body of the PUT request
     * @param component the component to be updated in place
     * @param <T>       the type
     * @return the {@code component} after being updated
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    <T extends GitlabComponent> T put(String tailUrl, Body body, T component) {
        return attachHttpClient(update(request(tailUrl, Method.PUT, body), component));
    }

    /**
     * Given the Gitlab httpClienturation, the endpoint tail url, the body and the component to be updated,
     * issue a POST request to the endpoint and deserialize the JSON response to update the given component
     *
     * @param tailUrl   the tail url of the endpoint
     * @param body      the body of the PUT request
     * @param component the component to be updated in place
     * @param <T>       the type
     * @return the {@code component} after being updated
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    <T extends GitlabComponent> T post(String tailUrl, Body body, T component) {
        return attachHttpClient(update(request(tailUrl, Method.POST, body), component));
    }

    /**
     * Given the Gitlab httpClienturation and the endpoint tail url
     * issue a DELETE request to the endpoint
     *
     * @param tailUrl the tail url of the endpoint
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    void delete(String tailUrl) {
        request(tailUrl, Method.DELETE, null);
    }

    /**
     * Create a list based of {@link GitlabComponent} based on the JSON response, and array type and attach the httpClient
     *
     * @param response the JSON response
     * @param type     the array type for deserialization
     * @param <T>      the type
     * @return the list of {@link GitlabComponent} with httpClient attached
     * @throws GitlabException if {@link IOException} occurs
     */
    <T extends GitlabComponent> List<T> createList(String response, Class<T[]> type) {
        try {
            T[] array = MAPPER.readValue(response, type);
            if (array == null) {
                return Collections.emptyList();
            }
            List<T> instances = Arrays.asList(array);
            instances.forEach(instance -> instance.withHttpClient(this));
            return instances;
        } catch (IOException e) {
            throw new GitlabException("Response cannot be parsed", e);
        }
    }

    /**
     * Issue a HTTP request to the Gitlab endpoint from the given httpClient, tail url, HTTP method the the body data
     *
     * @param tailUrl the tail url of the endpoint
     * @param method  method he HTTP method to be used in this request
     * @param body    the body to be used
     * @return the JSON response
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    private String request(String tailUrl, Method method, Body body) {
        Request request;
        try {
            Request.Builder builder = new Request.Builder()
                    .url(getAPIUrl(tailUrl))
                    .method(method.name(),
                            body == null ?
                                    // send empty body for post and put if no body is provided
                                    method.equals(Method.POST) || method.equals(Method.PUT) ? EMPTY_BODY : null :
                                    RequestBody.create(MAPPER.writeValueAsString(body.getMap()), JSON));
            if (authHeaderName != null) {
                builder.addHeader(authHeaderName, authHeaderValue);
            }
            request = builder.build();
            System.out.println("URL:" + request.url());
            System.out.println("Body:" + request.body());
        } catch (JsonProcessingException e) {
            // should never happen
            throw new GitlabException("Cannot serialize", e);
        }
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (response.isSuccessful()) {
                return responseBody;
            }
            throw new GitlabException(String
                    .format("Response code %d: %s\n%s", response.code(), response.message(), responseBody));
        } catch (IOException e) {
            throw new GitlabException(e);
        }
    }

    /**
     * Current supported HTTP methods
     */
    private enum Method {
        GET, PUT, POST, DELETE;
    }

}