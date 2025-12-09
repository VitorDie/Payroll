package br.com.vitordie.payroll;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class BiWeeklySchedule implements PaymentSchedule {

    @Override
    public boolean isPayDate(LocalDate payDate) {
        // Verifica se é Sexta-feira E se o dia do mês é par
        return payDate.getDayOfWeek() == DayOfWeek.FRIDAY &&
                payDate.getDayOfMonth() % 2 == 0;
    }

    @Override
    public LocalDate getPayPeriodStartDate(LocalDate date) {
        // 2 semanas = 14 dias. Se o pagamento é no dia N, o período começou 13 dias atrás.
        return date.minusDays(13);
    }

    @Override
    public String toString() {
        return "bi-weekly";
    }
}