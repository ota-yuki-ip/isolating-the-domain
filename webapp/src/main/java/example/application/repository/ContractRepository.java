package example.application.repository;


import example.domain.model.contract.ContractEffectiveDate;
import example.domain.model.contract.ContractConditions;
import example.domain.model.contract.Contracts;
import example.domain.model.contract.wage.WageCondition;
import example.domain.model.employee.ContractingEmployees;
import example.domain.model.employee.Employee;

/**
 * 契約リポジトリ
 */
public interface ContractRepository {
    ContractConditions getContractConditions(Employee employee);

    void registerHourlyWage(Employee employee, ContractEffectiveDate effectiveDate, WageCondition wageCondition);

    Contracts findContracts(ContractingEmployees contractingEmployees);
}
