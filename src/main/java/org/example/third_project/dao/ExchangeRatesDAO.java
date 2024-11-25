package org.example.third_project.dao;

import lombok.Getter;
import lombok.SneakyThrows;
import org.example.third_project.DataBaseConnection;
import org.example.third_project.model.Currency;
import org.example.third_project.model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ExchangeRatesDAO {

    @Getter
    private static final ExchangeRatesDAO Instance = new ExchangeRatesDAO();

    private static final String FIND_ALL = """
            select ER.id as id,
                   BC.id as baseCurrencyId,
                   BC.code as baseCurrencyCode,
                   BC.fullName as baseCurrencyFullName,
                   BC.sign as baseCurrencySign,
                   TC.id as targetCurrencyId,
                   TC.code as targetCurrencyCode,
                   TC.fullName as targetCurrencyFullName,
                   TC.sign as targetCurrencySign,
                   ER.rate as rate
            from ExchangeRates ER
            left join Currencies BC on BC.id = ER.baseCurrencyId
            left join Currencies TC on TC.id = ER.targetCurrencyId;
            """;

    private static final String FIND_BY_ID = """
            select ER.id as id,
                   BC.id as baseCurrencyId,
                   BC.code as baseCurrencyCode,
                   BC.fullName as baseCurrencyFullName,
                   BC.sign as baseCurrencySign,
                   TC.id as targetCurrencyId,
                   TC.code as targetCurrencyCode,
                   TC.fullName as targetCurrencyFullName,
                   TC.sign as targetCurrencySign,
                   ER.rate as rate
            from ExchangeRates ER
            left join Currencies BC on BC.id = ER.baseCurrencyId
            left join Currencies TC on TC.id = ER.targetCurrencyId
            where baseCurrencyId = ? and targetCurrencyId = ?
            """;

    private static final String FIND_BY_CODE = """
            select ER.id as id,
                   BC.id as baseCurrencyId,
                   BC.code as baseCurrencyCode,
                   BC.fullName as baseCurrencyFullName,
                   BC.sign as baseCurrencySign,
                   TC.id as targetCurrencyId,
                   TC.code as targetCurrencyCode,
                   TC.fullName as targetCurrencyFullName,
                   TC.sign as targetCurrencySign,
                   ER.rate as rate
            from ExchangeRates ER
            left join Currencies BC on BC.id = ER.baseCurrencyId
            left join Currencies TC on TC.id = ER.targetCurrencyId
            where BC.code = ? and TC.code = ?
            """;

    public static final String INSERT = """
            insert into ExchangeRates(baseCurrencyId, targetCurrencyId, rate) values (?, ?, ?)
            """;

    public static final String FIND_USD_CROSS_RATE = """
            SELECT *
            FROM ExchangeRates
            WHERE baseCurrencyId = (SELECT id FROM Currencies WHERE code = 'USD')
            AND targetCurrencyId IN (SELECT id FROM Currencies WHERE code IN (?, ?));
            """;

    public static final String UPDATE_RATE = """
            update ExchangeRates set rate = ? where id = ?
            """;

    private ExchangeRatesDAO() {
    }

    @SneakyThrows
    public List<ExchangeRate> findAll() {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                ExchangeRate exchangeRate = buildExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }

            return exchangeRates;
        }
    }

    @SneakyThrows
    public Optional<ExchangeRate> find(Currency base, Currency target) {
        Optional<ExchangeRate> result;

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);

            preparedStatement.setInt(1, base.getId());
            preparedStatement.setInt(2, target.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            ExchangeRate exchangeRate = buildExchangeRate(resultSet);

            result = Optional.of(exchangeRate);
        }

        return result;
    }

    @SneakyThrows
    public void insert(ExchangeRate exchangeRate) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT);) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setDouble(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public void update(ExchangeRate exchangeRate) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RATE);
            preparedStatement.setDouble(1, exchangeRate.getRate());
            preparedStatement.setInt(2, exchangeRate.getId());
            preparedStatement.executeUpdate();
        }
    }

    public Optional<Double> findUSDCrossRate(String baseCode, String targetCode) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_USD_CROSS_RATE);

            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Double> rates = new ArrayList<>();

            while (resultSet.next()) {
                double rate = resultSet.getDouble("rate");
                rates.add(rate);
            }

            if (rates.isEmpty()) {
                return Optional.empty();
            }

            double result = rates.get(0) / rates.get(1);
            return Optional.of(result);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<ExchangeRate> findByCode(String base, String target) {
        Optional<ExchangeRate> result;

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE);

            preparedStatement.setString(1, base);
            preparedStatement.setString(2, target);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            ExchangeRate exchangeRate = buildExchangeRate(resultSet);

            result = Optional.of(exchangeRate);
        } catch (SQLException e) {
            result = Optional.empty();
        }

        return result;
    }

    private static Connection getConnection() {
        return DataBaseConnection.get();
    }

    private static ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(resultSet.getInt("id"));

        exchangeRate.setBaseCurrency(new Currency(
                resultSet.getInt("baseCurrencyId"),
                resultSet.getString("baseCurrencyCode"),
                resultSet.getString("baseCurrencyFullName"),
                resultSet.getString("baseCurrencySign")
        ));

        exchangeRate.setTargetCurrency(new Currency(
                resultSet.getInt("targetCurrencyId"),
                resultSet.getString("targetCurrencyCode"),
                resultSet.getString("targetCurrencyFullName"),
                resultSet.getString("targetCurrencySign")));

        exchangeRate.setRate(resultSet.getDouble("rate"));
        return exchangeRate;
    }
}


