package org.gitlab.api.models;


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
public abstract class GitlabQuery<T extends GitlabComponent> implements GitlabComponent {
    private final Class<T[]> type;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private Config config;

    public abstract String getUrlPrefix();


    public GitlabQuery(Config config, Class<T[]> type) {
        this.config = config;
        this.type = type;
    }

    @Override
    public GitlabQuery<T> withConfig(Config config) {
        this.config = config;
        return this;
    }


    @Override
    public Config getConfig() {
        return config;
    }

    public List<T> query() {
        return GitlabHttpClient.getList(config, getEntireUrl(), type);
    }


    public String getEntireUrl() {
        return getUrlPrefix() + toString();
    }


    public Class<T[]> getType() {
        return type;
    }

    private static class Tuple<T1, T2> {
        T1 _1;
        T2 _2;

        public Tuple(T1 _1, T2 _2) {
            this._1 = _1;
            this._2 = _2;
        }
    }

    /**
     * The type of params is:
     * Tuple<name, Tuple<value, URLEncoder.encode(value, "UTF-8")>>
     */
    private final List<Tuple<String, Tuple<String, String>>> params = new ArrayList<Tuple<String, Tuple<String, String>>>();

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
                params.add(new Tuple<>(name, new Tuple<>(value, URLEncoder.encode(value, "UTF-8"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this;
    }


    protected GitlabQuery<T> appendInt(String name, int value) {
        return appendString(name, String.valueOf(value));
    }

    protected GitlabQuery<T> appendBoolean(String name, boolean value) {
        return appendString(name, String.valueOf(value));
    }

    protected GitlabQuery<T> appendStrings(String name, List<String> strings) {
        return appendString(name, String.join(",", strings));
    }

    protected GitlabQuery<T> appendInts(String name, List<Integer> ints) {
        return appendString(name, ints.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    protected GitlabQuery<T> appendDate(String name, LocalDate date) {
        if (date != null) {
            return appendString(name, date.format(DATE_FORMATTER));
        }
        return this;
    }

    protected GitlabQuery<T> appendDateTime(String name, LocalDateTime dateTime) {
        if (dateTime != null) {
            ZonedDateTime time = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
            return appendString(name, DATE_TIME_FORMATTER.format(time));
        }
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


}
