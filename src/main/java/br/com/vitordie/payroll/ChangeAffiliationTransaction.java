package br.com.vitordie.payroll;

public abstract class ChangeAffiliationTransaction extends ChangeEmployeeTransaction {

    public ChangeAffiliationTransaction(int empId, PayrollDatabase database) {
        super(empId, database);
    }

    @Override
    protected void change(Employee e) {
        recordMembership(e);
        e.setAffiliation(getAffiliation());
    }

    protected abstract Affiliation getAffiliation();
    protected abstract void recordMembership(Employee e);
}