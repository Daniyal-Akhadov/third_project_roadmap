package org.example.third_project.services;

import lombok.Getter;
import org.example.third_project.dao.CurrenciesDAO;
import org.example.third_project.dao.ExchangeRatesDAO;
import org.example.third_project.dto.ExchangeDTO;
import org.example.third_project.dto.ExchangeRateResponseDTO;
import org.example.third_project.exceptions.uncheck.CurrencyNotFoundException;
import org.example.third_project.model.Currency;
import org.example.third_project.exceptions.uncheck.ExchangeRateUnavailableException;
import org.example.third_project.utils.MappingUtils;

import java.util.Optional;

public class ExchangeService {
    @Getter
    private static final ExchangeService Instance = new ExchangeService();

    private final CurrenciesDAO currenciesDAO = CurrenciesDAO.getInstance();
    private final ExchangeRatesDAO exchangeRatesDAO = ExchangeRatesDAO.getInstance();
    private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();

    public ExchangeDTO calculate(String baseCode, String targetCode, Double amount) throws ExchangeRateUnavailableException {
        Currency fromCurrency = findCurrency(baseCode);
        Currency toCurrency = findCurrency(targetCode);

        double rate = getExchangeRate(fromCurrency, toCurrency, amount);
        return MappingUtils.toExchangeResponseDTO(fromCurrency, toCurrency, rate, amount, rate * amount);
    }

    private Currency findCurrency(String code) {
        return currenciesDAO.findByCode(code)
                .orElseThrow(CurrencyNotFoundException::new);
    }

    private Double getExchangeRate(Currency fromCurrency, Currency toCurrency, double amount) throws ExchangeRateUnavailableException {
        return getExchangeRateResponse(fromCurrency, toCurrency)
                .or(() -> getReverseExchangeRate(fromCurrency, toCurrency, amount))
                .or(() -> getUSDCrossRate(fromCurrency, toCurrency))
                .orElseThrow(ExchangeRateUnavailableException::new);
    }

    private Optional<Double> getExchangeRateResponse(Currency fromCurrency, Currency toCurrency) {
        return exchangeRatesService.findByCode(fromCurrency.getCode(), toCurrency.getCode())
                .map(ExchangeRateResponseDTO::getRate);
    }

    private Optional<Double> getReverseExchangeRate(Currency fromCurrency, Currency toCurrency, double amount) {
        return exchangeRatesDAO.find(toCurrency, fromCurrency)
                .map(rate -> amount / rate.getRate());
    }

    private Optional<Double> getUSDCrossRate(Currency fromCurrency, Currency toCurrency) {
        return exchangeRatesDAO.findUSDCrossRate(fromCurrency.getCode(), toCurrency.getCode());
    }
}
