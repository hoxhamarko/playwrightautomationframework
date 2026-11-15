package com.framework.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import java.io.IOException;
import java.io.UncheckedIOException;

public class ApiClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final APIRequestContext request;

    public ApiClient(APIRequestContext request) {
        this.request = request;
    }

    public APIResponse get(String path) {
        return request.get(path);
    }

    public APIResponse post(String path, String jsonBody) {
        return request.post(path, json(jsonBody));
    }

    public APIResponse put(String path, String jsonBody) {
        return request.put(path, json(jsonBody));
    }

    public APIResponse delete(String path) {
        return request.delete(path);
    }

    public static JsonNode parse(APIResponse response) {
        try {
            return MAPPER.readTree(response.text());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static RequestOptions json(String body) {
        return RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(body);
    }
}
