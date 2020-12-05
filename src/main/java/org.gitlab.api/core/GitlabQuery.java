package org.gitlab.api.core;


import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Models the Query
 * aspect of a URL
 */
public abstract class GitlabQuery<C extends GitlabComponent> implements GitlabComponent {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final Class<C[]> type;
    /**
     * The type of params is:
     * Tuple<name, Tuple<value, URLEncoder.encode(value, "UTF-8")>>
     */
    private final List<Tuple<String, Tuple<String, String>>> params = new ArrayList<Tuple<String, Tuple<String, String>>>();
    private Config config;

    public GitlabQuery(Config config, Class<C[]> type) {
        this.config = config;
        this.type = type;
    }

    public abstract String getUrlPrefix();

    public abstract GitlabQuery<C> withPagination(Pagination pagination);

    @Override
    public GitlabQuery<C> withConfig(Config config) {
        this.config = config;
        return this;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public List<C> query() {
        return GitlabHttpClient.getList(config, getEntireUrl(), type);
    }

    public String getEntireUrl() {
        return getUrlPrefix() + toString();
    }

    public Class<C[]> getType() {
        return type;
    }

    /**
     * Appends a parameter to the query
     *
     * @param name  Parameter name
     * @param value Parameter value
     * @return this
     */
    protected GitlabQuery<C> appendString(String name, String value) {
        if (value != null) {
            try {
                params.add(new Tuple<>(name, new Tuple<>(value, URLEncoder.encode(value, "UTF-8"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    protected GitlabQuery<C> appendInt(String name, int value) {
        return appendString(name, String.valueOf(value));
    }

    protected GitlabQuery<C> appendBoolean(String name, boolean value) {
        return appendString(name, String.valueOf(value));
    }

    protected GitlabQuery<C> appendStrings(String name, List<String> strings) {
        return appendString(name, String.join(",", strings));
    }

    protected GitlabQuery<C> appendInts(String name, List<Integer> ints) {
        return appendString(name, ints.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    protected GitlabQuery<C> appendDate(String name, LocalDate date) {
        if (date != null) {
            return appendString(name, date.format(DATE_FORMATTER));
        }
        return this;
    }

    protected GitlabQuery<C> appendDateTime(String name, LocalDateTime dateTime) {
        if (dateTime != null) {
            ZonedDateTime time = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
            return appendString(name, DATE_TIME_FORMATTER.format(time));
        }
        return this;
    }

    protected GitlabQuery<C> appendPagination(Pagination pagination) {
        appendInt("per_page", pagination.getPageSize());
        appendInt("page", pagination.getPageNumber());
        return this;
    }

    /**
     * Returns a Query suitable for appending
     * to a URI
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (final Tuple<String, Tuple<String, String>> param : params) {
            if (builder.length() == 0) {
                builder.append('?');
            } else {
                builder.append('&');
            }
            builder.append(param._1);
            builder.append('=');
            builder.append(param._2._2);
        }

        return builder.toString();
    }

    private static class Tuple<T1, T2> {
        T1 _1;
        T2 _2;

        public Tuple(T1 _1, T2 _2) {
            this._1 = _1;
            this._2 = _2;
        }
    }


}
