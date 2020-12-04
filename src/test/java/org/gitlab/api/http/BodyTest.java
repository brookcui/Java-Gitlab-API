package org.gitlab.api.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class BodyTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void test() throws JsonProcessingException {
        Body body = new Body().putIntArray("a", new int[]{1, 2, 3})
                              .putString("b", "adadad")
                              .putInt("c", 32323)
                              .putBoolean("d", false)
                              .putStringArray("e", new String[]{"aa", "bb"});
        System.out.println(MAPPER.writeValueAsString(body.getMap()));
    }
}