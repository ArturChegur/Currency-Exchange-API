package util;

import dto.ResponseCurrencyDto;
import entity.Currency;

public final class Mapper {
    private Mapper() {
    }

    public static ResponseCurrencyDto currencyToResponseCurrencyDto(Currency currency) {
        return new ResponseCurrencyDto(currency.getId(),
                currency.getFullName(),
                currency.getCode(),
                currency.getSign());
    }
}
