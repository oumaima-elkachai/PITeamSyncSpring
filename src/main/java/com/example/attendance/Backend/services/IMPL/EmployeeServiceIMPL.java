package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.services.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceIMPL implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceIMPL(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }


    @Override
    public String getEmployeeName(String employeeId) {
        if (employeeId == null) {
            return "Employé non spécifié";
        }
        return employeeRepository.findById(employeeId)
                .map(employee -> employee.getName() != null ? employee.getName() : "Nom non fourni")
                .orElse("Employé inconnu");
    }

}
