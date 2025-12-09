package br.com.vitordie.payroll;

public class HoldMethod implements PaymentMethod {
    @Override
    public void pay(Paycheck paycheck) {
        paycheck.setField("Disposition", "Hold");
    }

    @Override
    public String toString() {
        return "hold";
    }
}
