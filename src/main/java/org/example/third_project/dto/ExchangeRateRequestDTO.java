package org.example.third_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ExchangeRateRequestDTO implements Serializable {
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private double rate;
}
