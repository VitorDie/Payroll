package br.com.vitordie.payroll;

public class ChangeCommissionedTransaction extends ChangeClassificationTransaction {

    private final double baseSalary;
    private final double commissionRate;

    public ChangeCommissionedTransaction(int id, double baseSalary, double commissionRate, PayrollDatabase database) {
        super(id, database);
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
    }

    @Override
    protected PaymentClassification getClassification() {
        return new CommissionedClassification(baseSalary, commissionRate);
    }

    @Override
    protected PaymentSchedule getSchedule() {
        return new BiWeeklySchedule();
    }
}