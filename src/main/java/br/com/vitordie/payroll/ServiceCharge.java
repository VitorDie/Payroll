package br.com.vitordie.payroll;

import java.time.LocalDate;

public class ServiceCharge {
    private final LocalDate date;
    private final double amount;

    public ServiceCharge(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public LocalDate getDate() { // Equivalente ao .Time do C#
        return date;
    }

    public double getAmount() {
        return amount;
    }
}