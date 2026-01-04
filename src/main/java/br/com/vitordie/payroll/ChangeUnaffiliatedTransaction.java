package br.com.vitordie.payroll;

public class ChangeUnaffiliatedTransaction extends ChangeAffiliationTransaction {

    public ChangeUnaffiliatedTransaction(int empId, PayrollDatabase database) {
        super(empId, database);
    }

    @Override
    protected Affiliation getAffiliation() {
        return new NoAffiliation();
    }

    @Override
    protected void recordMembership(Employee e) {
        Affiliation affiliation = e.getAffiliation();
        if (affiliation instanceof UnionAffiliation) {
            UnionAffiliation unionAffiliation = (UnionAffiliation) affiliation;
            int memberId = unionAffiliation.getMemberId();
            database.removeUnionMember(memberId);
        }
    }
}