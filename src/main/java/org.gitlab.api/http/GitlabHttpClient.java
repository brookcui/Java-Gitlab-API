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
import org.gitlab.api.models.GitlabComponent;
import org.gitlab.api.models.GitlabException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GitlabHttpClient {
    /**
     * The default client has no auth method.
     */
    private static final OkHttpClient client = new OkHttpClient();
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static <T extends GitlabComponent> T withConfig(T instance, Config config) {
        instance.withConfig(config);
        return instance;
    }

    public static <T extends GitlabComponent> T get(Config config, String tailUrl, Class<T> type) {
        return withConfig(create(request(config, tailUrl, Method.GET, null), type), config);
    }

    public static <T extends GitlabComponent> List<T> getList(Config config, String tailUrl, Class<T[]> type) {
        return createList(request(config, tailUrl, Method.GET, null), type, config);
    }

    public static <T extends GitlabComponent> T put(Config config, String tailUrl, Body body, T instance) {
        return withConfig(update(request(config, tailUrl, Method.PUT, body), instance), config);
    }

    public static <T extends GitlabComponent> T post(Config config, String tailUrl, Body body, T instance) {
        return withConfig(update(request(config, tailUrl, Method.POST, body), instance), config);
    }

    public static void delete(Config config, String tailUrl) {
        request(config, tailUrl, Method.DELETE, null);
    }

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

    private static <T> T create(String response, Class<T> type) {
        try {
            return MAPPER.readValue(response, type);
        } catch (IOException e) {
            throw new GitlabException("Response cannot be parsed", e);
        }
    }

    private static <T> T update(String response, T instance) {
        try {
            return MAPPER.readerForUpdating(instance).readValue(response);
        } catch (IOException e) {
            throw new GitlabException("Response cannot be parsed", e);
        }
    }


    public static String request(Config config, String tailUrl, Method method, Body data) {
        Request request;
        try {
            request = new Request.Builder()
                    .url(config.getAPIUrl(tailUrl))
                    .method(method.name(),
                            data == null ?
                                    null :
                                    RequestBody.create(MAPPER.writeValueAsString(data.getMap()), JSON))
                    .addHeader(
                            config.authMethod().headerName(), // header name
                            String.format(config.authMethod().headerFormat(), config.token()) // header token
                    )
                    .build();
            System.out.println("URL:" + request.url());
            System.out.println("Body:" + request.body());
        } catch (JsonProcessingException e) {
            throw new GitlabException("Cannot serialize", e);
        }
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            if (response.isSuccessful()) {
                return body;
            }
            throw new GitlabException(body);
        } catch (IOException e) {
            throw new GitlabException(e);
        }
    }

}
