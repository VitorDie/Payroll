package br.com.vitordie.payroll;

import java.time.LocalDate;

public class MonthlySchedule implements PaymentSchedule {

    private boolean isLastDayOfMonth(LocalDate date) {
        // No Java, não precisamos somar um dia para descobrir isso.
        // O método lengthOfMonth() já nos diz quantos dias o mês tem (28, 29, 30 ou 31).
        return date.getDayOfMonth() == date.lengthOfMonth();
    }

    @Override
    public boolean isPayDate(LocalDate payDate) {
        return isLastDayOfMonth(payDate);
    }

    @Override
    public LocalDate getPayPeriodStartDate(LocalDate date) {
        // No lugar daquele laço 'while' do C# que voltava os dias um por um,
        // no Java simplesmente dizemos: "Me dê este mesmo ano/mês, mas no dia 1".
        return date.withDayOfMonth(1);
    }

    @Override
    public String toString() {
        return "monthly";
    }
}