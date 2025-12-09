package br.com.vitordie.payroll;

public abstract class Transaction {

    // 'readonly' do C# torna-se 'final' no Java
    protected final PayrollDatabase database;

    public Transaction(PayrollDatabase database) {
        this.database = database;
    }

    // Nomenclatura Java: métodos começam com letra minúscula (camelCase)
    public abstract void execute();
}