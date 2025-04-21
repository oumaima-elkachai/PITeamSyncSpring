package com.example.payroll.services.interfaces;

import com.example.payroll.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(String id);
    Employee saveEmployee(Employee employee);
    void deleteEmployee(String id);
    String getEmployeeNameById(String id);

}

