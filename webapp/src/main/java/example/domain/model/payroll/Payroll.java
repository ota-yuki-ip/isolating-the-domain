package example.domain.model.payroll;

import example.domain.model.attendance.Attendance;
import example.domain.model.attendance.PayableWork;
import example.domain.model.contract.Contract;
import example.domain.model.contract.wage.WageCondition;
import example.domain.model.employee.EmployeeNumber;
import example.domain.model.employee.Name;
import example.domain.model.timerecord.evaluation.OverLegalHoursWorkTime;
import example.domain.model.timerecord.evaluation.WeeklyTimeRecord;

import java.math.BigDecimal;

/**
 * 給与
 */
public class Payroll {

    Contract contract;
    Attendance attendance;
    Attendance beforeMonthAttendance;

    public Payroll(Contract contract, Attendance attendance, Attendance beforeMonthAttendance) {
        this.contract = contract;
        this.attendance = attendance;
        this.beforeMonthAttendance = beforeMonthAttendance;
    }

    public EmployeeNumber employeeNumber() {
        return contract.employeeNumber();
    }

    public Name employeeName() {
        return contract.employeeName();
    }

    public PaymentAmount totalPayment() {
        PaymentAmount paymentAmount = new PaymentAmount(BigDecimal.ZERO);

        for (PayableWork payableWork : attendance.listPayableWork()) {
            WageCondition wageCondition = contract.wageConditionAt(payableWork.date());

            WeeklyTimeRecord weeklyTimeRecord = WeeklyTimeRecord.from(attendance.timeRecords(), beforeMonthAttendance.timeRecords(), payableWork.date());

            OverLegalHoursWorkTime overLegalHoursWorkTime = payableWork.overLegalHoursWorkTime(weeklyTimeRecord);

            // TODO: 支払い対象時間を出すことがもっとわかりやすいようにする
            PaymentAmount 基本給 = new PaymentWorkTime(payableWork.workTime()).multiply(wageCondition.baseHourlyWage().value());
            PaymentAmount 深夜割増 = new PaymentWorkTime(payableWork.nightWorkTime()).multiply(wageCondition.nightHourlyExtraWage().value());
            PaymentAmount 法定時間外労働月60時間超割増 = new PaymentWorkTime(overLegalHoursWorkTime.moreThan60HoursWorkTime().quarterHour()).multiply(wageCondition.overLegalMoreThan60HoursHourlyExtraWage().value());
            PaymentAmount 法定時間外労働月60時間以内割増 = new PaymentWorkTime(overLegalHoursWorkTime.within60HoursWorkTime().quarterHour()).multiply(wageCondition.overLegalWithin60HoursHourlyExtraWage().value());
            PaymentAmount 法定休日労働割増 = new PaymentWorkTime(payableWork.legalDaysOffWorkTime(weeklyTimeRecord).quarterHour()).multiply(wageCondition.legalDaysOffHourlyExtraWage().value());
            paymentAmount = paymentAmount.addAll(基本給, 深夜割増, 法定時間外労働月60時間超割増, 法定時間外労働月60時間以内割増, 法定休日労働割増);
        }

        return paymentAmount;
    }

    public PayrollStatus payrollStatus() {
        return PayrollStatus.from(contract.contractStatus(attendance.attendDates().toDates()));
    }
}
