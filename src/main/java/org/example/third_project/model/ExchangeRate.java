package org.example.third_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public final class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
}
