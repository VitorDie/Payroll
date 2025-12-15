package br.com.vitordie.payroll;

public class ChangeHourlyTransaction extends ChangeClassificationTransaction {

    private final double hourlyRate;

    public ChangeHourlyTransaction(int id, double hourlyRate, PayrollDatabase database) {
        super(id, database);
        this.hourlyRate = hourlyRate;
    }

    @Override
    protected PaymentClassification getClassification() {
        return new HourlyClassification(hourlyRate);
    }

    @Override
    protected PaymentSchedule getSchedule() {
        return new WeeklySchedule();
    }
}