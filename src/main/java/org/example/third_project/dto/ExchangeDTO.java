package org.example.third_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.third_project.model.Currency;


@Setter
@Getter
@ToString
@AllArgsConstructor
public final class ExchangeDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeDTO() {
    }
}
