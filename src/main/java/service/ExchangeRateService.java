package service;

import dao.CurrenciesDao;
import dao.ExchangeRatesDao;
import dto.CurrencyDto;
import dto.ExchangeRateDto;
import entity.Currency;
import entity.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class ExchangeRateService implements Service<ExchangeRateDto> {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static final ExchangeRatesDao exchangeRateDao = ExchangeRatesDao.getInstance();
    private static final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private ExchangeRateService() {
    }

    @Override
    public List<ExchangeRateDto> findAll() throws SQLException {
        return exchangeRateDao.findAll().stream()
                .map(this::buildExchangeRateDto)
                .collect(toList());
    }

    @Override
    public ExchangeRateDto findByCode(String codePair) throws SQLException {
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByCode(codePair);
        return exchangeRate.map(this::buildExchangeRateDto).orElse(null);
    }

    @Override
    public void add(String baseCurrencyCode, String targetCurrencyCode, String rate) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrencyId(currenciesDao.findByCode(baseCurrencyCode).get().getId());
        exchangeRate.setTargetCurrencyId(currenciesDao.findByCode(targetCurrencyCode).get().getId());
        exchangeRate.setRate(new BigDecimal(rate));
        exchangeRateDao.add(exchangeRate);
    }

    @Override
    public boolean exists(String codePair) throws SQLException {
        return findByCode(codePair) != null;
    }

    private ExchangeRateDto buildExchangeRateDto(ExchangeRate exchangeRate) {
        Optional<Currency> baseCurrency;
        try {
            baseCurrency = currenciesDao.findCurrencyById(exchangeRate.getBaseCurrencyId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Optional<Currency> targetCurrency;
        try {
            targetCurrency = currenciesDao.findCurrencyById(exchangeRate.getTargetCurrencyId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ExchangeRateDto(exchangeRate.getId(),
                baseCurrency.map(value -> new CurrencyDto(
                        value.getId(),
                        value.getFullName(),
                        value.getCode(),
                        value.getSign())).orElse(null),
                targetCurrency.map(value -> new CurrencyDto(
                        value.getId(),
                        value.getFullName(),
                        value.getCode(),
                        value.getSign())).orElse(null),
                exchangeRate.getRate());
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}