package br.com.vitordie.payroll;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HourlyClassification extends PaymentClassification {

    private final double hourlyRate;

    // Usando Map com Generics para associar Data -> Cartão de Ponto
    private Map<LocalDate, TimeCard> timeCards = new HashMap<>();

    public HourlyClassification(double rate) {
        this.hourlyRate = rate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public TimeCard getTimeCard(LocalDate date) {
        return timeCards.get(date);
    }

    public void addTimeCard(TimeCard card) {
        // Assumindo que TimeCard tem um método getDate()
        timeCards.put(card.getDate(), card);
    }

    @Override
    public double calculatePay(Paycheck paycheck) {
        double totalPay = 0.0;

        // Itera sobre todos os cartões de ponto armazenados
        for (TimeCard timeCard : timeCards.values()) {
            if (isInPayPeriod(timeCard.getDate(), paycheck)) {
                totalPay += calculatePayForTimeCard(timeCard);
            }
        }

        return totalPay;
    }

    private double calculatePayForTimeCard(TimeCard card) {
        // Assumindo que TimeCard tem um método getHours() retornando double
        double overtimeHours = Math.max(0.0, card.getHours() - 8);
        double normalHours = card.getHours() - overtimeHours;

        return (hourlyRate * normalHours) + (hourlyRate * 1.5 * overtimeHours);
    }

    // Substituto para o DateUtil.IsInPayPeriod
    private boolean isInPayPeriod(LocalDate date, Paycheck paycheck) {
        LocalDate start = paycheck.getPayPeriodStartDate();
        LocalDate end = paycheck.getPayPeriodEndDate();

        // Verifica se a data é >= inicio E <= fim
        // !date.isBefore(start) equivale a date >= start
        // !date.isAfter(end) equivale a date <= end
        return !date.isBefore(start) && !date.isAfter(end);
    }

    @Override
    public String toString() {
        return String.format("$%.2f/hr", hourlyRate);
    }
}