package com.mg.hr.bl.interfaces.managers;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.exceptions.*;
import java.util.*;
public interface EmployeeManagerInterface
{
public void addEmployee(EmployeeInterface employee)throws BLException;
public void updateEmployee(EmployeeInterface employee)throws BLException;
public void removeEmployee(String employeeId)throws BLException;
public EmployeeInterface getEmployeeByEmployeeId(String EmplyeeId)throws BLException;
public EmployeeInterface getEmployeeByPANNumber(String panNumber)throws BLException;
public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber)throws BLException;
public boolean employeeIdExists(String employeeId);
public boolean employeePANNumberExists(String panNumber);
public boolean employeeAadharCardNumberExists(String aadharCardNumber);
public int getEmployeeCount();
public Set<EmployeeInterface> getEmployees()throws BLException;
public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode)throws BLException;
public int getEmployeeCountByDesignationCode(int designationCode);
public boolean designationAlloted(int designationCode);
}