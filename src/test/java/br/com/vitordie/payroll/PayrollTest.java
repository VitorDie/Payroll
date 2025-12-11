package br.com.vitordie.payroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

    @Test
    public void testAddCommissionedEmployee() {
        int empId = 3;
        // Precisaremos criar esta transação: AddCommissionedEmployee
        AddCommissionedEmployee t =
                new AddCommissionedEmployee(empId, "Justin", "Home", 2500, 9.5, database);
        t.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);
        Assertions.assertEquals("Justin", e.getName());

        PaymentClassification pc = e.getClassification();
        // Verifica se é instância da classe que vamos criar
        Assertions.assertTrue(pc instanceof CommissionedClassification);

        CommissionedClassification cc = (CommissionedClassification) pc;

        // Validando Salário Base e Taxa de Comissão
        Assertions.assertEquals(2500, cc.getBaseRate(), .001);
        Assertions.assertEquals(9.5, cc.getCommissionRate(), .001);

        PaymentSchedule ps = e.getSchedule();
        // Verifica a agenda quinzenal
        Assertions.assertTrue(ps instanceof BiWeeklySchedule);

        PaymentMethod pm = e.getMethod();
        Assertions.assertTrue(pm instanceof HoldMethod);
    }

    @Test
    public void testDeleteEmployee() {
        int empId = 4;
        AddCommissionedEmployee t =
                new AddCommissionedEmployee(empId, "Bill", "Home", 2500, 3.2, database);
        t.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        // Agora instanciamos a transação de deleção (que vamos criar a seguir)
        DeleteEmployeeTransaction dt =
                new DeleteEmployeeTransaction(empId, database);
        dt.execute();

        e = database.getEmployee(empId);
        Assertions.assertNull(e);
    }

    @Test
    public void testTimeCardTransaction() {
        int empId = 5;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25, database);
        t.execute();

        // LocalDate.of(ano, mes, dia) substitui o new DateTime(...)
        TimeCardTransaction tct = new TimeCardTransaction(
                LocalDate.of(2005, 7, 31), 8.0, empId, database);
        tct.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentClassification pc = e.getClassification();
        Assertions.assertTrue(pc instanceof HourlyClassification);

        HourlyClassification hc = (HourlyClassification) pc;

        TimeCard tc = hc.getTimeCard(LocalDate.of(2005, 7, 31));
        Assertions.assertNotNull(tc);
        Assertions.assertEquals(8.0, tc.getHours());
    }

    @Test
    public void testSalesReceiptTransaction() {
        int empId = 5;

        // Criando o funcionário comissionado
        AddCommissionedEmployee t = new AddCommissionedEmployee(
                empId, "Bill", "Home", 2000, 15.25, database);
        t.execute();

        // Lançando a venda (SalesReceiptTransaction ainda precisa ser criada)
        SalesReceiptTransaction tct = new SalesReceiptTransaction(
                LocalDate.of(2005, 7, 31), 250.00, empId, database);
        tct.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentClassification pc = e.getClassification();
        Assertions.assertTrue(pc instanceof CommissionedClassification);

        CommissionedClassification cc = (CommissionedClassification) pc;

        SalesReceipt sr = cc.getSalesReceipt(LocalDate.of(2005, 7, 31));
        Assertions.assertNotNull(sr);
        Assertions.assertEquals(250.00, sr.getSaleAmount(), .001);
    }
}
