package service;

import dao.CurrenciesDao;
import dto.CurrencyDto;
import entity.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private CurrencyService() {
    }

    public List<CurrencyDto> findAll() throws SQLException {
        return currenciesDao.findAll().stream()
                .map(currency -> new CurrencyDto(
                        currency.getId(),
                        currency.getFullName(),
                        currency.getCode(),
                        currency.getSign()
                )).collect(toList());
    }

    public CurrencyDto findCurrencyByCode(String currencyCode) throws SQLException {
        Optional<Currency> currency = currenciesDao.findCurrencyByCode(currencyCode);
        return currency.map(value -> new CurrencyDto(value.getId(),
                value.getFullName(),
                value.getCode(),
                value.getSign())).orElse(null);
    }

    //todo findAllCodes

    public boolean isCurrencyExists(String code) throws SQLException {
        return currenciesDao.findAll().stream()
                .map(Currency::getCode)
                .anyMatch(c -> c.equals(code));
    }

    public void addCurrency(String code, String name, String sign) throws SQLException {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setFullName(name);
        currency.setSign(sign);
        currenciesDao.addCurrency(currency);
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
