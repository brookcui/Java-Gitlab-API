package org.gitlab.api.http;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Body {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Map<String, Object> map = new HashMap<>();

    public Body putInt(String key, int value) {
        map.put(key, value);
        return this;
    }

    public Body putDate(String key, LocalDate value) {
        map.put(key, value == null ? null : value.format(FORMATTER));
        return this;
    }

    public Body putString(String key, String value) {
        map.put(key, value);
        return this;
    }

    public Body putBoolean(String key, boolean value) {
        map.put(key, value);
        return this;
    }

    public Body putIntArray(String key, int[] value) {
        map.put(key, value);
        return this;
    }

    public Body putIntArray(String key, List<Integer> value) {
        map.put(key, value.stream().mapToInt(i -> i).toArray());
        return this;
    }

    public Body putStringArray(String key, String[] value) {
        map.put(key, value);
        return this;
    }

    public Body putStringArray(String key, List<String> value) {
        map.put(key, value.toArray(new String[0]));
        return this;
    }

    public Map<String, Object> getMap() {
        return Collections.unmodifiableMap(map);
    }


    @Override
    public String toString() {
        return map.toString();
    }

}
