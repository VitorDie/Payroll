package br.org.cefsa.payroll;

import java.time.LocalDateTime;

public class Employee {

    private final int empid;
    private String name;
    private final String address;
    private PaymentClassification classification;
    private PaymentSchedule schedule;
    private PaymentMethod method;
    private Affiliation affiliation = new noAffiliation();

    public Employee(int empid, String name, String address) {
        this.empid = empid;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

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

    public int getEmpid() {
        return empid;
    }

    public boolean isPayDate(LocalDateTime date) {
        return schedule.isPayDate(date);
    }

    public void payDay(Paycheck paycheck) {
        Double grossPay = classification.calculatePay(paycheck);
        Double deductions = affiliation.calculateDeductions(paycheck);
        Double netPay = grossPay - deductions;
        paycheck.setGrossPay(grossPay);
        paycheck.setDeductions(deductions);
        paycheck.setNetPay(netPay);
        method.pay(paycheck);
    }

    public LocalDateTime getPayPeriodStartDate(LocalDateTime date) {
        return schedule.getPayPeriodStartDate(date);
    }
}
