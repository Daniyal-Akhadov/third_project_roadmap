package org.example.third_project.utils;

import org.example.third_project.dto.CurrencyRequestDTO;
import org.example.third_project.dto.CurrencyResponseDTO;
import org.example.third_project.dto.ExchangeDTO;
import org.example.third_project.dto.ExchangeRateResponseDTO;
import org.example.third_project.model.Currency;
import org.example.third_project.model.ExchangeRate;

public class MappingUtils {
    public static CurrencyResponseDTO toCurrencyResponseDTO(Currency currency) {
        return new CurrencyResponseDTO(
                currency.getId(),
                currency.getCode(),
                currency.getName(),
                currency.getSign());
    }

    public static Currency toModel(CurrencyRequestDTO currencyRequestDTO) {
        Currency currency = new Currency();

        currency.setCode(currencyRequestDTO.getCode());
        currency.setName(currencyRequestDTO.getName());
        currency.setSign(currencyRequestDTO.getSign());

        return currency;
    }

    public static Currency toModel(CurrencyResponseDTO currencyResponseDTO) {
        Currency currency = new Currency();

        currency.setId(currencyResponseDTO.getId());
        currency.setCode(currencyResponseDTO.getCode());
        currency.setName(currencyResponseDTO.getName());
        currency.setSign(currencyResponseDTO.getSign());

        return currency;
    }


    public static ExchangeRateResponseDTO toExchangeRateResponseDTO(ExchangeRate exchangeRate) {
        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
                toCurrencyResponseDTO(exchangeRate.getBaseCurrency()),
                toCurrencyResponseDTO(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate());
    }

    public static ExchangeRate toModel(ExchangeRateResponseDTO exchangeRateResponseDTO) {
        return new ExchangeRate(
                exchangeRateResponseDTO.getId(),
                toModel(exchangeRateResponseDTO.getBaseCurrency()),
                toModel(exchangeRateResponseDTO.getTargetCurrency()),
                exchangeRateResponseDTO.getRate()
        );
    }

    public static ExchangeDTO toExchangeResponseDTO(Currency fromCurrency, Currency toCurrency, double rate, double amount, double convertedAmount) {
        return new ExchangeDTO(fromCurrency, toCurrency, rate, amount, convertedAmount);
    }
}