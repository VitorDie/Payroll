package br.com.vitordie.payroll;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeeklySchedule implements PaymentSchedule {

    @Override
    public boolean isPayDate(LocalDate payDate) {
        // Verifica se o dia da semana é SEXTA-FEIRA
        return payDate.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    @Override
    public LocalDate getPayPeriodStartDate(LocalDate date) {
        // No Java, usamos minusDays para subtrair dias
        return date.minusDays(6);
    }

    @Override
    public String toString() {
        return "weekly";
    }
}