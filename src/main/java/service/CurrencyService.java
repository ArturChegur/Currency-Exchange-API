package service;

import dao.CurrenciesDao;
import dto.CurrencyDto;

import java.util.List;


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

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
