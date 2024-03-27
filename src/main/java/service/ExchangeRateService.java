package service;

import dao.CurrenciesDao;
import dao.ExchangeRatesDao;
import dto.RequestCurrencyDto;
import dto.RequestExchangeRateDto;
import dto.ResponseExchangeRateDto;
import entity.Currency;
import entity.ExchangeRate;
import exceptions.DataNotFoundException;
import util.Mapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ExchangeRateService implements Service<ResponseExchangeRateDto, RequestExchangeRateDto> {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static final ExchangeRatesDao exchangeRateDao = ExchangeRatesDao.getInstance();
    private static final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private ExchangeRateService() {
    }

    @Override
    public List<ResponseExchangeRateDto> findAll() { //done
        return exchangeRateDao.findAll().stream()
                .map(this::buildExchangeRateDto)
                .collect(toList());
    }

    @Override
    public ResponseExchangeRateDto findByCode(RequestExchangeRateDto request) { //done
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByCode(request);
        if (exchangeRate.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found");
        } else {
            return buildExchangeRateDto(exchangeRate.get());
        }
    }

    @Override
    public void add(RequestExchangeRateDto request) { //done
        RequestCurrencyDto base = new RequestCurrencyDto();
        RequestCurrencyDto target = new RequestCurrencyDto();
        base.setCode(request.getBaseCurrency());
        target.setCode(request.getTargetCurrency());
        Optional<Currency> baseCurrency = currenciesDao.findByCode(base);
        Optional<Currency> targetCurrency = currenciesDao.findByCode(target);
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Currency with this code was not found");
        } else {
            request.setBaseCurrency(String.valueOf(baseCurrency.get().getCode()));
            request.setTargetCurrency(String.valueOf(targetCurrency.get().getCode()));
            exchangeRateDao.add(request);
        }
    }

    public void update(RequestExchangeRateDto request) { //done
        exchangeRateDao.update(request);
    }

    private ResponseExchangeRateDto buildExchangeRateDto(ExchangeRate exchangeRate) {
        Optional<Currency> baseCurrency = currenciesDao.findCurrencyById(exchangeRate.getBaseCurrencyId());
        Optional<Currency> targetCurrency = currenciesDao.findCurrencyById(exchangeRate.getTargetCurrencyId());
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found");
        } else {
            return new ResponseExchangeRateDto(exchangeRate.getId(),
                    Mapper.currencyToResponseCurrencyDto(baseCurrency.get()),
                    Mapper.currencyToResponseCurrencyDto(targetCurrency.get()),
                    exchangeRate.getRate());
        }
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}