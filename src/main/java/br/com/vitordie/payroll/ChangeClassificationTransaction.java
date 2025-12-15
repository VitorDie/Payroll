package br.com.vitordie.payroll;

public abstract class ChangeClassificationTransaction extends ChangeEmployeeTransaction {

    public ChangeClassificationTransaction(int id, PayrollDatabase database) {
        super(id, database);
    }

    @Override
    protected void change(Employee e) {
        // Precisamos garantir que a classe Employee tenha esses setters
        e.setClassification(getClassification());
        e.setSchedule(getSchedule());
    }

    // Métodos abstratos que as subclasses devem implementar
    protected abstract PaymentClassification getClassification();
    protected abstract PaymentSchedule getSchedule();
}