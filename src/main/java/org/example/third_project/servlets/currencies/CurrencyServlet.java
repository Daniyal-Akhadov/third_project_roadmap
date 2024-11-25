package org.example.third_project.servlets.currencies;

import org.example.third_project.dto.CurrencyResponseDTO;
import org.example.third_project.exceptions.uncheck.CurrencyNotFoundException;
import org.example.third_project.exceptions.uncheck.DataBaseNotAvailableException;
import org.example.third_project.services.CurrenciesService;
import org.example.third_project.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrenciesService currenciesService = CurrenciesService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();

        switch (method) {
            case "GET" -> super.service(request, response);
            default -> ResponseUtils.send(response, SC_INTERNAL_SERVER_ERROR,
                    new DataBaseNotAvailableException("%s: not available method"
                            .formatted(method)));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String code = pathInfo.substring(1);
        Optional<CurrencyResponseDTO> currency = currenciesService.findByCode(code);

        if (currency.isPresent()) {
            ResponseUtils.send(response, HttpServletResponse.SC_OK, currency.get());
        } else {
            ResponseUtils.send(response, HttpServletResponse.SC_NOT_FOUND, new CurrencyNotFoundException());
        }
    }
}
