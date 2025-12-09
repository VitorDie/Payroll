package br.com.vitordie.payroll;

public class AddCommissionedEmployee extends AddEmployeeTransaction {

    private final double baseRate;
    private final double commissionRate;

    public AddCommissionedEmployee(int id, String name, String address, double baseRate, double commissionRate, PayrollDatabase database) {
        super(id, name, address, database);
        this.baseRate = baseRate;
        this.commissionRate = commissionRate;
    }

    @Override
    protected PaymentClassification makeClassification() {
        return new CommissionedClassification(baseRate, commissionRate);
    }

    @Override
    protected PaymentSchedule makeSchedule() {
        return new BiWeeklySchedule();
    }
}