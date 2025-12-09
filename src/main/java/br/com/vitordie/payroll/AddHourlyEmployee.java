package br.com.vitordie.payroll;

public class AddHourlyEmployee extends AddEmployeeTransaction {

    private final double hourlyRate;

    public AddHourlyEmployee(int id, String name, String address, double hourlyRate, PayrollDatabase database) {
        super(id, name, address, database);
        this.hourlyRate = hourlyRate;
    }

    @Override
    protected PaymentClassification makeClassification() {
        return new HourlyClassification(hourlyRate);
    }

    @Override
    protected PaymentSchedule makeSchedule() {
        return new WeeklySchedule();
    }
}