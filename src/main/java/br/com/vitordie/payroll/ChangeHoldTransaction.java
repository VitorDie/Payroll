package br.com.vitordie.payroll;

public class ChangeHoldTransaction extends ChangeMethodTransaction {

    public ChangeHoldTransaction(int empId, PayrollDatabase database) {
        super(empId, database);
    }

    @Override
    protected PaymentMethod getMethod() {
        return new HoldMethod();
    }
}