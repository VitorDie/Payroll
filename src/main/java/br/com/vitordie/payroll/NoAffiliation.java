package br.com.vitordie.payroll;

public class NoAffiliation implements Affiliation {
    @Override
    public double calculateDeductions(Paycheck paycheck) {
        return 0;
    }
}
