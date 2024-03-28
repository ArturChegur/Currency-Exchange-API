package dao;

import dto.RequestCurrencyDto;
import entity.Currency;
import exceptions.DataExistsException;
import exceptions.DatabaseException;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao implements Dao<Currency, RequestCurrencyDto> {
    private static final CurrenciesDao INSTANCE = new CurrenciesDao();
    private static final String FIND_ALL = "SELECT * FROM currencies";
    private static final String FIND_BY_CODE = "SELECT * FROM currencies WHERE code = ?";
    private static final String FIND_BY_ID = "SELECT * FROM currencies WHERE id = ?";
    private static final String ADD_NEW_CURRENCY = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";

    private CurrenciesDao() {
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public Optional<Currency> findByCode(RequestCurrencyDto request) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE);
            preparedStatement.setObject(1, request.getCode().toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildCurrency(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public void add(RequestCurrencyDto request) {
        if (exists(request)) {
            throw new DataExistsException("Currency already exists");
        } else {
            try (Connection connection = ConnectionManager.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_CURRENCY);
                preparedStatement.setString(1, request.getCode().toUpperCase());
                preparedStatement.setString(2, request.getName());
                preparedStatement.setString(3, request.getSign());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseException("Database is unavailable");
            }
        }
    }

    private boolean exists(RequestCurrencyDto request) {
        return findByCode(request).isPresent();
    }

    public Optional<Currency> findCurrencyById(Integer id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildCurrency(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
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