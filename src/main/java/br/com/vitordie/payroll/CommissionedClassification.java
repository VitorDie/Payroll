package br.com.vitordie.payroll;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CommissionedClassification extends PaymentClassification {

    private final double baseRate;
    private final double commissionRate;

    // Substituindo Hashtable por HashMap com Generics
    private Map<LocalDate, SalesReceipt> salesReceipts = new HashMap<>();

    public CommissionedClassification(double baseRate, double commissionRate) {
        this.baseRate = baseRate;
        this.commissionRate = commissionRate;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void addSalesReceipt(SalesReceipt receipt) {
        // Assume-se que SalesReceipt tem um método getDate()
        salesReceipts.put(receipt.getDate(), receipt);
    }

    public SalesReceipt getSalesReceipt(LocalDate date) {
        return salesReceipts.get(date);
    }

    @Override
    public double calculatePay(Paycheck paycheck) {
        double salesTotal = 0;

        for (SalesReceipt receipt : salesReceipts.values()) {
            if (isInPayPeriod(receipt.getDate(), paycheck)) {
                // Assume-se que SalesReceipt tem um método getSaleAmount()
                salesTotal += receipt.getSaleAmount();
            }
        }

        // A lógica do C# multiplica por 0.01 (assumindo que a taxa vem como 10 para 10%)
        return baseRate + (salesTotal * commissionRate * 0.01);
    }

    // Helper method para verificar intervalo de datas (o mesmo usado no HourlyClassification)
    private boolean isInPayPeriod(LocalDate date, Paycheck paycheck) {
        LocalDate start = paycheck.getPayPeriodStartDate();
        LocalDate end = paycheck.getPayPeriodEndDate();
        return !date.isBefore(start) && !date.isAfter(end);
    }

    @Override
    public String toString() {
        return String.format("$%.2f + %.1f%% sales commission", baseRate, commissionRate);
    }
}