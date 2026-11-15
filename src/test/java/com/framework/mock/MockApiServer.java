package com.framework.mock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** Local stand-in for the real API. Runs only on this PC, so no company inspection applies. */
public class MockApiServer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int PORT = 3000;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/users/1", MockApiServer::getUser);
        server.createContext("/posts", MockApiServer::createPost);
        server.start();
        System.out.println("Mock API running at http://localhost:" + PORT + " (Ctrl+C to stop)");
    }

    private static void getUser(HttpExchange exchange) throws IOException {
        String json = "{\"id\":1,\"name\":\"Leanne Graham\",\"username\":\"Bret\",\"email\":\"Sincere@april.biz\"}";
        send(exchange, 200, json);
    }

    private static void createPost(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            send(exchange, 405, "{}");
            return;
        }
        JsonNode incoming = MAPPER.readTree(readAll(exchange.getRequestBody()));
        ObjectNode created = incoming.deepCopy();
        created.put("id", 101);
        send(exchange, 201, MAPPER.writeValueAsString(created));
    }

    private static String readAll(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}