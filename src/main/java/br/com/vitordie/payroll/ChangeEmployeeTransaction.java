package br.com.vitordie.payroll;

public abstract class ChangeEmployeeTransaction extends Transaction {

    private final int empId;

    public ChangeEmployeeTransaction(int empId, PayrollDatabase database) {
        super(database);
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = database.getEmployee(empId);

        if (e != null) {
            change(e);
        } else {
            throw new RuntimeException("No such employee.");
        }
    }

    // Método abstrato para ser implementado pelas subclasses
    protected abstract void change(Employee e);
}