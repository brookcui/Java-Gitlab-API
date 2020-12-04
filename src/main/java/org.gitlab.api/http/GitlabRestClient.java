package org.gitlab.api.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import org.gitlab.api.models.AuthComponent;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static org.gitlab.api.http.Method.*;

public class GitlabRestClient {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    private static <T extends AuthComponent> T withConfig(T component, Config config) {
        if (component != null) {
            component.withConfig(config);
        }
        return component;
    }

    /**
     * Get request, update the instance inplace
     *
     * @param tailAPIUrl
     * @param instance
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> T get(Config config, String tailAPIUrl, T instance) throws IOException {
        return withConfig(request(config, GET, tailAPIUrl, null, null, instance), config);

    }

    /**
     * Get request, create a new instance
     *
     * @param tailAPIUrl
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> T get(Config config, String tailAPIUrl, Class<T> type) throws IOException {
        return withConfig(request(config, GET, tailAPIUrl, null, type, null), config);
    }

    /**
     * Get request, create a new instance
     *
     * @param tailAPIUrl
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> List<T> getList(Config config, String tailAPIUrl, Class<T[]> type) throws IOException {
        T[] array = request(config, GET, tailAPIUrl, null, type, null);
        if (array == null) {
            return Collections.emptyList();
        }
        List<T> list = Arrays.asList(array);
        list.forEach(c -> withConfig(c, config));
        return list;
    }

    /**
     * Put request with body, update the instance inplace
     *
     * @param tailAPIUrl
     * @param instance
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> T put(Config config, String tailAPIUrl, Body body, T instance) throws IOException {
        return withConfig(request(config, PUT, tailAPIUrl, body, null, instance), config);
    }

    /**
     * Put request with body, create a new instance
     *
     * @param tailAPIUrl
     * @param body
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> T put(Config config, String tailAPIUrl, Body body, Class<T> type) throws IOException {
        return withConfig(request(config, PUT, tailAPIUrl, body, type, null), config);
    }

    /**
     * Post request with body, update the instance inplace
     *
     * @param tailAPIUrl
     * @param body
     * @param instance
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> T post(Config config, String tailAPIUrl, Body body, T instance) throws IOException {
        return withConfig(request(config, POST, tailAPIUrl, body, null, instance), config);

    }

    /**
     * Post request, create a new instance
     *
     * @param tailAPIUrl
     * @param body
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends AuthComponent> T post(Config config, String tailAPIUrl, Body body, Class<T> type) throws IOException {
        return withConfig(request(config, POST, tailAPIUrl, body, type, null), config);

    }

    public static void delete(Config config, String tailAPIUrl) throws IOException {
        request(config, DELETE, tailAPIUrl, null, null, null);
    }


    private static void submitData(HttpURLConnection connection, Body body) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        System.out.println("Body: " + MAPPER.writeValueAsString(body.getMap()));
        MAPPER.writeValue(connection.getOutputStream(), body.getMap());
    }

    private static HttpURLConnection setupConnection(URL url, Config config, Method method) throws IOException {
        if (config.isIgnoreCertificateErrors()) {
            ignoreCertificateErrors();
        }
//        //TODO using url parameter is not secure and should be deprecated
//        if (apiToken != null && authMethod == AuthMethod.URL_PARAMETER) {
//            String urlWithAuth = url.toString();
//            urlWithAuth = urlWithAuth + (urlWithAuth.indexOf('?') > 0 ? '&' : '?') +
//                    tokenType.getTokenParamName() + "=" + apiToken;
//            url = new URL(urlWithAuth);
//        }

        HttpURLConnection connection = config.getProxy() != null ?
                (HttpURLConnection) url.openConnection(config.getProxy()) : (HttpURLConnection) url.openConnection();
        if (config.getToken() != null) {
            connection.setRequestProperty(config.getAuthMethod().getTokenHeaderName(),
                    String.format(config.getAuthMethod().getTokenHeaderFormat(), config.getToken()));
        }

        connection.setReadTimeout(config.getResponseReadTimeout());
        connection.setConnectTimeout(config.getConnectionTimeout());

        try {
            connection.setRequestMethod(method.name());
        } catch (ProtocolException e) {
            // Hack in case the API uses a non-standard HTTP verb
            try {
                Field methodField = connection.getClass().getDeclaredField("method");
                methodField.setAccessible(true);
                methodField.set(connection, method.name());
            } catch (Exception x) {
                throw new IOException("Failed to set the custom verb", x);
            }
        }
        connection.setRequestProperty("User-Agent", config.getUserAgent());
        connection.setRequestProperty("Accept-Encoding", "gzip");
        return connection;
    }

    private static InputStream wrapStream(HttpURLConnection connection, InputStream inputStream) throws IOException {
        String encoding = connection.getContentEncoding();

        if (encoding == null || inputStream == null) {
            return inputStream;
        } else if (encoding.equals("gzip")) {
            return new GZIPInputStream(inputStream);
        } else {
            throw new UnsupportedOperationException("Unexpected Content-Encoding: " + encoding);
        }
    }

    private static <T> T parse(HttpURLConnection connection, Class<T> type, T instance) throws IOException {
        InputStreamReader reader = null;
        try {
            if (byte[].class == type) {
                return type.cast(IOUtils.toByteArray(wrapStream(connection, connection.getInputStream())));
            }
            reader = new InputStreamReader(wrapStream(connection, connection.getInputStream()), StandardCharsets.UTF_8);
            String json = IOUtils.toString(reader);
            System.out.println("Response: " + json);
            if (type == String.class) {
                return type.cast(json);
            }
            if (type != null && type != Void.class) {
                return MAPPER.readValue(json, type);
            } else if (instance != null) {
                return MAPPER.readerForUpdating(instance).readValue(json);
            } else {
                return null;
            }
        } catch (SSLHandshakeException e) {
            throw new SSLException("You can disable certificate checking by setting ignoreCertificateErrors " +
                    "on GitlabHTTPRequestor.", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static void handleAPIError(IOException e, HttpURLConnection connection) throws IOException {
        System.out.println("Error: " + connection.getResponseMessage());
        System.out.println("Error: " + IOUtils.toString(connection.getErrorStream(), StandardCharsets.UTF_8));
        if (e instanceof FileNotFoundException || // pass through 404 Not Found to allow the caller to handle it intelligently
                e instanceof SocketTimeoutException ||
                e instanceof ConnectException) {
            throw e;
        }

        InputStream es = wrapStream(connection, connection.getErrorStream());
        try {
            String error = null;
            if (es != null) {
                error = IOUtils.toString(es, StandardCharsets.UTF_8);
            }
            //TODO
            //throw new GitlabAPIException(error, connection.getResponseCode(), e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(es);
        }
    }

    private static void ignoreCertificateErrors() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        // Added per https://github.com/timols/java-gitlab-api/issues/44
        HostnameVerifier nullVerifier = (hostname, session) -> true;

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Added per https://github.com/timols/java-gitlab-api/issues/44
            HttpsURLConnection.setDefaultHostnameVerifier(nullVerifier);
        } catch (Exception ignore) {
        }
    }

    private static <T> T request(Config config, Method method, String tailAPIUrl, Body data, Class<T> type, T instance) throws IOException {
        HttpURLConnection connection = null;
        try {
            System.out.println(method.name() + " " + config.getAPIUrl(tailAPIUrl));
            connection = setupConnection(config.getAPIUrl(tailAPIUrl), config, method);
            if (data != null) {
                submitData(connection, data);
            }
            try {
                return parse(connection, type, instance);
            } catch (IOException e) {
                e.printStackTrace();
                handleAPIError(e, connection);
            }

            return null;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
