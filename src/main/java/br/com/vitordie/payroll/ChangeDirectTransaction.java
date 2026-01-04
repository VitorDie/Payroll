package br.com.vitordie.payroll;

public class ChangeDirectTransaction extends ChangeMethodTransaction {

    public ChangeDirectTransaction(int empId, PayrollDatabase database) {
        super(empId, database);
    }

    @Override
    protected PaymentMethod getMethod() {
        return new DirectDepositMethod("Bank -1", "123");
    }
}