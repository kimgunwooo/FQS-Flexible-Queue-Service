package com.f4.fqs.commons.kafka_common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ParsingUtil {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static String makeJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseJsonString(String jsonString, Class<T> clazz) {
        return objectMapper.convertValue(jsonString, clazz);
    }

    public static <T> T readJsonString(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String parseText(String before) {
        try {
            return objectMapper.readTree(before).asText();
        } catch (JsonProcessingException e) {
            System.out.println("ENTER EXCEPTION :: ");
            throw new RuntimeException(e);
        }
    }
}
