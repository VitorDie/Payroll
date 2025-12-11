package br.com.vitordie.payroll;

import java.time.LocalDate;

public class TimeCardTransaction extends Transaction {
    private final LocalDate date;
    private final double hours;
    private final int empId;

    public TimeCardTransaction(LocalDate date, double hours, int empId, PayrollDatabase database) {
        super(database);
        this.date = date;
        this.hours = hours;
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = database.getEmployee(empId);

        if (e != null) {
            PaymentClassification pc = e.getClassification();

            // Verifica se a classificação é do tipo Horista
            if (pc instanceof HourlyClassification) {
                HourlyClassification hc = (HourlyClassification) pc;
                hc.addTimeCard(new TimeCard(date, hours));
            } else {
                throw new RuntimeException("Tried to add timecard to non-hourly employee");
            }
        } else {
            throw new RuntimeException("No such employee.");
        }
    }
}
