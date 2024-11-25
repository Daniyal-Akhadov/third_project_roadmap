package org.example.third_project.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@WebFilter(value = {
        "/currencies",
        "/currency/*",
        "/exchangeRate/*",
        "/exchangeRates",
        "/exchange"
})
public class CharsetFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        setupCharset(request, response, StandardCharsets.UTF_8.name());
        filterChain.doFilter(request, response);
    }

    private static void setupCharset(ServletRequest request, ServletResponse response, String characterEncoding) throws UnsupportedEncodingException {
        request.setCharacterEncoding(characterEncoding);
        response.setCharacterEncoding(characterEncoding);
    }
}
