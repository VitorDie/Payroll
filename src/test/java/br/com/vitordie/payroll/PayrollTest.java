package br.com.vitordie.payroll_domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PayrollTest {

    private PayrollDatabase database;

    @BeforeEach
    public void setUp() {
        database = new InMemoryPayrollDatabase();
    }

    @Test
    public void testAddSalariedEmployee() {
        int empId = 1;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00, database);
        t.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertEquals("Bob", e.getName());

        PaymentClassification pc = e.getClassification();
        Assertions.assertTrue(pc instanceof  SalariedClassification);
        SalariedClassification sc = (SalariedClassification) pc;

        Assertions.assertEquals(100.00, sc.getSalary(), .001);
        PaymentSchedule ps = e.getSchedule();
        Assertions.assertTrue(ps instanceof MonthlySchedule);

        PaymentMethod pm = e.getMethod();
        Assertions.assertTrue(pm instanceof HoldMethod);
    }
}
