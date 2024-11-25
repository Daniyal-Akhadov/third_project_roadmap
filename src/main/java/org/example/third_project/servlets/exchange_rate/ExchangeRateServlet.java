package org.example.third_project.servlets.exchange_rate;

import org.example.third_project.dto.ExchangeRateResponseDTO;
import org.example.third_project.exceptions.check.IncorrectParamsException;
import org.example.third_project.exceptions.uncheck.CurrencyNotFoundException;
import org.example.third_project.exceptions.uncheck.DataBaseNotAvailableException;
import org.example.third_project.services.ExchangeRatesService;
import org.example.third_project.utils.ResponseUtils;
import org.example.third_project.utils.CurrencyExtractor;
import org.example.third_project.validation.ClientRequestValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/exchangeRate/*")
public final class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();

        switch (method) {
            case "GET" -> super.service(request, response);
            case "PATCH" -> this.doPatch(request, response);
            default -> ResponseUtils.send(response, SC_INTERNAL_SERVER_ERROR,
                    new DataBaseNotAvailableException("%s: not available method"
                            .formatted(method)));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ClientCodes clientCodes = getClientCodes(request);
            validation(clientCodes.base, clientCodes.target);

            Optional<ExchangeRateResponseDTO> exchangeRateResponseDTO = exchangeRatesService.findByCode(clientCodes.base(), clientCodes.target());

            if (exchangeRateResponseDTO.isPresent()) {
                ResponseUtils.send(response, HttpServletResponse.SC_OK, exchangeRateResponseDTO.get());
            } else {
                ResponseUtils.send(response, HttpServletResponse.SC_NOT_FOUND,
                        new CurrencyNotFoundException("One or two currencies with the same currency code are not found"));
            }
        } catch (IncorrectParamsException exception) {
            ResponseUtils.send(response, HttpServletResponse.SC_BAD_REQUEST, exception);
        }
    }

    private void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ClientCodes result = getClientCodes(request);
            String rate = getRate(request);

            validation(result.base(), result.target(), rate);

            Optional<ExchangeRateResponseDTO> exchangeRateResponseDTO =
                    exchangeRatesService.findByCode(result.base(), result.target());

            if (exchangeRateResponseDTO.isPresent()) {
                exchangeRateResponseDTO.get().setRate(Double.parseDouble(rate));
                exchangeRatesService.update(exchangeRateResponseDTO.get());
                ResponseUtils.send(response, HttpServletResponse.SC_OK, exchangeRateResponseDTO.get());
            } else {
                ResponseUtils.send(response, HttpServletResponse.SC_NOT_FOUND,
                        new CurrencyNotFoundException("One or two currencies with the same currency code are not found"));
            }
        } catch (IncorrectParamsException exception) {
            ResponseUtils.send(response, HttpServletResponse.SC_BAD_REQUEST, exception);
        }
    }

    private static ClientCodes getClientCodes(HttpServletRequest request) throws IncorrectParamsException {
        String[] codes = CurrencyExtractor.extract(request.getPathInfo());
        String base = codes[0];
        String target = codes[1];
        return new ClientCodes(base, target);
    }

    private record ClientCodes(String base, String target) {

    }
    private void validation(String base, String target, String rate) throws IncorrectParamsException {
        validation(base, target);
        ClientRequestValidator.validate( rate)
                .notNull()
                .notEmpty()
                .limitLength(10)
                .isPositiveNumber()
                .end();
    }

    private void validation(String base, String target) throws IncorrectParamsException {
        ClientRequestValidator.validate(base, target)
                .notNull()
                .notEmpty()
                .limitLength(10)
                .isCurrencyCode()
                .end();
    }

    private static String getRate(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .filter((line) -> line.startsWith("rate"))
                .map((line) -> line.split("=")[1])
                .collect(Collectors.joining());
    }
}


