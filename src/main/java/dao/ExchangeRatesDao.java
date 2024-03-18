package dao;

import entity.ExchangeRate;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ExchangeRatesDao {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final String FIND_ALL = "SELECT * FROM exchange_rates";
    private static final String[] FIND_BY_CODE_PAIR = {
            "SELECT id FROM currencies WHERE code = ?",
            "SELECT id FROM currencies WHERE code = ?",
            "SELECT * FROM exchange_rates WHERE base_currency_id = ? AND target_currency_id = ?"};

    private ExchangeRatesDao() {
    }

    public List<ExchangeRate> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate findByCodePair(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement prepareBaseCurrencyId = connection.prepareStatement(FIND_BY_CODE_PAIR[0]);
             PreparedStatement prepareTargetCurrencyId = connection.prepareStatement(FIND_BY_CODE_PAIR[1])) {

            prepareBaseCurrencyId.setString(1, baseCurrencyCode);
            prepareTargetCurrencyId.setString(1, targetCurrencyCode);
            try (ResultSet resultBaseCurrency = prepareBaseCurrencyId.executeQuery();
                 ResultSet resultTargetCurrency = prepareTargetCurrencyId.executeQuery();
            ) {
                if (resultBaseCurrency.next() && resultTargetCurrency.next()) {
                    int baseCurrencyId = resultBaseCurrency.getInt("id");
                    int targetCurrencyId = resultTargetCurrency.getInt("id");
                    try (PreparedStatement preparedExchangeRate = connection.prepareStatement(FIND_BY_CODE_PAIR[2])) {
                        preparedExchangeRate.setInt(1, baseCurrencyId);
                        preparedExchangeRate.setInt(2, targetCurrencyId);
                        ResultSet resultSet = preparedExchangeRate.executeQuery();
                        if (resultSet.next()) {
                            return buildExchangeRate(resultSet);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(resultSet.getInt(("id")),
                resultSet.getInt("base_currency_id"),
                resultSet.getInt("target_currency_id"),
                resultSet.getBigDecimal("rate"));
    }

    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }
}
