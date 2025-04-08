package com.mg.hr.bl.managers;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.dto.*;
import com.mg.hr.dl.dao.*;
import java.util.*;
import java.math.*;
import com.mg.enums.*;

public class EmployeeManager implements EmployeeManagerInterface
{
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
private Set<EmployeeInterface> employeesSet;
private static EmployeeManager employeeManager=null;
private EmployeeManager() throws BLException
{
populateDataStructures();
}
private void populateDataStructures() throws BLException
{
this.employeeIdWiseEmployeesMap=new HashMap<>();
this.panNumberWiseEmployeesMap=new HashMap<>();
this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
this.designationCodeWiseEmployeesMap=new HashMap<>();
this.employeesSet=new TreeSet<>();
Set<EmployeeInterface> ets;
DesignationInterface designation;
try
{
Set<EmployeeDTOInterface> dlEmployees;
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface blEmployee;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
for(EmployeeDTOInterface dlEmployee:dlEmployees)
{
blEmployee=new Employee();
blEmployee.setEmployeeId(dlEmployee.getEmployeeId());
blEmployee.setName(dlEmployee.getName());
designation=designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
blEmployee.setDesignation(designation);
blEmployee.setDateOfBirth(dlEmployee.getDateOfBirth());
if(dlEmployee.getGender()=='M') blEmployee.setGender(GENDER.MALE);
else blEmployee.setGender(GENDER.FEMALE);
blEmployee.setIsIndian(dlEmployee.getIsIndian());
blEmployee.setBasicSalary(dlEmployee.getBasicSalary());
blEmployee.setPANNumber(dlEmployee.getPANNumber());
blEmployee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
this.employeeIdWiseEmployeesMap.put(blEmployee.getEmployeeId().toUpperCase(),blEmployee);
this.panNumberWiseEmployeesMap.put(blEmployee.getPANNumber().toUpperCase(),blEmployee);
this.aadharCardNumberWiseEmployeesMap.put(blEmployee.getAadharCardNumber().toUpperCase(),blEmployee);
this.employeesSet.add(blEmployee);
ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(blEmployee);
this.designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
}
else
{
ets.add(blEmployee);
}
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public static EmployeeManagerInterface getEmployeeManager() throws BLException
{
if(employeeManager==null) employeeManager=new EmployeeManager();
return employeeManager;
}
public void addEmployee(EmployeeInterface employee)throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is Required");
throw blException;
}
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
int designationCode=0;
if(employeeId!=null)
{
employeeId=employeeId.trim();
if(employeeId.length()>0) blException.addException("Employee Id","Employee Id Sholud Be nil/empty");
}
if(name==null) blException.addException("name","Employee Name Required");
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","Employee Name Required");
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designation==null) blException.addException("Designation","Designation is Required");
else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.addException("designation","Invalid Designation");
}
}
if(dateOfBirth==null) blException.addException("Date of Birth","Date of Birth is Required");
if(gender==' ') blException.addException("gender","Gender Required");
if(basicSalary==null) blException.addException("Basic Salary","Basic Salary is Required");
else if(basicSalary.signum()==-1) blException.addException("Basic Salary","Basic Salary Cannnot Be Negative");
if(panNumber==null) blException.addException("PAN Number","PAN Number is Required");
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0) blException.addException("PAN Number","PAN Number is Required");
}
if(aadharCardNumber==null) blException.addException("Aadhar Card Number","Aadhar Card Number is Required");
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) blException.addException("Aadhar Card Number","Aadhar Card Number is Required");
}
if(panNumber!=null && panNumber.length()>0)
{
if(this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())) blException.addException("panNumber","PAN Number ("+panNumber+") Already Exists..");
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
if(this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase())) blException.addException("aadharCardNumber","Aadhar Card Number ("+aadharCardNumber+") Already Exists..");
}
if(blException.hasExceptions()) throw blException;
try
{
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth(dateOfBirth);
dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.add(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(dlEmployee.getEmployeeId());
dsEmployee.setName(name);
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designationCode));
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);
employeesSet.add(dsEmployee);
employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
this.designationCodeWiseEmployeesMap.put(designationCode,ets);
}
else
{
ets.add(dsEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void updateEmployee(EmployeeInterface employee)throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is Required");
throw blException;
}
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
int designationCode=0;
if(employeeId==null) blException.addException("Employee Id","Employee Id Required");
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0) blException.addException("Employee Id","Employee Id Required");
}
if(name==null) blException.addException("name","Employee Name Required");
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","Employee Name Required");
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designation==null) blException.addException("Designation","Designation is Required");
else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.addException("designation","Invalid Designation");
}
}
if(dateOfBirth==null) blException.addException("Date of Birth","Date of Birth is Required");
if(gender==' ') blException.addException("gender","Gender Required");
if(basicSalary==null) blException.addException("Basic Salary","Basic Salary is Required");
else if(basicSalary.signum()==-1) blException.addException("Basic Salary","Basic Salary Cannnot Be Negative");
if(panNumber==null) blException.addException("PAN Number","PAN Number is Required");
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0) blException.addException("PAN Number","PAN Number is Required");
}
if(aadharCardNumber==null) blException.addException("Aadhar Card Number","Aadhar Card Number is Required");
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) blException.addException("Aadhar Card Number","Aadhar Card Number is Required");
}
EmployeeInterface emp=null;
if(employeeId!=null && employeeId.length()>0)
{
if(this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false) blException.addException("employeeId","Cannot find this Employee Id.");
else
{
emp=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
}
}
if(panNumber!=null && panNumber.length()>0)
{
if(this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
EmployeeInterface tmp=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(emp!=tmp) blException.addException("panNumber","PAN Number ("+panNumber+") Exists Against Another Employee");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
if(this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase())) 
{
EmployeeInterface tmp=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(emp!=tmp) blException.addException("aadharCardNumber","Aadhar Card Number ("+aadharCardNumber+") Exists Against Another Employee");
}
}
if(blException.hasExceptions()) throw blException;
try
{
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
dlEmployee.setEmployeeId(employeeId);
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth(dateOfBirth);
dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.update(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(dlEmployee.getEmployeeId());
dsEmployee.setName(name);
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designationCode));
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);

