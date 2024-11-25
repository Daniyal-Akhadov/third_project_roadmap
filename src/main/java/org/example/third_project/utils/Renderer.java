package org.example.third_project.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Renderer {
    @Getter
    private static final Renderer instance = new Renderer();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> void print(HttpServletResponse response, T message) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, message);
        }
    }
}
