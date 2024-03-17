package dao;

import entity.ExchangeRate;
import util.ConnectionManager;

import javax.swing.event.DocumentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ExchangeRatesDao {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final String FIND_ALL = "SELECT * FROM exchange_rates";
    private static final String FIND_BY_CODE_PAIR = ""; //todo

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

    public ExchangeRate findByCodePair(String baseCurrencyCode, String targetCurrencyCode)  {
        return null;//todo
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
