package example.domain.model.contract.hours;

import example.domain.type.date.Date;

/**
 * 休日条件
 */
public class DaysOffCondition {
    Date value;

    public DaysOffCondition(Date value) {
        this.value = value;
    }
}