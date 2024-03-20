package entity;

import java.math.BigDecimal;

public class ExchangeRate {
    private Integer id;
    private Integer baseCurrencyId;
    private Integer targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(Integer id, Integer baseCurrencyId, Integer targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
