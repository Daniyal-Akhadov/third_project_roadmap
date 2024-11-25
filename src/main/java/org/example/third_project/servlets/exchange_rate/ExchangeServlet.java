package org.example.third_project.servlets.exchange_rate;

import org.example.third_project.dto.ExchangeDTO;
import org.example.third_project.exceptions.check.IncorrectParamsException;
import org.example.third_project.exceptions.uncheck.CurrencyNotFoundException;
import org.example.third_project.exceptions.uncheck.DataBaseNotAvailableException;
import org.example.third_project.exceptions.uncheck.ExchangeRateUnavailableException;
import org.example.third_project.services.ExchangeService;
import org.example.third_project.utils.ResponseUtils;
import org.example.third_project.validation.ClientRequestValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("/exchange")
public final class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeService.getInstance();

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
        try {
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            String amount = request.getParameter("amount");

            ClientRequestValidator.validate(from, to, amount)
                    .notNull()
                    .notEmpty()
                    .limitLength(10)
                    .end();

            ClientRequestValidator.validate(amount)
                    .isNumber()
                    .end();

            ClientRequestValidator.validate(from, to)
                    .isStringEquals(false)
                    .end();

            ExchangeDTO exchangeDTO = exchangeService.calculate(from, to, Double.parseDouble(amount));
            ResponseUtils.send(response, SC_OK, exchangeDTO);
        } catch (IncorrectParamsException exception) {
            ResponseUtils.send(response, SC_BAD_REQUEST, exception);
        } catch (CurrencyNotFoundException | ExchangeRateUnavailableException exception) {
            ResponseUtils.send(response, SC_NOT_FOUND, exception);
        }
    }
}

