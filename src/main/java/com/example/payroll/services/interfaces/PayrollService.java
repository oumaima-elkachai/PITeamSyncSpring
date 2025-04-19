package com.example.payroll.services.interfaces;

import com.example.payroll.model.Payroll;

import java.util.List;

public interface PayrollService {
    List<Payroll> getAllPayrolls();
    Payroll savePayroll(Payroll payroll);
    void deletePayroll(String id);
    Payroll getPayrollById(String id);
    Payroll updatePayroll(String id, Payroll payrollDetails);

}
