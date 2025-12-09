package br.com.vitordie.payroll;

import java.time.LocalDate;

public class TimeCard {

    private final LocalDate date;
    private final double hours;

    public TimeCard(LocalDate date, double hours) {
        this.date = date;
        this.hours = hours;
    }

    public double getHours() {
        return hours;
    }

    public LocalDate getDate() {
        return date;
    }
}