package br.com.vitordie.payroll;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Paycheck {
    private LocalDate payDate;
    private final LocalDate payPeriodStartDate; // 'readonly' vira 'final'
    private double grossPay;

    // Substituindo Hashtable por HashMap tipado para evitar castings
    private Map<String, String> fields = new HashMap<>();

    private double deductions;
    private double netPay;

    public Paycheck(LocalDate payPeriodStartDate, LocalDate payDate) {
        this.payPeriodStartDate = payPeriodStartDate;
        this.payDate = payDate;
    }

    // --- Getters e Setters ---

    public LocalDate getPayDate() {
        return payDate;
    }
    // Não criei setPayDate pois no C# só tinha 'get'

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public LocalDate getPayPeriodStartDate() {
        return payPeriodStartDate;
    }

    public LocalDate getPayPeriodEndDate() {
        // No seu código C# original, a Data Final era igual a Data de Pagamento
        return payDate;
    }

    // --- Métodos para campos dinâmicos (Fields) ---

    public void setField(String fieldName, String value) {
        fields.put(fieldName, value);
    }

    public String getField(String fieldName) {
        // Como o Map é <String, String>, não precisamos de casting (as string)
        return fields.get(fieldName);
    }
}
