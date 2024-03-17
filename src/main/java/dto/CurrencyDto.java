package dto;

import java.util.Objects;


public class CurrencyDto {
    private final Integer id;
    private final String name;
    private final String code;
    private final String sign;

    public CurrencyDto(Integer id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyDto that = (CurrencyDto) o;
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
               + "\"name\":\"" + name + "\","
               + "\"code\":\"" + code + "\","
               + "\"sign\":\"" + sign + "\""
               + "}";
    }
}
