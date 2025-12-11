package br.com.vitordie.payroll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UnionAffiliation implements Affiliation {

    // Substituindo Hashtable por HashMap Tipado
    private Map<LocalDate, ServiceCharge> charges = new HashMap<>();
    private int memberId;
    private final double dues;

    public UnionAffiliation(int memberId, double dues) {
        this.memberId = memberId;
        this.dues = dues;
    }

    // Construtor padrão encadeado
    public UnionAffiliation() {
        this(-1, 0.0);
    }

    public ServiceCharge getServiceCharge(LocalDate date) {
        return charges.get(date);
    }

    public void addServiceCharge(ServiceCharge sc) {
        charges.put(sc.getDate(), sc);
    }

    public double getDues() {
        return dues;
    }

    public int getMemberId() {
        return memberId;
    }

    @Override
    public double calculateDeductions(Paycheck paycheck) {
        double totalDues = 0;

        // 1. Calcula as taxas semanais (baseado nas sextas-feiras)
        int fridays = numberOfFridaysInPayPeriod(
                paycheck.getPayPeriodStartDate(), paycheck.getPayPeriodEndDate());

        totalDues = dues * fridays;

        // 2. Soma as taxas de serviço extras
        for (ServiceCharge charge : charges.values()) {
            if (isInPayPeriod(charge.getDate(), paycheck)) {
                totalDues += charge.getAmount();
            }
        }

        return totalDues;
    }

    // Lógica para contar sextas-feiras no período
    private int numberOfFridaysInPayPeriod(LocalDate start, LocalDate end) {
        int fridays = 0;

        // Loop em datas no Java: começamos no start e vamos somando dias até passar do end
        // !date.isAfter(end) é equivalente a date <= end
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                fridays++;
            }
        }
        return fridays;
    }

    // Metodo auxiliar para verificar intervalo de datas (substituindo DateUtil)
    private boolean isInPayPeriod(LocalDate date, Paycheck paycheck) {
        LocalDate start = paycheck.getPayPeriodStartDate();
        LocalDate end = paycheck.getPayPeriodEndDate();
        return !date.isBefore(start) && !date.isAfter(end);
    }
}