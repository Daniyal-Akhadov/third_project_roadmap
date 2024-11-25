package org.example.third_project.services;

import lombok.Getter;
import org.example.third_project.dao.ExchangeRatesDAO;
import org.example.third_project.dto.CurrencyResponseDTO;
import org.example.third_project.dto.ExchangeRateRequestDTO;
import org.example.third_project.dto.ExchangeRateResponseDTO;
import org.example.third_project.model.Currency;
import org.example.third_project.model.ExchangeRate;
import org.example.third_project.utils.MappingUtils;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ExchangeRatesService {
    @Getter
    private static final ExchangeRatesService Instance = new ExchangeRatesService();

    private final ExchangeRatesDAO exchangeRatesDAO = ExchangeRatesDAO.getInstance();

    private ExchangeRatesService() {

    }

    public List<ExchangeRateResponseDTO> findAll() {
        List<ExchangeRate> all = exchangeRatesDAO.findAll();
        return all.stream()
                .map(MappingUtils::toExchangeRateResponseDTO)
                .collect(toList());
    }

    public void insert(ExchangeRateRequestDTO exchangeRateResponseDTO) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(MappingUtils.toModel(exchangeRateResponseDTO.getBaseCurrency()));
        exchangeRate.setTargetCurrency(MappingUtils.toModel(exchangeRateResponseDTO.getTargetCurrency()));
        exchangeRate.setRate(exchangeRateResponseDTO.getRate());
        exchangeRatesDAO.insert(exchangeRate);
    }

    public Optional<ExchangeRateResponseDTO> findBy(CurrencyResponseDTO currencyBaseResponseDTO, CurrencyResponseDTO currencyTargetResponseDTO) {
        Currency base = MappingUtils.toModel(currencyBaseResponseDTO);
        Currency target = MappingUtils.toModel(currencyTargetResponseDTO);

        Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.find(base, target);
        return exchangeRate
                .map(MappingUtils::toExchangeRateResponseDTO);
    }

    public Optional<ExchangeRateResponseDTO> findByCode(String base, String target) {
        return exchangeRatesDAO.findByCode(base, target)
                .map(MappingUtils::toExchangeRateResponseDTO);
    }

    public void update(ExchangeRateResponseDTO exchangeRateResponseDTO) {
        exchangeRatesDAO.update(MappingUtils.toModel(exchangeRateResponseDTO));
    }
}
