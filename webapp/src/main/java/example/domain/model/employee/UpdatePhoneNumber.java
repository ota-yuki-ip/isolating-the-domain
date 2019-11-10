package example.domain.model.employee;

/**
 * 電話番号の変更
 */
public class UpdatePhoneNumber {

    EmployeeNumber employeeNumber;

    PhoneNumber phoneNumber;

    public UpdatePhoneNumber(EmployeeNumber employeeNumber, PhoneNumber phoneNumber) {
        this.employeeNumber = employeeNumber;
        this.phoneNumber = phoneNumber;
    }

    public EmployeeNumber employeeNumber() {
        return employeeNumber;
    }

    public PhoneNumber phoneNumber() {
        return phoneNumber;
    }
}
