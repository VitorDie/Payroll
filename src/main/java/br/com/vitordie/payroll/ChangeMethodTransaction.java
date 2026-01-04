package br.com.vitordie.payroll;

public abstract class ChangeMethodTransaction extends ChangeEmployeeTransaction {

    public ChangeMethodTransaction(int empId, PayrollDatabase database) {
        super(empId, database);
    }

    @Override
    protected void change(Employee e) {
        // Assume que a classe Employee possui o método setMethod
        e.setMethod(getMethod());
    }

    protected abstract PaymentMethod getMethod();
}