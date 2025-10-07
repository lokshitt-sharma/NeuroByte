package com.coding.nuerobyte.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Util {

	private static final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

	private Util() {
	}

	/**
	 * Convert object to JSON string.
	 */
	public static String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to convert object to JSON", e);
		}
	}

	/**
	 * Convert JSON string to object of specified class.
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to convert JSON to object", e);
		}
	}

	/**
	 * Pretty print an object as JSON string.
	 */
	public static String toPrettyJson(Object obj) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to convert object to pretty JSON", e);
		}
	}

	/**
	 * Get the ObjectMapper instance for custom configurations.
	 */
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

}
