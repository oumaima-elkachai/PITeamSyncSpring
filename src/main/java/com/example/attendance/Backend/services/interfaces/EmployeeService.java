package com.example.attendance.Backend.services.interfaces;

import com.example.attendance.Backend.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(String id);
    Employee saveEmployee(Employee employee);
    void deleteEmployee(String id);
    String getEmployeeName(String employeeId); // Add this method


}