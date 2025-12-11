package br.com.vitordie.payroll;

import java.time.LocalDate;

public class ServiceChargeTransaction extends Transaction {

    private final int memberId;
    private final LocalDate date; // Renomeei de 'time' para 'date' para condizer com o tipo
    private final double charge;

    public ServiceChargeTransaction(int id, LocalDate date, double charge, PayrollDatabase database) {
        super(database);
        this.memberId = id;
        this.date = date;
        this.charge = charge;
    }

    @Override
    public void execute() {
        // Busca o funcionário pelo ID do Sindicato (Mapeamento MemberId -> Employee)
        Employee e = database.getUnionMember(memberId);

        if (e != null) {

            // Verificação de tipo segura (Java não tem o operador 'as' da mesma forma que C#)
            if (e.getAffiliation() instanceof UnionAffiliation) {
                UnionAffiliation ua = (UnionAffiliation) e.getAffiliation();

                // Adiciona a taxa usando a classe ServiceCharge que criamos antes
                ua.addServiceCharge(new ServiceCharge(date, charge));
            } else {
                throw new RuntimeException(
                        "Tries to add service charge to union member without a union affiliation");
            }
        } else {
            throw new RuntimeException("No such union member.");
        }
    }
}