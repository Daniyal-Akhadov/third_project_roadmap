package org.example.third_project.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ExchangeRateResponseDTO implements Serializable {
    private int id;
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private double rate;
}

