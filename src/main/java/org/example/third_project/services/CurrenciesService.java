package org.example.third_project.services;

import lombok.Getter;
import org.example.third_project.dao.CurrenciesDAO;
import org.example.third_project.dto.CurrencyResponseDTO;
import org.example.third_project.model.Currency;
import org.example.third_project.utils.MappingUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrenciesService {
    @Getter
    private static final CurrenciesService Instance = new CurrenciesService();

    private final CurrenciesDAO currenciesDAO = CurrenciesDAO.getInstance();

    private CurrenciesService() {
    }

    public List<CurrencyResponseDTO> findAll() {
        return currenciesDAO.findAll().stream()
                .map(MappingUtils::toCurrencyResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyResponseDTO> findByCode(String code) {
        return currenciesDAO.findByCode(code)
                .map(MappingUtils::toCurrencyResponseDTO);
    }

    public void insert(Currency currency) {
        currenciesDAO.insert(currency);
    }
}

