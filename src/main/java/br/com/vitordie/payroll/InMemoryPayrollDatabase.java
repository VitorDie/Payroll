package br.com.vitordie.payroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPayrollDatabase implements PayrollDatabase {

    // Em Java, usamos Map/HashMap com Generics no lugar de Hashtable.
    // O modificador 'static' mantém o comportamento de "Banco de Dados" global em memória.
    private static Map<Integer, Employee> employees = new HashMap<>();
    private static Map<Integer, Employee> unionMembers = new HashMap<>();

    @Override
    public void addEmployee(Employee employee) {
        // Assume-se que Employee tem um getter para o ID (getEmpId)
        employees.put(employee.getEmpId(), employee);
    }

    @Override
    public Employee getEmployee(int id) {
        // Com Generics, não precisamos fazer cast (return ... as Employee)
        return employees.get(id);
    }

    @Override
    public void deleteEmployee(int id) {
        employees.remove(id);
    }

    @Override
    public void addUnionMember(int id, Employee e) {
        unionMembers.put(id, e);
    }

    @Override
    public Employee getUnionMember(int id) {
        return unionMembers.get(id);
    }

    @Override
    public void removeUnionMember(int memberId) {
        unionMembers.remove(memberId);
    }

    @Override
    public List<Integer> getAllEmployeeIds() {
        // keyset() retorna um Set, passamos para o construtor do ArrayList
        return new ArrayList<>(employees.keySet());
    }

    @Override
    public List<Employee> getAllEmployees() {
        // values() retorna uma Collection, passamos para o construtor do ArrayList
        return new ArrayList<>(employees.values());
    }

    // Método extra (não estava na interface original, mas útil para testes)
    public void clear() {
        employees.clear();
        unionMembers.clear();
    }
}