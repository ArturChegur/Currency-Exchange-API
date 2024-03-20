package service;

import dao.CurrenciesDao;
import dto.CurrencyDto;
import entity.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class CurrencyService implements Service<CurrencyDto> {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private CurrencyService() {
    }

    @Override
    public List<CurrencyDto> findAll() throws SQLException {
        return currenciesDao.findAll().stream()
                .map(currency -> new CurrencyDto(
                        currency.getId(),
                        currency.getFullName(),
                        currency.getCode(),
                        currency.getSign()
                )).collect(toList());
    }

    @Override
    public CurrencyDto findByCode(String currencyCode) throws SQLException {
        Optional<Currency> currency = currenciesDao.findByCode(currencyCode);
        return currency.map(value -> new CurrencyDto(value.getId(),
                value.getFullName(),
                value.getCode(),
                value.getSign())).orElse(null);
    }

    //todo findAllCodes

    @Override
    public boolean exists(String code) throws SQLException {
        return currenciesDao.findAll().stream()
                .map(Currency::getCode)
                .anyMatch(c -> c.equals(code));
    }

    @Override
    public void add(String code, String name, String sign) throws SQLException {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setFullName(name);
        currency.setSign(sign);
        currenciesDao.add(currency);
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
