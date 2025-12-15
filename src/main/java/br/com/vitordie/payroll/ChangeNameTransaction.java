package br.com.vitordie.payroll;

public class ChangeNameTransaction extends ChangeEmployeeTransaction {

    private final String newName;

    public ChangeNameTransaction(int id, String newName, PayrollDatabase database) {
        super(id, database);
        this.newName = newName;
    }

    @Override
    protected void change(Employee e) {
        // Requer que a classe Employee tenha o método public void setName(String name)
        e.setName(newName);
    }
}