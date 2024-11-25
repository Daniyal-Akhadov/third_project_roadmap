package org.example.third_project.dao;

import lombok.Getter;
import lombok.SneakyThrows;
import org.example.third_project.DataBaseConnection;
import org.example.third_project.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDAO {
    public static final String FIND_ALL = "select * from Currencies";
    public static final String FIND_BY_CODE = "select * from Currencies where code = ?";
    public static final String INSERT = "insert into Currencies(code, fullName, sign) values (?, ?, ?)";

    @Getter
    private final static CurrenciesDAO Instance = new CurrenciesDAO();

    private CurrenciesDAO() {
    }

    @SneakyThrows
    public List<Currency> findAll() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                Currency currency = buildCurrency(resultSet);
                currencies.add(currency);
            }

            return currencies;
        }
    }

    @SneakyThrows
    public Optional<Currency> findByCode(final String code) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Currency currency = buildCurrency(resultSet);
                return Optional.of(currency);
            }

            return Optional.empty();
        }
    }

    @SneakyThrows
    public void insert(final Currency currency) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT);) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();
        }
    }

    private static Currency buildCurrency(final ResultSet resultSet) throws SQLException {
        final Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("fullName"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }

    private static Connection getConnection() {
        return DataBaseConnection.get();
    }
}
