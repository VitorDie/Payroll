package br.com.vitordie.payroll;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaydayTransaction extends Transaction {

    private final LocalDate payDate;
    private final Map<Integer, Paycheck> paychecks = new HashMap<>();

    public PaydayTransaction(LocalDate payDate, PayrollDatabase database) {
        super(database);
        this.payDate = payDate;
    }

    @Override
    public void execute() {
        List<Integer> empIds = database.getAllEmployeeIds();

        for (int empId : empIds) {
            Employee employee = database.getEmployee(empId);
            if (employee.isPayDate(payDate)) {
                LocalDate startDate = employee.getPayPeriodStartDate(payDate);
                Paycheck pc = new Paycheck(startDate, payDate);
                paychecks.put(empId, pc);
                employee.payday(pc);
            }
        }
    }

    public Paycheck getPaycheck(int empId) {
        return paychecks.get(empId);
    }
}