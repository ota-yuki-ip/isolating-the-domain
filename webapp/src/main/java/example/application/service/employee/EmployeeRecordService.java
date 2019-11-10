package example.application.service.employee;

import example.application.repository.EmployeeRepository;
import example.domain.model.employee.*;
import org.springframework.stereotype.Service;

/**
 * 従業員登録更新サービス
 */
@Service
public class EmployeeRecordService {

    EmployeeRepository employeeRepository;

    /**
     * 従業員契約準備
     */
    public EmployeeNumber prepareNewContract() {
        return employeeRepository.registerNew();
    }

    /**
     * 従業員名登録
     */
    public void registerName(UpdateName name) {
        employeeRepository.registerName(name.employeeNumber(), name.name());
    }

    /**
     * 従業員メールアドレス登録
     */
    public void registerMailAddress(UpdateMailAddress mail) {
        employeeRepository.registerMailAddress(mail.employeeNumber(), mail.mailAddress());
    }

    /**
     * 従業員電話番号登録
     */
    public void registerPhoneNumber(UpdatePhoneNumber phone) {
        employeeRepository.registerPhoneNumber(phone.employeeNumber(), phone.phoneNumber());
    }

    /**
     * 従業員契約開始
     */
    public void inspireContract(EmployeeNumber employeeNumber) {
        employeeRepository.registerInspireContract(employeeNumber);
    }

    /**
     * 従業員契約終了
     */
    public void expireContract(Employee employee) {
        employeeRepository.registerExpireContract(employee);
    }


    EmployeeRecordService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
