package br.com.vitordie.payroll;

import java.time.LocalDate;

public interface PaymentSchedule {
    boolean isPayDate(LocalDate payDate);

    LocalDate getPayPeriodStartDate(LocalDate payDate);
}
