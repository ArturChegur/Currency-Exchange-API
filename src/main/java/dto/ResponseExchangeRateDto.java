package dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ResponseExchangeRateDto {
    private final Integer id;
    private final ResponseCurrencyDto baseCurrency;
    private final ResponseCurrencyDto targetCurrency;
    private final BigDecimal rate;

    public ResponseExchangeRateDto(Integer id, ResponseCurrencyDto baseCurrency, ResponseCurrencyDto targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public ResponseCurrencyDto getBaseCurrency() {
        return baseCurrency;
    }

    public ResponseCurrencyDto getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseExchangeRateDto that = (ResponseExchangeRateDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{"
               + "\"id\":" + id + ","
               + "\"baseCurrency\":" + baseCurrency.toString() + ","
               + "\"targetCurrency\":" + targetCurrency.toString() + ","
               + "\"rate\":" + rate
               + "}";
    }
}