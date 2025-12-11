package br.com.vitordie.payroll;

public class DeleteEmployeeTransaction extends Transaction {
    private final int id;

    public DeleteEmployeeTransaction(int id, PayrollDatabase database) {
        super(database); // Chama o construtor da classe pai (Transaction)
        this.id = id;
    }

    @Override
    public void execute() {
        database.deleteEmployee(id);
    }
}
