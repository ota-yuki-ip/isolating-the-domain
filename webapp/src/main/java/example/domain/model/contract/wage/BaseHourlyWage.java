package example.domain.model.contract.wage;

import example.domain.model.legislation.ExtraPayRate;
import example.domain.model.wage.HourlyWage;
import example.domain.type.amount.Amount;
import example.domain.type.amount.RoundingMode;

import java.math.BigDecimal;

/**
 * 基本の時給
 */
public class BaseHourlyWage {

    HourlyWage value;

    @Deprecated
    public BaseHourlyWage() {
    }

    public BaseHourlyWage(Integer value) {
        this(new HourlyWage(value));
    }

    public BaseHourlyWage(String value) {
        this(new HourlyWage(new Amount(value)));
    }

    BaseHourlyWage(HourlyWage value) {

        this.value = value;
    }

    public static BaseHourlyWage disable() {
        return new BaseHourlyWage(HourlyWage.disable());
    }

    public BigDecimal toBigDecimal() {
        return new BigDecimal(value.value().value());
    }

    public HourlyWage value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    HourlyWage withExtraPayRate(ExtraPayRate extraPayRate) {
        return new HourlyWage(value.value().multiply(extraPayRate.value(), RoundingMode.切り捨て));
    }
}
