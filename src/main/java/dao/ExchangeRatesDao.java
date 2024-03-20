package dao;

import entity.ExchangeRate;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRatesDao implements Dao<ExchangeRate, String> {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final String FIND_ALL = "SELECT * FROM exchange_rates";
    public static final String ADD_NEW_EXCHANGE_RATE = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?);";
    private static final String FIND_BY_CODE_PAIR = """
            SELECT er.id, er.base_currency_id, er.target_currency_id, er.rate
            FROM exchange_rates er
            JOIN currencies base_curr ON er.base_currency_id = base_curr.id
            JOIN currencies target_curr ON er.target_currency_id = target_curr.id
            WHERE base_curr.code = ? AND target_curr.code = ?;
            """;

    private ExchangeRatesDao() {
    }

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        }
    }

    @Override
    public Optional<ExchangeRate> findByCode(String codePair) throws SQLException {
        String baseCurrency = codePair.substring(0, 3);
        String targetCurrency = codePair.substring(3);
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_PAIR);
            preparedStatement.setString(1, baseCurrency);
            preparedStatement.setString(1, targetCurrency);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildExchangeRate(resultSet));
            }
            return Optional.empty();
        }
    }

    @Override
    public void add(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_EXCHANGE_RATE);
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();
        }
    }

    public void update(ExchangeRate exchangeRate) {
        //todo
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