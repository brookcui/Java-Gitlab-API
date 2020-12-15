package org.gitlab.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is a static utility class to handle date and time format in Gitlab API.
 */
class DateUtil {
    /**
     * The date formatter specifically for the Gitlab API
     * e.g. 2020-12-14
     */
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * The time formatter (zoned) specifically for the Gitlab API
     * e.g. 2020-12-14T01:34:24.852Z
     */
    static final DateTimeFormatter DATE_TIME_ZONED_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    /**
     * The time formatter (offset) specifically for the Gitlab API
     * e.g. 2020-12-02T04:06:57.000+00:00
     */
    static final DateTimeFormatter DATE_TIME_OFFSET_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx");

    /**
     * This class is not instantiatable
     */
    private DateUtil() {
    }

    /**
     * The class to serialize a {@link ZonedDateTime} to yyyy-MM-dd'T'HH:mm:ss'Z' format for Jackson
     */
    static class ZonedSerializer extends JsonSerializer<ZonedDateTime> {
        @Override
        public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(DATE_TIME_ZONED_FORMATTER.format(zonedDateTime));
        }
    }

    /**
     * The class to deserialize a String in yyyy-MM-dd'T'HH:mm:ss'Z' format to a {@link ZonedDateTime} for Jackson
     */
    static class ZonedDeserializer extends JsonDeserializer<ZonedDateTime> {
        @Override
        public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return ZonedDateTime.parse(jsonParser.getText());
        }
    }

    /**
     * The class to serialize a {@link ZonedDateTime} to yyyy-MM-dd'T'HH:mm:ss.SSSxxx format for Jackson
     */
    static class OffsetSerializer extends JsonSerializer<ZonedDateTime> {
        @Override
        public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(DATE_TIME_OFFSET_FORMATTER.format(zonedDateTime));
        }
    }

    /**
     * The class to deserialize a String in yyyy-MM-dd'T'HH:mm:ss.SSSxxx format to a {@link ZonedDateTime} for Jackson
     */
    static class OffsetDeserializer extends JsonDeserializer<ZonedDateTime> {
        @Override
        public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return ZonedDateTime.parse(jsonParser.getText());
        }
    }
}
