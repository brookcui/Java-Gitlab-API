package org.gitlab.api;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * An abstract class to query list of {@link GitlabComponent} based on some query conditions (query parameters)
 *
 * @param <T> the expected {@link GitlabComponent} as the query result
 */
abstract class GitlabQuery<T extends GitlabComponent> {
    /**
     * The date formatter specifically for the Gitlab API
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateUtil.DATE_FORMATTER;
    /**
     * The time formatter specifically for the Gitlab API
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateUtil.DATE_TIME_ZONED_FORMATTER;
    /**
     * The type representing a array of the given {@link GitlabComponent}
     */

    private final HttpClient httpClient;
    private final Class<T[]> type;
    /**
     * The type of params is:
     * Tuple<name, Pair<value, URLEncoder.encode(value, "UTF-8")>>
     */
    private final List<Pair<String, Pair<String, String>>> params = new ArrayList<Pair<String, Pair<String, String>>>();

    /**
     * Construct the query by the Gitlab httpClienturation and the expected type for the query response
     *
     * @param httpClient - the {@link HttpClient} to be used
     * @param type       - the expected array type for the query response
     */
    GitlabQuery(HttpClient httpClient, Class<T[]> type) {
        this.httpClient = httpClient;
        this.type = type;
    }

    /**
     * Get the tail url of the request
     *
     * @return the tail url of the request, e.g. /projects
     */
    abstract String getTailUrl();

    /**
     * For a component to bind with the parent component after it is parsed
     *
     * @param component component to be bind
     */
    abstract void bind(T component);

    /**
     * Add pagination on top of the query
     *
     * @param pagination pagination object that defines page number and size
     * @return this {@link GitlabQuery} with the given pagination object
     */
    public abstract GitlabQuery<T> withPagination(Pagination pagination);


    /**
     * Issue a HTTP request to perform the query
     *
     * @return a list of component retrieved from the query
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<T> query() {
        List<T> components = httpClient.getList(getEntireUrl(), type);
        components.forEach(this::bind);
        return components;
    }

    /**
     * Get the entire url of the query
     *
     * @return entire url, e.g. /projects?owned=true
     */
    String getEntireUrl() {
        return getTailUrl() + toString();
    }

    protected Class<T[]> getType() {
        return type;
    }

    /**
     * Appends a parameter to the query
     *
     * @param name  Parameter name
     * @param value Parameter value
     * @return this
     */
    protected GitlabQuery<T> appendString(String name, String value) {
        if (value != null) {
            try {
                params.add(new Pair<>(name, new Pair<>(value, URLEncoder.encode(value, "UTF-8"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * add a integer type parameter to the query
     *
     * @param name  name of the parameter
     * @param value value of the parameter
     * @return GitlabQuery with the new parameter added
     */
    protected GitlabQuery<T> appendInt(String name, int value) {
        return appendString(name, String.valueOf(value));
    }

    /**
     * add a boolean type parameter to the query
     *
     * @param name  name of the parameter
     * @param value value of the parameter
     * @return GitlabQuery with the new parameter added
     */
    protected GitlabQuery<T> appendBoolean(String name, boolean value) {
        return appendString(name, String.valueOf(value));
    }

    /**
     * add a list of strings type parameter to the query
     *
     * @param name    name of the parameter
     * @param strings value of the parameter
     * @return GitlabQuery with the new parameter added
     */
    protected GitlabQuery<T> appendStrings(String name, List<String> strings) {
        return appendString(name, String.join(",", strings));
    }

    /**
     * add a list of integers type parameter to the query
     *
     * @param name name of the parameter
     * @param ints value of the parameter
     * @return GitlabQuery with the new parameter added
     */
    protected GitlabQuery<T> appendInts(String name, List<Integer> ints) {
        return appendString(name, ints.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    /**
     * add a date type parameter to the query
     *
     * @param name name of the parameter
     * @param date value of the parameter
     * @return GitlabQuery with the new parameter added
     */
    protected GitlabQuery<T> appendDate(String name, LocalDate date) {
        if (date != null) {
            return appendString(name, date.format(DATE_FORMATTER));
        }
        return this;
    }

    /**
     * add a datetime type parameter to the query
     *
     * @param name     name of the parameter
     * @param dateTime value of the parameter
     * @return GitlabQuery with the new parameter added
     */
    protected GitlabQuery<T> appendDateTime(String name, ZonedDateTime dateTime) {
        if (dateTime != null) {
            return appendString(name, DATE_TIME_FORMATTER.format(dateTime));
        }
        return this;
    }

    /**
     * add a pagination to the query
     *
     * @param pagination name of the parameter
     * @return GitlabQuery with the new pagination added
     */
    protected GitlabQuery<T> appendPagination(Pagination pagination) {
        appendInt("per_page", pagination.getPageSize());
        appendInt("page", pagination.getPageNumber());
        return this;
    }

    /**
     * Returns the string in the format URL query parameters
     * e.g. {@code ?key1=value1&key2=value2}
     *
     * @return the string in the format URL query parameters
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Pair<String, Pair<String, String>> param : params) {
            if (builder.length() == 0) {
                builder.append('?');
            } else {
                builder.append('&');
            }
            builder.append(param.first);
            builder.append('=');
            builder.append(param.second.second);
        }

        return builder.toString();
    }

    /**
     * The class representing a pair
     *
     * @param <T1> the class of the first element in the pair
     * @param <T2> the class of the second element in the pair
     */
    private static final class Pair<T1, T2> {
        /**
         * The first element
         */
        T1 first;
        /**
         * The second element
         */
        T2 second;

        /**
         * Create a new pair
         *
         * @param first  the first element in the pair
         * @param second the second element in the pair
         */
        private Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
    }
}
