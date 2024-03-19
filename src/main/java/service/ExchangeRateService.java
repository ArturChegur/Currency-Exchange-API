package service;

import dao.CurrenciesDao;
import dao.ExchangeRatesDao;
import dto.CurrencyDto;
import dto.ExchangeRateDto;
import entity.Currency;
import entity.ExchangeRate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static final ExchangeRatesDao exchangeRateDao = ExchangeRatesDao.getInstance();
    private static final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private ExchangeRateService() {
    }

    public List<ExchangeRateDto> findAll() {
        return exchangeRateDao.findAll().stream()
                .map(this::buildExchangeRateDto)
                .collect(toList());
    }

    public ExchangeRateDto findByCodePair(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        return buildExchangeRateDto(exchangeRateDao.findByCodePair(baseCurrencyCode, targetCurrencyCode));
    }

    private ExchangeRateDto buildExchangeRateDto(ExchangeRate exchangeRate) {
//        Optional<Currency> baseCurrency = currenciesDao.findCurrencyById(exchangeRate.getBaseCurrencyId());
//        Optional<Currency> targetCurrency = currenciesDao.findCurrencyById(exchangeRate.getTargetCurrencyId());
//        return new ExchangeRateDto(exchangeRate.getId(),
//                new CurrencyDto(
//                        baseCurrency.get().getId(),
//                        baseCurrency.get().getFullName(),
//                        baseCurrency.get().getCode(),
//                        baseCurrency.get().getSign()
//                ),
//                new CurrencyDto(
//                        targetCurrency.get().getId(),
//                        targetCurrency.get().getFullName(),
//                        targetCurrency.get().getCode(),
//                        targetCurrency.get().getSign()
//                ),
//                exchangeRate.getRate());
        return null; //todo
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
