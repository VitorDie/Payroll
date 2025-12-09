package br.com.vitordie.payroll;

public abstract class AddEmployeeTransaction extends Transaction {

    // 'readonly' vira 'final'
    private final int empId;
    private final String name;
    private final String address;

    public AddEmployeeTransaction(int empId, String name, String address, PayrollDatabase database) {
        super(database); // Chama o construtor da classe pai (Transaction)
        this.empId = empId;
        this.name = name;
        this.address = address;
    }

    // Métodos abstratos do Template Method (em camelCase)
    protected abstract PaymentClassification makeClassification();

    protected abstract PaymentSchedule makeSchedule();

    @Override
    public void execute() {
        PaymentClassification pc = makeClassification();
        PaymentSchedule ps = makeSchedule();

        // Assume que a classe HoldMethod já existe (padrão para novos funcionários)
        PaymentMethod pm = new HoldMethod();

        Employee e = new Employee(empId, name, address);

        // Usando os setters que definimos na classe Employee
        e.setClassification(pc);
        e.setSchedule(ps);
        e.setMethod(pm);

        database.addEmployee(e);
    }

    @Override
    public String toString() {
        // getClass().getSimpleName() é o equivalente ao GetType().Name do C#
        return String.format("%s  id:%d   name:%s   address:%s",
                getClass().getSimpleName(), empId, name, address);
    }
}