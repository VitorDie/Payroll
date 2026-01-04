package br.com.vitordie.payroll;

public class MailMethod implements PaymentMethod {

    private final String address;

    public MailMethod(String address) {
        this.address = address;
    }

    @Override
    public void pay(Paycheck paycheck) {
        paycheck.setField("Disposition", "Mail");
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return String.format("mail (%s)", address);
    }
}