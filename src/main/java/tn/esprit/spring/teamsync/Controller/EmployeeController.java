package tn.esprit.spring.teamsync.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.teamsync.Entity.Employee;
import tn.esprit.spring.teamsync.Entity.Project;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Services.Interfaces.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Employee>> getEmployeesByProject(@PathVariable String projectId) {
        List<Employee> employees = employeeService.getEmployeesByProject(projectId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/upcoming-deadlines")
    public ResponseEntity<List<Task>> getUpcomingDeadlines(
            @PathVariable String employeeId,
            @RequestParam(defaultValue = "7") int daysAhead
    ) {
        List<Task> tasks = employeeService.getUpcomingDeadlines(employeeId, daysAhead);
        return ResponseEntity.ok(tasks);
    }


    // Create a new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // Update an employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        if (updatedEmployee != null) {
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        boolean isDeleted = employeeService.deleteEmployee(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get employee's assigned tasks
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getEmployeeAssignedTasks(@PathVariable String id) {
        List<Task> tasks = employeeService.getEmployeeAssignedTasks(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Get employee's projects
   // @GetMapping("/{id}/projects")
    //public ResponseEntity<List<Project>> getEmployeeProjects(@PathVariable String id) {
      //  List<Project> projects = employeeService.getEmployeeProjects(id);
        //return new ResponseEntity<>(projects, HttpStatus.OK);
    //}

    // Assign a task to an employee
    @PostMapping("/{employeeId}/tasks/{taskId}")
    public ResponseEntity<Void> assignTaskToEmployee(@PathVariable String employeeId, @PathVariable String taskId) {
        boolean isAssigned = employeeService.assignTaskToEmployee(employeeId, taskId);
        if (isAssigned) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add an employee to a project
    @PostMapping("/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> addEmployeeToProject(@PathVariable String employeeId, @PathVariable String projectId) {
        boolean isAdded = employeeService.addEmployeeToProject(employeeId, projectId);
        if (isAdded) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}