Set<EmployeeInterface> ets;

//Remove From DS
employeesSet.remove(emp);
employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(emp.getPANNumber().toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(emp.getAadharCardNumber().toUpperCase());
ets=this.designationCodeWiseEmployeesMap.get(designationCode);
for(EmployeeInterface e:ets)
{
if(e.getEmployeeId().equalsIgnoreCase(employeeId))
{
ets.remove(e);
}
}

//add to ds
employeesSet.add(dsEmployee);
employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
ets=this.designationCodeWiseEmployeesMap.get(designationCode);
ets.add(dsEmployee);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void removeEmployee(String employeeId)throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.addException("Employee Id","Employee Id Required");
throw blException;
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0) blException.addException("Employee Id","Employee Id Required");
}
if(employeeId!=null && employeeId.length()>0)
{
if(this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false) blException.addException("employeeId","Cannot find this Employee Id.");
}
if(blException.hasExceptions()) throw blException;
try
{
EmployeeInterface emp=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);
//Remove From DS
employeesSet.remove(emp);
employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(emp.getPANNumber().toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(emp.getAadharCardNumber().toUpperCase());
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(emp.getDesignation().getCode());
for(EmployeeInterface e:ets)
{
if(e.getEmployeeId().equalsIgnoreCase(employeeId))
{
ets.remove(e);
}
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public EmployeeInterface getEmployeeByEmployeeId(String employeeId)throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.setGenericException("Employee Id Required");
throw blException;
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0) 
{
blException.addException("EmployeeId","Employee Id Required");
throw blException;
}
}
EmployeeInterface employee=new Employee();
EmployeeInterface dsEmployee;
dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
EmployeeInterface dlEmployee=new Employee();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(dsEmployee.getName());
dlEmployee.setDesignation(dsEmployee.getDesignation());
dlEmployee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
dlEmployee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(dsEmployee.getIsIndian());
dlEmployee.setBasicSalary(dsEmployee.getBasicSalary());
dlEmployee.setPANNumber(dsEmployee.getPANNumber());
dlEmployee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
return dlEmployee;
}
public EmployeeInterface getEmployeeByPANNumber(String panNumber)throws BLException
{
BLException blException=new BLException();
if(panNumber==null)
{
blException.setGenericException("PAN Number Required");
throw blException;
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("panNumber","PAN Number Required");
throw blException;
}
}
EmployeeInterface employee=new Employee();
EmployeeInterface dsEmployee;
dsEmployee=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
EmployeeInterface dlEmployee=new Employee();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(dsEmployee.getName());
dlEmployee.setDesignation(dsEmployee.getDesignation());
dlEmployee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
dlEmployee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(dsEmployee.getIsIndian());
dlEmployee.setBasicSalary(dsEmployee.getBasicSalary());
dlEmployee.setPANNumber(dsEmployee.getPANNumber());
dlEmployee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
return dlEmployee;
}
public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber)throws BLException
{
BLException blException=new BLException();
if(aadharCardNumber==null)
{
blException.setGenericException("Aadhar Card Number Required");
throw blException;
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) 
{
blException.addException("aadharCardNumber","Aadhar Card Number Required");
throw blException;
}
}
EmployeeInterface employee=new Employee();
EmployeeInterface dsEmployee;
dsEmployee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
EmployeeInterface dlEmployee=new Employee();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(dsEmployee.getName());
dlEmployee.setDesignation(dsEmployee.getDesignation());
dlEmployee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
dlEmployee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(dsEmployee.getIsIndian());
dlEmployee.setBasicSalary(dsEmployee.getBasicSalary());
dlEmployee.setPANNumber(dsEmployee.getPANNumber());
dlEmployee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
return dlEmployee;
}
public boolean employeeIdExists(String employeeId)
{
return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
}
public boolean employeePANNumberExists(String panNumber)
{
return this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
}
public boolean employeeAadharCardNumberExists(String aadharCardNumber)
{
return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
}
public int getEmployeeCount()
{
return this.employeesSet.size();
}
public Set<EmployeeInterface> getEmployees()throws BLException
{
Set<EmployeeInterface> employees;
employees=new TreeSet<>();
for(EmployeeInterface dsEmployee:this.employeesSet)
{
EmployeeInterface dlEmployee=new Employee();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(dsEmployee.getName());
dlEmployee.setDesignation(dsEmployee.getDesignation());
dlEmployee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
dlEmployee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(dsEmployee.getIsIndian());
dlEmployee.setBasicSalary(dsEmployee.getBasicSalary());
dlEmployee.setPANNumber(dsEmployee.getPANNumber());
dlEmployee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
employees.add(dlEmployee);
}
return employees;
}
public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode)throws 
BLException
{
Set<EmployeeInterface> employees=new TreeSet<>();
int count=0;
BLException blException=new BLException();
if(designationCode<=0)
{
blException.setGenericException("Invalid Code");
throw blException;
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.setGenericException("Invalid Code");
throw blException;
}
for(EmployeeInterface dsEmployee:this.employeesSet)
{
if(dsEmployee.getDesignation().getCode()==designationCode)
{
EmployeeInterface dlEmployee=new Employee();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(dsEmployee.getName());
dlEmployee.setDesignation(dsEmployee.getDesignation());
dlEmployee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
dlEmployee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(dsEmployee.getIsIndian());
dlEmployee.setBasicSalary(dsEmployee.getBasicSalary());
dlEmployee.setPANNumber(dsEmployee.getPANNumber());
dlEmployee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
employees.add(dlEmployee);
}
}
return employees;
}
public int getEmployeeCountByDesignationCode(int designationCode)
{
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null) return 0;
return ets.size();
}
public boolean designationAlloted(int designationCode)
{
return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
}
}