package org.example.third_project.utils;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public class ResponseUtils {
    private final Renderer renderer = Renderer.getInstance();
    
    public static void send(HttpServletResponse response, int statusCode, Object message) throws IOException {
        response.setStatus(statusCode);
        renderer.print(response, message);
    }
}
