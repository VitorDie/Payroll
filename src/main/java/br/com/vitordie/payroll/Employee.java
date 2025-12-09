package br.com.vitordie.payroll;

import java.time.LocalDate;

public class Employee {
    private final int empId; // 'readonly' vira 'final'
    private String name;
    private final String address; // 'readonly' vira 'final'

    // Associações (Strategy Pattern)
    private PaymentClassification classification;
    private PaymentSchedule schedule;
    private PaymentMethod method;
    private Affiliation affiliation = new NoAffiliation();

    public Employee(int empId, String name, String address) {
        this.empId = empId;
        this.name = name;
        this.address = address;
    }

    // --- Getters e Setters (Properties do C#) ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    // Sem setAddress pois era readonly no construtor

    public PaymentClassification getClassification() {
        return classification;
    }

    public void setClassification(PaymentClassification classification) {
        this.classification = classification;
    }

    public PaymentSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(PaymentSchedule schedule) {
        this.schedule = schedule;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public Affiliation getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }

    public int getEmpId() {
        return empId;
    }

    // --- Métodos de Negócio ---

    // Note o uso de LocalDate ao invés de DateTime ou Date
    public boolean isPayDate(LocalDate date) {
        return schedule.isPayDate(date);
    }

    public LocalDate getPayPeriodStartDate(LocalDate date) {
        return schedule.getPayPeriodStartDate(date);
    }

    public void payday(Paycheck paycheck) {
        double grossPay = classification.calculatePay(paycheck);
        double deductions = affiliation.calculateDeductions(paycheck);
        double netPay = grossPay - deductions;

        // Em Java, assumimos que Paycheck tem setters
        paycheck.setGrossPay(grossPay);
        paycheck.setDeductions(deductions);
        paycheck.setNetPay(netPay);

        method.pay(paycheck);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Emp#: ").append(empId).append("   ");
        builder.append(name).append("   ");
        builder.append(address).append("   ");

        // O Java chama .toString() automaticamente ao concatenar objetos
        builder.append("Paid ").append(classification).append(" ");
        builder.append(schedule);
        builder.append(" by ").append(method);

        return builder.toString();
    }
}
