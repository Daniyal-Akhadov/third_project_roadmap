package org.example.third_project.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(value = {
        "/currencies",
        "/currency/*",
        "/exchangeRate/*",
        "/exchangeRates",
        "/exchange"
})
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
