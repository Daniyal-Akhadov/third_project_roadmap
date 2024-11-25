package org.example.third_project.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(value = {
        "/currencies",
        "/currency/*",
        "/exchangeRate/*",
        "/exchangeRates",
        "/exchange"
})
public class ResponseContentTypeSetupFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType("application/json");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
