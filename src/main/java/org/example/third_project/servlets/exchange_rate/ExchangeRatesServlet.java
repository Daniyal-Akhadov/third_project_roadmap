package org.example.third_project.servlets.exchange_rate;

import org.example.third_project.dto.CurrencyResponseDTO;
import org.example.third_project.dto.ExchangeRateRequestDTO;
import org.example.third_project.dto.ExchangeRateResponseDTO;
import org.example.third_project.exceptions.uncheck.CurrencyNotFoundException;
import org.example.third_project.exceptions.uncheck.DataBaseNotAvailableException;
import org.example.third_project.exceptions.check.IncorrectParamsException;
import org.example.third_project.exceptions.uncheck.ObjectAlreadyExistsException;
import org.example.third_project.services.CurrenciesService;
import org.example.third_project.services.ExchangeRatesService;
import org.example.third_project.utils.ResponseUtils;
import org.example.third_project.validation.ClientRequestValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final CurrenciesService currenciesService = CurrenciesService.getInstance();
    private final ExchangeRatesService exchangeRateService = ExchangeRatesService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();

        switch (method) {
            case "GET", "POST" -> super.service(request, response);
            default -> ResponseUtils.send(response, SC_INTERNAL_SERVER_ERROR,
                    new DataBaseNotAvailableException("%s: not available method"
                            .formatted(method)));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExchangeRateResponseDTO> exchangeRates = exchangeRateService.findAll();
        ResponseUtils.send(response, SC_OK, exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        try {
            ClientRequestValidator.validate(baseCurrencyCode, targetCurrencyCode, rate)
                    .notNull()
                    .notEmpty()
                    .limitLength(10)
                    .end();

            ClientRequestValidator.validate(rate)
                    .isNumber()
                    .end();

            Optional<CurrencyResponseDTO> baseCode = currenciesService.findByCode(baseCurrencyCode);
            Optional<CurrencyResponseDTO> targetCode = currenciesService.findByCode(targetCurrencyCode);

            if (baseCode.isPresent() && targetCode.isPresent()) {

                Optional<ExchangeRateResponseDTO> exchangeRate = exchangeRateService.findBy(baseCode.get(), targetCode.get());

                if (exchangeRate.isPresent()) {
                    throw new ObjectAlreadyExistsException();
                }

                ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCode.get(), targetCode.get(), Double.parseDouble(rate));
                exchangeRateService.insert(exchangeRateRequestDTO);

                ResponseUtils.send(response, SC_CREATED, exchangeRateRequestDTO);
            } else {
                throw new CurrencyNotFoundException("One or two currencies with the same currency code are not found");
            }
        } catch (IncorrectParamsException exception) {
            ResponseUtils.send(response, SC_BAD_REQUEST, exception);
        } catch (ObjectAlreadyExistsException exception) {
            ResponseUtils.send(response, SC_CONFLICT, exception);
        } catch (CurrencyNotFoundException exception) {
            ResponseUtils.send(response, SC_NOT_FOUND, exception);
        }
    }
}
