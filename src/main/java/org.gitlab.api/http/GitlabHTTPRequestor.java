package org.gitlab.api.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import org.gitlab.api.core.AuthMethod;
import org.gitlab.api.models.GitlabComponent;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import static org.gitlab.api.http.Method.*;


/**
 * Gitlab HTTP Requestor
 * Responsible for handling HTTP requests to the Gitlab API
 *
 * @author &#064;timols (Tim O)
 */
public class GitlabHTTPRequestor {

    private static final Pattern PAGE_PATTERN = Pattern.compile("([&|?])page=(\\d+)");

    private final Config config;

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public GitlabHTTPRequestor(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public <T extends GitlabComponent> T withHTTPRequestor(T component) {
        if (component != null) {
            component.withHTTPRequestor(this);
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
    public <T extends GitlabComponent> T get(String tailAPIUrl, T instance) throws IOException {
        return withHTTPRequestor(to(GET, tailAPIUrl, null, null, null, instance));

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
    public <T extends GitlabComponent> T get(String tailAPIUrl, Class<T> type) throws IOException {
        return withHTTPRequestor(to(GET, tailAPIUrl, null, null, type, null));
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
    public <T extends GitlabComponent> List<T> getList(String tailAPIUrl, Class<T[]> type) throws IOException {
        T[] array = to(GET, tailAPIUrl, null, null, type, null);
        if (array == null) {
            return Collections.emptyList();
        }
        List<T> list = Arrays.asList(array);
        list.forEach(this::withHTTPRequestor);
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
    public <T extends GitlabComponent> T put(String tailAPIUrl, Body body, T instance) throws IOException {
        return withHTTPRequestor(to(PUT, tailAPIUrl, body, null, null, instance));
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
    public <T extends GitlabComponent> T put(String tailAPIUrl, Body body, Class<T> type) throws IOException {
        return withHTTPRequestor(to(PUT, tailAPIUrl, body, null, type, null));
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
    public <T extends GitlabComponent> T post(String tailAPIUrl, Body body, T instance) throws IOException {
        return withHTTPRequestor(to(POST, tailAPIUrl, body, null, null, instance));

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
    public <T extends GitlabComponent> T post(String tailAPIUrl, Body body, Class<T> type) throws IOException {
        return withHTTPRequestor(to(POST, tailAPIUrl, body, null, type, null));

    }

    public void delete(String tailAPIUrl) throws IOException {
        to(DELETE, tailAPIUrl, null, null, null, null);
    }


    /**
     * Opens the HTTP(S) connection, submits any data and parses the response.
     * Will throw an error
     *
     * @param <T>        The return type of the method
     * @param tailAPIUrl The url to open a connection to (after the host and namespace)
     * @param type       The type of the response to be deserialized from
     * @param instance   The instance to update from the response
     * @return An object of type T
     * @throws IOException on gitlab api error
     */
    private <T> T to(Method method, String tailAPIUrl, Body data, Map<String, File> attachments, Class<T> type, T instance) throws IOException {
        HttpURLConnection connection = null;
        try {
            System.out.println(method.name() + " " + config.getAPIUrl(tailAPIUrl));
            connection = setupConnection(config.getAPIUrl(tailAPIUrl), method);
            if (attachments != null) {
                submitAttachments(connection, data, attachments);
            } else if (data != null) {
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

    public <T> List<T> getTotal(final String tailUrl, final Class<T[]> type) {
        List<T> results = new ArrayList<>();
        Iterator<T[]> iterator = asIterator(tailUrl, type);

        while (iterator.hasNext()) {
            T[] requests = iterator.next();

            if (requests.length > 0) {
                results.addAll(Arrays.asList(requests));
            }
        }
        return results;
    }

    public <T> Iterator<T> asIterator(final String tailApiUrl, final Class<T> type) {
        return new Iterator<T>() {
            T next;
            URL url;

            {
                try {
                    url = config.getAPIUrl(tailApiUrl);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public boolean hasNext() {
                fetch();
                if (next != null && next.getClass().isArray()) {
                    Object[] arr = (Object[]) next;
                    return arr.length != 0;
                } else {
                    return next != null;
                }
            }

            @Override
            public T next() {
                fetch();
                T record = next;

                if (record == null) {
                    throw new NoSuchElementException();
                }

                next = null;
                return record;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void fetch() {
                if (next != null) {
                    return;
                }

                if (url == null) {
                    return;
                }
                try {
                    HttpURLConnection connection = setupConnection(url, GET);
                    try {
                        next = parse(connection, type, null);
                        assert next != null;
                        findNextUrl();
                    } catch (IOException e) {
                        handleAPIError(e, connection);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            private void findNextUrl() throws MalformedURLException {
                String url = this.url.toString();

                this.url = null;
                /* Increment the page number for the url if a "page" property exists,
                 * otherwise, add the page property and increment it.
                 * The Gitlab API is not a compliant hypermedia REST api, so we use
                 * a naive implementation.
                 */
                Matcher matcher = PAGE_PATTERN.matcher(url);

                if (matcher.find()) {
                    Integer page = Integer.parseInt(matcher.group(2)) + 1;
                    this.url = new URL(matcher.replaceAll(matcher.group(1) + "page=" + page));
                } else {
                    // Since the page query was not present, its safe to assume that we just
                    // currently used the first page, so we can default to page 2
                    this.url = new URL(url + (url.indexOf('?') > 0 ? '&' : '?') + "page=2");
                }
            }
        };
    }

    private void submitAttachments(HttpURLConnection connection, Object data, Map<String, File> attachments) throws IOException {
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        String charset = "UTF-8";
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.
        OutputStream output = connection.getOutputStream();
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)) {
            // TODO
            for (Map.Entry<String, Object> paramEntry : ((Map<String, Object>) MAPPER.convertValue(data, Map.class))
                    .entrySet()) {
                String paramName = paramEntry.getKey();
                String param = MAPPER.writeValueAsString(paramEntry.getValue());
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
                writer.append(CRLF).append(param).append(CRLF).flush();
            }
            for (Map.Entry<String, File> attachMentEntry : attachments.entrySet()) {
                File binaryFile = attachMentEntry.getValue();
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"").append(attachMentEntry.getKey())
                      .append("\"; filename=\"").append(binaryFile.getName()).append("\"").append(CRLF);
                writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(binaryFile.getName()))
                      .append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();
                try (Reader fileReader = new FileReader(binaryFile)) {
                    IOUtils.copy(fileReader, output);
                }
                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
            }
            writer.append("--").append(boundary).append("--").append(CRLF).flush();
        }
    }

    /**
     * Convert object to JSON Stream and write to the connection
     *
     * @param connection
     * @param body
     * @throws IOException
     */
    private void submitData(HttpURLConnection connection, Body body) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        System.out.println("Body: " + MAPPER.writeValueAsString(body.getMap()));
        MAPPER.writeValue(connection.getOutputStream(), body.getMap());
    }


    private HttpURLConnection setupConnection(URL url, Method method) throws IOException {
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

    private <T> T parse(HttpURLConnection connection, Class<T> type, T instance) throws IOException {
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

    private InputStream wrapStream(HttpURLConnection connection, InputStream inputStream) throws IOException {
        String encoding = connection.getContentEncoding();

        if (encoding == null || inputStream == null) {
            return inputStream;
        } else if (encoding.equals("gzip")) {
            return new GZIPInputStream(inputStream);
        } else {
            throw new UnsupportedOperationException("Unexpected Content-Encoding: " + encoding);
        }
    }

    private void handleAPIError(IOException e, HttpURLConnection connection) throws IOException {
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

    private void ignoreCertificateErrors() {
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

    public AuthMethod getAuthMethod() {
        return config.getAuthMethod();
    }
}
