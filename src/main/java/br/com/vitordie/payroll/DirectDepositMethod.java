package br.com.vitordie.payroll;

public class DirectDepositMethod implements PaymentMethod {

    private final String bank;
    private final String accountNumber;

    public DirectDepositMethod(String bank, String accountNumber) {
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    @Override
    public void pay(Paycheck paycheck) {
        paycheck.setField("Disposition", "Direct");
    }

    public String getBank() {
        return bank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return String.format("direct deposit into %s:%s", bank, accountNumber);
    }
}