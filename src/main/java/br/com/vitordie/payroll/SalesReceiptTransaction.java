package br.com.vitordie.payroll;

import java.time.LocalDate;

public class SalesReceiptTransaction extends Transaction {
    private final LocalDate date;
    private final double saleAmount;
    private final int empId;

    public SalesReceiptTransaction(LocalDate date, double saleAmount, int empId, PayrollDatabase database) {
        super(database);
        this.date = date;
        this.saleAmount = saleAmount;
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = database.getEmployee(empId);

        if (e != null) {
            PaymentClassification pc = e.getClassification();

            // Verifica se a classificação é do tipo Comissionado
            if (pc instanceof CommissionedClassification) {
                CommissionedClassification cc = (CommissionedClassification) pc;
                cc.addSalesReceipt(new SalesReceipt(date, saleAmount));
            } else {
                throw new RuntimeException("Tried to add sales receipt to non-commissioned employee");
            }
        } else {
            throw new RuntimeException("No such employee.");
        }
    }
}
