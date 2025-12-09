package br.com.vitordie.payroll;

public class SalariedClassification extends PaymentClassification {

    private final double salary;

    public SalariedClassification(double salary) {
        this.salary = salary;
    }

    // Property 'Salary' vira um getter
    public double getSalary() {
        return salary;
    }

    @Override
    public double calculatePay(Paycheck paycheck) {
        return salary;
    }

    @Override
    public String toString() {
        // Formata como moeda (ex: $1000.00)
        return String.format("$%.2f", salary);
    }
}