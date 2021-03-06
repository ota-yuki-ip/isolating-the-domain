package example.application.service;

import example.application.coordinator.employee.EmployeeRecordCoordinator;
import example.application.service.contract.ContractQueryService;
import example.application.service.contract.ContractRecordService;
import example.application.service.employee.EmployeeQueryService;
import example.domain.model.contract.*;
import example.domain.model.contract.wage.*;
import example.domain.model.employee.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContractConditionRecordServiceTest {
    @Autowired
    EmployeeRecordCoordinator employeeRecordCoordinator;
    @Autowired
    EmployeeQueryService employeeQueryService;
    @Autowired
    ContractRecordService sutRecord;
    @Autowired
    ContractQueryService sutQuery;

    @Test
    void test() {
        EmployeeNumber employeeNumber = employeeRecordCoordinator.register(
                new EmployeeToRegister(new Name("any"), new MailAddress("any"), new PhoneNumber("any")));

        登録直後の従業員は時給を持たない(employeeNumber);
        時給が登録できる(employeeNumber);
        指定日以降の時給を登録できる(employeeNumber);
        指定日以降次の指定があるまでの時給を登録できる(employeeNumber);
        同じ指定日の時給を上書きできる(employeeNumber);
    }

    void 登録直後の従業員は時給を持たない(EmployeeNumber employeeNumber) {
        Employee employee = employeeQueryService.choose(employeeNumber);
        ContractConditions history = sutQuery.getContractWages(employee);
        assertTrue(history.list().isEmpty());
    }

    void 時給が登録できる(EmployeeNumber employeeNumber) {
        Employee employee = employeeQueryService.choose(employeeNumber);

        LocalDate effectiveDate1 = LocalDate.parse("2018-12-12", DateTimeFormatter.ISO_DATE);
        updateHourlyWageContract(employee, new ContractEffectiveDate(effectiveDate1), new BaseHourlyWage(800));

        ContractConditions history1 = sutQuery.getContractWages(employee);
        assertEquals(1, history1.list().size());
        assertAll(
                () -> assertEquals(effectiveDate1, history1.list().get(0).effectiveDate().value()),
                () -> assertEquals(800, history1.list().get(0).baseHourlyWage().toBigDecimal().intValue())
        );
    }

    void 指定日以降の時給を登録できる(EmployeeNumber employeeNumber) {
        Employee employee = employeeQueryService.choose(employeeNumber);

        LocalDate effectiveDate2 = LocalDate.parse("2018-12-22", DateTimeFormatter.ISO_DATE);
        updateHourlyWageContract(employee, new ContractEffectiveDate(effectiveDate2), new BaseHourlyWage(850));
        ContractConditions history2 = sutQuery.getContractWages(employee);
        assertEquals(2, history2.list().size());
        assertAll(
                () -> assertEquals(effectiveDate2, history2.list().get(0).effectiveDate().value()),
                () -> assertEquals(850, history2.list().get(0).baseHourlyWage().toBigDecimal().intValue()),
                () -> assertEquals(800, history2.list().get(1).baseHourlyWage().toBigDecimal().intValue())
        );
    }

    void 指定日以降次の指定があるまでの時給を登録できる(EmployeeNumber employeeNumber) {
        Employee employee = employeeQueryService.choose(employeeNumber);

        LocalDate effectiveDate3 = LocalDate.parse("2018-12-17", DateTimeFormatter.ISO_DATE);
        updateHourlyWageContract(employee, new ContractEffectiveDate(effectiveDate3), new BaseHourlyWage(830));
        ContractConditions history3 = sutQuery.getContractWages(employee);
        assertEquals(3, history3.list().size());
        assertAll(
                () -> assertEquals(850, history3.list().get(0).baseHourlyWage().toBigDecimal().intValue()),

                () -> assertEquals(effectiveDate3, history3.list().get(1).effectiveDate().value()),
                () -> assertEquals(830, history3.list().get(1).baseHourlyWage().toBigDecimal().intValue()),

                () -> assertEquals(800, history3.list().get(2).baseHourlyWage().toBigDecimal().intValue())
        );
    }

    void 同じ指定日の時給を上書きできる(EmployeeNumber employeeNumber) {
        Employee employee = employeeQueryService.choose(employeeNumber);

        LocalDate effectiveDate1 = LocalDate.parse("2018-12-12", DateTimeFormatter.ISO_DATE);
        updateHourlyWageContract(employee, new ContractEffectiveDate(effectiveDate1), new BaseHourlyWage(1000));

        ContractConditions history = sutQuery.getContractWages(employee);
        assertEquals(3, history.list().size());
        assertAll(
                () -> assertEquals(850, history.list().get(0).baseHourlyWage().toBigDecimal().intValue()),
                () -> assertEquals(830, history.list().get(1).baseHourlyWage().toBigDecimal().intValue()),
                () -> assertEquals(1000, history.list().get(2).baseHourlyWage().toBigDecimal().intValue())
        );
    }

    private void updateHourlyWageContract(Employee employee, ContractEffectiveDate effectiveDate, BaseHourlyWage baseHourlyWage) {
        OverTimeExtraRate overTimeExtraRate = new OverTimeExtraRate(new OverLegalWithin60HoursExtraRate(25), OverLegalMoreThan60HoursExtraRate.regulation(), LegalDaysOffExtraRate.regulation(), new NightExtraRate(35));
        sutRecord.registerHourlyWage(employee, effectiveDate, new WageCondition(baseHourlyWage, overTimeExtraRate));
    }
}
