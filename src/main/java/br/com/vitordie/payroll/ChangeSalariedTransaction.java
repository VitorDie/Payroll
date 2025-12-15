package br.com.vitordie.payroll;

public class ChangeSalariedTransaction extends ChangeClassificationTransaction {

    private final double salary;

    public ChangeSalariedTransaction(int id, double salary, PayrollDatabase database) {
        super(id, database);
        this.salary = salary;
    }

    @Override
    protected PaymentClassification getClassification() {
        return new SalariedClassification(salary);
    }

    @Override
    protected PaymentSchedule getSchedule() {
        return new MonthlySchedule();
    }
}