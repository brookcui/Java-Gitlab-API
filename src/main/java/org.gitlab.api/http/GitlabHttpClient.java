package org.gitlab.api.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.gitlab.api.core.GitlabComponent;
import org.gitlab.api.core.GitlabException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to send HTTP request to the with the given Gitlab configuration,
 * and then deserialize the JSON response to the corresponding {@link GitlabComponent} class.
 */
public class GitlabHttpClient {
    /**
     * The Jackson Mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    /**
     * The internal OkHttpClient
     */
    private static final OkHttpClient client = new OkHttpClient();
    /**
     * The default media type to be sent in PUT and POST
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * The empty body to be used for PUT and POST
     */
    private static final RequestBody EMPTY_BODY = RequestBody.create("", null);

    /**
     * Attach a config to a given {@link GitlabComponent} then return it
     *
     * @param component the {@link GitlabComponent} to be attached the config with
     * @param config    the config to be attached
     * @param <T>       the type of the {@link GitlabComponent}
     * @return the component after attaching the config
     */
    private static <T extends GitlabComponent> T withConfig(T component, Config config) {
        component.withConfig(config);
        return component;
    }

    /**
     * Given the Gitlab configuration, the endpoint tail url and the expected return type,
     * issue a GET request to the endpoint and deserialize the JSON response to a object with the given type
     *
     * @param config  the configuration to be used in this request
     * @param tailUrl the tail url of the endpoint
     * @param type    the class of the expected result
     * @param <T>     the type
     * @return a {@link GitlabComponent} with the given type
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public static <T extends GitlabComponent> T get(Config config, String tailUrl, Class<T> type) {
        return withConfig(create(request(config, tailUrl, Method.GET, null), type), config);
    }

    /**
     * Given the Gitlab configuration, the endpoint tail url and the expected return type (which expects a list)
     * issue a get request to the endpoint and deserialize the JSON response to a list of object with the given type
     *
     * @param config  the configuration to be used in this request
     * @param tailUrl the tail url of the endpoint
     * @param type    the class of the expected result
     * @param <T>     the type
     * @return a list of {@link GitlabComponent} with the given type
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public static <T extends GitlabComponent> List<T> getList(Config config, String tailUrl, Class<T[]> type) {
        return createList(request(config, tailUrl, Method.GET, null), type, config);
    }

    /**
     * Given the Gitlab configuration, the endpoint tail url, the body and the component to be updated,
     * issue a PUT request to the endpoint and deserialize the JSON response to update the given component
     *
     * @param config    the configuration to be used in this request
     * @param tailUrl   the tail url of the endpoint
     * @param body      the body of the PUT request
     * @param component the component to be updated in place
     * @param <T>       the type
     * @return the {@code component} after being updated
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public static <T extends GitlabComponent> T put(Config config, String tailUrl, Body body, T component) {
        return withConfig(update(request(config, tailUrl, Method.PUT, body), component), config);
    }

    /**
     * Given the Gitlab configuration, the endpoint tail url, the body and the component to be updated,
     * issue a POST request to the endpoint and deserialize the JSON response to update the given component
     *
     * @param config    the configuration to be used in this request
     * @param tailUrl   the tail url of the endpoint
     * @param body      the body of the PUT request
     * @param component the component to be updated in place
     * @param <T>       the type
     * @return the {@code component} after being updated
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public static <T extends GitlabComponent> T post(Config config, String tailUrl, Body body, T component) {
        return withConfig(update(request(config, tailUrl, Method.POST, body), component), config);
    }

    /**
     * Given the Gitlab configuration and the endpoint tail url
     * issue a DELETE request to the endpoint
     *
     * @param config  the configuration to be used in this request
     * @param tailUrl the tail url of the endpoint
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public static void delete(Config config, String tailUrl) {
        request(config, tailUrl, Method.DELETE, null);
    }

    /**
     * Create a list based of {@link GitlabComponent} based on the JSON response, and array type and attach the config
     *
     * @param response the JSON response
     * @param type     the array type for deserialization
     * @param config   the config to be attached
     * @param <T>      the type
     * @return the list of {@link GitlabComponent} with config attached
     * @throws GitlabException if {@link IOException} occurs
     */
    private static <T extends GitlabComponent> List<T> createList(String response, Class<T[]> type, Config config) {
        try {
            T[] array = MAPPER.readValue(response, type);
            if (array == null) {
                return Collections.emptyList();
            }
            List<T> instances = Arrays.asList(array);
            instances.forEach(instance -> instance.withConfig(config));
            return instances;
        } catch (IOException e) {
            throw new GitlabException("Response cannot be parsed", e);
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
     * Issue a HTTP request to the Gitlab endpoint from the given config, tail url, HTTP method the the body data
     *
     * @param config  the configuration to be used in this request
     * @param tailUrl the tail url of the endpoint
     * @param method method he HTTP method to be used in this request
     * @param body    the body to be used
     * @return the JSON response
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    private static String request(Config config, String tailUrl, Method method, Body body) {
        Request request;
        try {
            request = new Request.Builder()
                    .url(config.getAPIUrl(tailUrl))
                    .method(method.name(),
                            body == null ?
                                    // send empty body for post and put if no body is provided
                                    method.equals(Method.POST) || method.equals(Method.PUT) ? EMPTY_BODY : null :
                                    RequestBody.create(MAPPER.writeValueAsString(body.getMap()), JSON))
                    .addHeader(
                            config.getAuthMethod().headerName(), // header name
                            String.format(config.getAuthMethod().headerFormat(), config.getToken()) // header token
                    )
                    .build();
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
            throw new GitlabException(responseBody);
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