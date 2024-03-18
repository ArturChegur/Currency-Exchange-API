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
    private static final String FIND_ALL = "SELECT * FROM currencies";
    private static final String FIND_BY_CODE = "SELECT * FROM currencies WHERE code = ?";
    private static final String FIND_BY_ID = "SELECT * FROM currencies WHERE id = ?";
    private static final String ADD_NEW_CURRENCY = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";


    private CurrenciesDao() {
    }

    public List<Currency> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency findCurrencyByCode(String currencyCode) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {
            preparedStatement.setObject(1, currencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return buildCurrency(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency findCurrencyById(Integer id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return buildCurrency(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCurrency(Currency currency) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement addNewCurrency = connection.prepareStatement(ADD_NEW_CURRENCY)) {
            addNewCurrency.setString(1, currency.getCode());
            addNewCurrency.setString(2, currency.getFullName());
            addNewCurrency.setString(3, currency.getSign());
            addNewCurrency.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }
}
