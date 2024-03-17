package service;

import dao.CurrenciesDao;
import dao.ExchangeRatesDao;
import dto.CurrencyDto;
import dto.ExchangeRateDto;
import entity.Currency;
import entity.ExchangeRate;

import java.util.List;

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

    public ExchangeRateDto findByCodePair(String baseCurrencyCode, String targetCurrencyCode) {
        return null; //todo
    }

    private ExchangeRateDto buildExchangeRateDto(ExchangeRate exchangeRate) {
        Currency baseCurrency = currenciesDao.findCurrencyById(exchangeRate.getBaseCurrencyId());
        Currency targetCurrency = currenciesDao.findCurrencyById(exchangeRate.getTargetCurrencyId());
        return new ExchangeRateDto(exchangeRate.getId(),
                new CurrencyDto(
                        baseCurrency.getId(),
                        baseCurrency.getFullName(),
                        baseCurrency.getCode(),
                        baseCurrency.getSign()
                ),
                new CurrencyDto(
                        targetCurrency.getId(),
                        targetCurrency.getFullName(),
                        targetCurrency.getCode(),
                        targetCurrency.getSign()
                ),
                exchangeRate.getRate());
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
