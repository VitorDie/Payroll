package br.com.vitordie.payroll;

import java.time.LocalDate;

public class SalesReceipt {

    private final LocalDate date;
    private final double saleAmount;

    public SalesReceipt(LocalDate date, double amount) {
        this.date = date;
        this.saleAmount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getSaleAmount() {
        return saleAmount;
    }
}