package br.com.vitordie.payroll;

public class ChangeMailTransaction extends ChangeMethodTransaction {

    public ChangeMailTransaction(int empId, PayrollDatabase database) {
        super(empId, database);
    }

    @Override
    protected PaymentMethod getMethod() {
        return new MailMethod("3.14 Pi St");
    }
}