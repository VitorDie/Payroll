package br.com.vitordie.payroll;

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

        Assertions.assertEquals(1000.00, sc.getSalary(), .001);
        PaymentSchedule ps = e.getSchedule();
        Assertions.assertTrue(ps instanceof MonthlySchedule);

        PaymentMethod pm = e.getMethod();
        Assertions.assertTrue(pm instanceof HoldMethod);
    }

    @Test
    public void testAddHourlyEmployee() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Micah", "Home", 200.00, database);
        t.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertEquals("Micah", e.getName());

        PaymentClassification pc = e.getClassification();
        Assertions.assertTrue(pc instanceof HourlyClassification);

        // Casting explícito (o 'as' do C# não existe dessa forma no Java)
        HourlyClassification hc = (HourlyClassification) pc;

        Assertions.assertEquals(200.00, hc.getHourlyRate(), .001);

        PaymentSchedule ps = e.getSchedule();
        Assertions.assertTrue(ps instanceof WeeklySchedule);

        PaymentMethod pm = e.getMethod();
        Assertions.assertTrue(pm instanceof HoldMethod);
    }
}
