package br.com.vitordie.payroll;

import java.util.List;

public interface PayrollDatabase {
    void addEmployee(Employee employee);

    Employee getEmployee(int id);

    void deleteEmployee(int id);

    void addUnionMember(int id, Employee e);

    Employee getUnionMember(int id);

    void removeUnionMember(int memberId);

    // Em Java, usamos List<Integer> ao invés de ArrayList cru.
    // Interfaces devem preferir retornar interfaces (List) e não implementações (ArrayList).
    List<Integer> getAllEmployeeIds();

    // Equivalente ao IList do C#, mas tipado para Employee
    List<Employee> getAllEmployees();
}
