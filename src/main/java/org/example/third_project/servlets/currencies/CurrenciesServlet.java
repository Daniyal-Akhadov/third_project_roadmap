package org.example.third_project.servlets.currencies;

import org.example.third_project.dto.CurrencyRequestDTO;
import org.example.third_project.dto.CurrencyResponseDTO;
import org.example.third_project.exceptions.uncheck.DataBaseNotAvailableException;
import org.example.third_project.exceptions.check.IncorrectParamsException;
import org.example.third_project.exceptions.uncheck.ObjectAlreadyExistsException;
import org.example.third_project.model.Currency;
import org.example.third_project.services.CurrenciesService;
import org.example.third_project.utils.MappingUtils;
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

@WebServlet("/currencies")
public final class CurrenciesServlet extends HttpServlet {
    private final CurrenciesService currenciesService = CurrenciesService.getInstance();

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CurrencyResponseDTO> currencies = currenciesService.findAll();
        ResponseUtils.send(response, SC_OK, currencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String sign = request.getParameter("sign");

            ClientRequestValidator.validate(code, name, sign)
                    .notNull()
                    .notEmpty()
                    .limitLength(15)
                    .end();

            CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code, name, sign);
            Optional<CurrencyResponseDTO> currencyResponseDTO = currenciesService.findByCode(currencyRequestDTO.getCode());

            if (currencyResponseDTO.isPresent()) {
                throw new ObjectAlreadyExistsException("Currency already exists");
            } else {
                Currency currency = MappingUtils.toModel(currencyRequestDTO);
                currenciesService.insert(currency);
                ResponseUtils.send(response, SC_CREATED, currency);
            }
        } catch (IncorrectParamsException exception) {
            ResponseUtils.send(response, SC_BAD_REQUEST, exception);
        } catch (ObjectAlreadyExistsException exception) {
            ResponseUtils.send(response, SC_CONFLICT, exception);
        }
    }
}

