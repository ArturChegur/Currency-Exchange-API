package service;

import dao.CurrenciesDao;
import dto.CurrencyDto;
import entity.Currency;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private CurrencyService() {
    }

    public List<CurrencyDto> findAll() {
        return currenciesDao.findAll().stream()
                .map(currency -> new CurrencyDto(
                        currency.getId(),
                        currency.getFullName(),
                        currency.getCode(),
                        currency.getSign()
                )).collect(toList());
    }

    public Optional<CurrencyDto> findCurrencyByCode(String currencyCode) {
        Optional<Currency> resultValue = Optional.ofNullable(currenciesDao.findCurrencyByCode(currencyCode));
        if (resultValue.isPresent()) {
            Currency currency = resultValue.get();
            return Optional.of(new CurrencyDto(currency.getId(),
                    currency.getFullName(),
                    currency.getCode(),
                    currency.getSign()));
        }
        return Optional.empty();
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    public void addCurrency(String code, String name, String sign) {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setFullName(name);
        currency.setSign(sign);
        currenciesDao.addCurrency(currency);
    }
}
