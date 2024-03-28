package dao;

import dto.RequestExchangeRateDto;
import entity.ExchangeRate;
import exceptions.DatabaseException;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements Dao<ExchangeRate, RequestExchangeRateDto> {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final String FIND_ALL = "SELECT * FROM exchange_rates";
    public static final String ADD_NEW_EXCHANGE_RATE = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?);";
    private static final String UPDATE_EXCHANGE_RATE = """
            UPDATE exchange_rates er
            JOIN currencies base_curr ON er.base_currency_id = base_curr.id
            JOIN currencies target_curr ON er.target_currency_id = target_curr.id
            SET er.rate = ?
            WHERE base_curr.code = ?
            AND target_curr.code = ?
            """;
    private static final String FIND_BY_CODE_PAIR = """
            SELECT er.id, er.base_currency_id, er.target_currency_id, er.rate
            FROM exchange_rates er
            INNER JOIN currencies base_curr ON er.base_currency_id = base_curr.id
            INNER JOIN currencies target_curr ON er.target_currency_id = target_curr.id
            WHERE base_curr.code = ? AND target_curr.code = ?;
            """;

    private ExchangeRatesDao() {
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public Optional<ExchangeRate> findByCode(RequestExchangeRateDto request) {
        String baseCurrency = request.getBaseCurrency();
        String targetCurrency = request.getTargetCurrency();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_PAIR);
            preparedStatement.setString(1, baseCurrency.toUpperCase());
            preparedStatement.setString(2, targetCurrency.toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildExchangeRate(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public void add(RequestExchangeRateDto request) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_EXCHANGE_RATE);
            preparedStatement.setInt(1, Integer.parseInt(request.getBaseCurrency().toUpperCase()));
            preparedStatement.setInt(2, Integer.parseInt(request.getTargetCurrency().toUpperCase()));
            preparedStatement.setBigDecimal(3, request.getRate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    public void update(RequestExchangeRateDto request) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE)) {
            preparedStatement.setBigDecimal(1, request.getRate());
            preparedStatement.setString(2, request.getBaseCurrency().toUpperCase());
            preparedStatement.setString(3, request.getTargetCurrency().toUpperCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
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