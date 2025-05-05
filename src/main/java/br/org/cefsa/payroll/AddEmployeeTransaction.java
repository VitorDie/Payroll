package br.org.cefsa.payroll;

public interface AddEmployeeTransaction extends Transaction {
    public void execute();
    protected PaymentClassification makeClassification();
    protected PaymentSchedule makeSchedule();
}
