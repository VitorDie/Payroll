package br.com.vitordie.payroll;

public class AddSalariedEmployee extends AddEmployeeTransaction {

    private final double salary;

    public AddSalariedEmployee(int id, String name, String address, double salary, PayrollDatabase database) {
        super(id, name, address, database);
        this.salary = salary;
    }

    @Override
    protected PaymentClassification makeClassification() {
        return new SalariedClassification(salary);
    }

    @Override
    protected PaymentSchedule makeSchedule() {
        return new MonthlySchedule();
    }
}