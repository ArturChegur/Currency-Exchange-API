package dao;

import entity.Currency;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();
    private static final String FIND_ALL = "SELECT * FROM main.currencies";

    private CurrenciesDao() {
    }

    public List<Currency> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(buildFlight(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency buildFlight(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getObject("id", Integer.class),
                resultSet.getObject("code", String.class),
                resultSet.getObject("fullName", String.class),
                resultSet.getObject("sign", String.class));
    }

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }
}
