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

    @Test
    public void testAddServiceCharge() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        // Precisamos criar esta classe: UnionAffiliation
        // Nota: Geralmente passamos a taxa sindical no construtor,
        // mas vou seguir seu código C# que usa o construtor padrão por enquanto.
        UnionAffiliation af = new UnionAffiliation();

        e.setAffiliation(af);

        int memberId = 86; // ID do membro no sindicato (Maxwell Smart)
        database.addUnionMember(memberId, e);

        // Precisamos criar esta transação: ServiceChargeTransaction
        ServiceChargeTransaction sct = new ServiceChargeTransaction(
                memberId, LocalDate.of(2005, 8, 8), 12.95, database);
        sct.execute();

        // Precisamos criar esta classe: ServiceCharge
        ServiceCharge sc = af.getServiceCharge(LocalDate.of(2005, 8, 8));

        Assertions.assertNotNull(sc);
        Assertions.assertEquals(12.95, sc.getAmount(), .001);
    }

    @Test
    public void testChangeNameTransaction() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25, database);
        t.execute();

        // Precisamos criar esta transação
        ChangeNameTransaction cnt = new ChangeNameTransaction(empId, "Bob", database);
        cnt.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);
        Assertions.assertEquals("Bob", e.getName());
    }

    @Test
    public void testChangeHourlyTransaction() {
        int empId = 3;

        // Começa como Comissionado (Agenda Quinzenal)
        AddCommissionedEmployee t = new AddCommissionedEmployee(
                empId, "Lance", "Home", 2500, 3.2, database);
        t.execute();

        // Executa a mudança para Horista
        ChangeHourlyTransaction cht = new ChangeHourlyTransaction(empId, 27.52, database);
        cht.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentClassification pc = e.getClassification();
        Assertions.assertNotNull(pc);
        Assertions.assertTrue(pc instanceof HourlyClassification);

        HourlyClassification hc = (HourlyClassification) pc;
        Assertions.assertEquals(27.52, hc.getHourlyRate(), .001);

        PaymentSchedule ps = e.getSchedule();
        // Verifica se a agenda mudou automaticamente para Semanal
        Assertions.assertTrue(ps instanceof WeeklySchedule);
    }

    @Test
    public void testChangeSalariedTransaction() {
        int empId = 4;

        // Criação inicial como Comissionado
        AddCommissionedEmployee t = new AddCommissionedEmployee(
                empId, "Lance", "Home", 2500, 3.2, database);
        t.execute();

        // Execução da mudança para Assalariado
        ChangeSalariedTransaction cst =
                new ChangeSalariedTransaction(empId, 3000.00, database);
        cst.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentClassification pc = e.getClassification();
        Assertions.assertNotNull(pc);
        Assertions.assertTrue(pc instanceof SalariedClassification);

        SalariedClassification sc = (SalariedClassification) pc;
        Assertions.assertEquals(3000.00, sc.getSalary(), .001);

        PaymentSchedule ps = e.getSchedule();

        Assertions.assertTrue(ps instanceof MonthlySchedule);
    }

    @Test
    public void testChangeCommissionedTransaction() {
        int empId = 5;
        AddSalariedEmployee t = new AddSalariedEmployee(
                empId, "Bob", "Home", 2500.00, database);
        t.execute();

        ChangeCommissionedTransaction cht = new ChangeCommissionedTransaction(
                empId, 1250.00, 5.6, database);
        cht.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentClassification pc = e.getClassification();
        Assertions.assertNotNull(pc);
        Assertions.assertTrue(pc instanceof CommissionedClassification);

        CommissionedClassification cc = (CommissionedClassification) pc;
        Assertions.assertEquals(1250.00, cc.getBaseRate(), .001);
        Assertions.assertEquals(5.6, cc.getCommissionRate(), .001);

        PaymentSchedule ps = e.getSchedule();
        Assertions.assertTrue(ps instanceof BiWeeklySchedule);
    }

    @Test
    public void testChangeDirectMethod() {
        int empId = 6;
        AddSalariedEmployee t = new AddSalariedEmployee(
                empId, "Mike", "Home", 3500.00, database);
        t.execute();

        ChangeDirectTransaction cddt = new ChangeDirectTransaction(empId, database);
        cddt.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentMethod method = e.getMethod();
        Assertions.assertNotNull(method);
        Assertions.assertTrue(method instanceof DirectDepositMethod);
    }

    @Test
    public void testChangeHoldMethod() {
        int empId = 7;
        AddSalariedEmployee t = new AddSalariedEmployee(
                empId, "Mike", "Home", 3500.00, database);
        t.execute();

        new ChangeDirectTransaction(empId, database).execute();

        ChangeHoldTransaction cht = new ChangeHoldTransaction(empId, database);
        cht.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentMethod method = e.getMethod();
        Assertions.assertNotNull(method);
        Assertions.assertInstanceOf(HoldMethod.class, method);
    }

    @Test
    public void testChangeMailMethod() {
        int empId = 8;
        AddSalariedEmployee t = new AddSalariedEmployee(
                empId, "Mike", "Home", 3500.00, database);
        t.execute();

        ChangeMailTransaction cmt = new ChangeMailTransaction(empId, database);
        cmt.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        PaymentMethod method = e.getMethod();
        Assertions.assertNotNull(method);
        Assertions.assertTrue(method instanceof MailMethod);
    }

    @Test
    public void testChangeUnionMember() {
        int empId = 9;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25, database);
        t.execute();

        int memberId = 7743;
        ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, 99.42, database);
        cmt.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        Affiliation affiliation = e.getAffiliation();
        Assertions.assertNotNull(affiliation);
        Assertions.assertTrue(affiliation instanceof UnionAffiliation);

        UnionAffiliation uf = (UnionAffiliation) affiliation;
        Assertions.assertEquals(99.42, uf.getDues(), .001);

        Employee member = database.getUnionMember(memberId);
        Assertions.assertNotNull(member);
        Assertions.assertEquals(e, member);
    }

    @Test
    public void testChangeUnaffiliatedMember() {
        int empId = 10;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25, database);
        t.execute();

        int memberId = 7743;
        new ChangeMemberTransaction(empId, memberId, 99.42, database).execute();

        ChangeUnaffiliatedTransaction cut = new ChangeUnaffiliatedTransaction(empId, database);
        cut.execute();

        Employee e = database.getEmployee(empId);
        Assertions.assertNotNull(e);

        Affiliation affiliation = e.getAffiliation();
        Assertions.assertNotNull(affiliation);
        Assertions.assertTrue(affiliation instanceof NoAffiliation);

        Employee member = database.getUnionMember(memberId);
        Assertions.assertNull(member);
    }

    @Test
    public void testPaySingleSalariedEmployee() {
        int empId = 1;
        AddSalariedEmployee t = new AddSalariedEmployee(
                empId, "Bob", "Home", 1000.00, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 30);
        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        Paycheck pc = pt.getPaycheck(empId);
        Assertions.assertNotNull(pc);
        Assertions.assertEquals(payDate, pc.getPayDate());
        Assertions.assertEquals(1000.00, pc.getGrossPay(), .001);
        Assertions.assertEquals("Hold", pc.getField("Disposition"));
        Assertions.assertEquals(0.0, pc.getDeductions(), .001);
        Assertions.assertEquals(1000.00, pc.getNetPay(), .001);
    }

    @Test
    public void testPaySingleSalariedEmployeeOnWrongDate() {
        int empId = 1;
        AddSalariedEmployee t = new AddSalariedEmployee(
                empId, "Bob", "Home", 1000.00, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 29);
        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        Paycheck pc = pt.getPaycheck(empId);
        Assertions.assertNull(pc);
    }

    @Test
    public void testPayingSingleHourlyEmployeeNoTimeCards() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Sexta-feira
        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        validatePaycheck(pt, empId, payDate, 0.0);
    }

    // Método auxiliar sugerido para evitar repetição de código nos próximos testes
    private void validatePaycheck(PaydayTransaction pt, int empId, LocalDate payDate, double pay) {
        Paycheck pc = pt.getPaycheck(empId);
        Assertions.assertNotNull(pc);
        Assertions.assertEquals(payDate, pc.getPayDate());
        Assertions.assertEquals(pay, pc.getGrossPay(), .001);
        Assertions.assertEquals("Hold", pc.getField("Disposition"));
        Assertions.assertEquals(0.0, pc.getDeductions(), .001);
        Assertions.assertEquals(pay, pc.getNetPay(), .001);
    }

    @Test
    public void testPaySingleHourlyEmployeeOneTimeCard() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Sexta-feira

        TimeCardTransaction tc = new TimeCardTransaction(payDate, 2.0, empId, database);
        tc.execute();

        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        validatePaycheck(pt, empId, payDate, 30.5);
    }

    @Test
    public void testPaySingleHourlyEmployeeOvertimeOneTimeCard() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Sexta-feira

        TimeCardTransaction tc = new TimeCardTransaction(payDate, 9.0, empId, database);
        tc.execute();

        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        validatePaycheck(pt, empId, payDate, (8 + 1.5) * 15.25);
    }

    @Test
    public void testPaySingleHourlyEmployeeOnWrongDate() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 8); // Quinta-feira

        TimeCardTransaction tc = new TimeCardTransaction(payDate, 9.0, empId, database);
        tc.execute();

        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        Paycheck pc = pt.getPaycheck(empId);
        Assertions.assertNull(pc);
    }

    @Test
    public void testPaySingleHourlyEmployeeTwoTimeCards() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Sexta-feira

        TimeCardTransaction tc = new TimeCardTransaction(payDate, 2.0, empId, database);
        tc.execute();

        TimeCardTransaction tc2 = new TimeCardTransaction(payDate.minusDays(1), 5.0, empId, database);
        tc2.execute();

        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        validatePaycheck(pt, empId, payDate, 7 * 15.25);
    }

    @Test
    public void testPaySingleHourlyEmployeeWithTimeCardsSpanningTwoPayPeriods() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(
                empId, "Bill", "Home", 15.25, database);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Sexta-feira
        LocalDate dateInPreviousPayPeriod = LocalDate.of(2001, 10, 30);

        TimeCardTransaction tc = new TimeCardTransaction(payDate, 2.0, empId, database);
        tc.execute();

        TimeCardTransaction tc2 = new TimeCardTransaction(
                dateInPreviousPayPeriod, 5.0, empId, database);
        tc2.execute();

        PaydayTransaction pt = new PaydayTransaction(payDate, database);
        pt.execute();

        validatePaycheck(pt, empId, payDate, 2 * 15.25);
    }
}
