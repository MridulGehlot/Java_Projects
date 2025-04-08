package com.mg.hr.dl.dao;
import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.mg.enums.*;
import java.sql.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("No Employee Found To Be Added");
String name=employeeDTO.getName();
if(name==null || name.trim().length()==0) throw new DAOException("Name is NULL");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("This Designation Doesn't Exists");
Connection connection=null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection=DAOConnection.getConnection();
preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Code");
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Date Of Birth Is NULL");
}
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
 throw new DAOException("Basic Salary is Null");
}
if(basicSalary.signum()==-1) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
} 
throw new DAOException("Basic Salary Is Negative");
}
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null || panNumber.trim().length()==0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
 throw new DAOException("PAN Number is NULL");
}
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null || aadharCardNumber.trim().length()==0) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar Card Number Is NULL");
}
try
{
boolean panNumberExists;
preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
resultSet=preparedStatement.executeQuery();
panNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
boolean aadharCardNumberExists;
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet=preparedStatement.executeQuery();
aadharCardNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(panNumberExists && aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("PAN Number ("+panNumber+") exists and Aadhar Card Number ("+aadharCardNumber+") Exists.");
}
if(panNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("PAN Number ("+panNumber+") exists.");
}
if(aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar Card Number ("+aadharCardNumber+") Exists.");
}
preparedStatement=connection.prepareStatement("insert into employee (name,designation_code,date_of_birth,basic_salary,gender,is_indian,pan_number,aadhar_number) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDate=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDate);
preparedStatement.setBigDecimal(4,basicSalary);
preparedStatement.setString(5,String.valueOf(gender));
preparedStatement.setBoolean(6,isIndian);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.executeUpdate();
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedEmployeeId=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
employeeDTO.setEmployeeId("A"+(1000000+generatedEmployeeId));
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("No Employee Found To Be Added");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("Employee Id. Required");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Employee Id. Required");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
throw new DAOException("Invalid Id.");
}
String name=employeeDTO.getName();
if(name==null || name.trim().length()==0) throw new DAOException("Name is NULL");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("This Designation Doesn't Exists");
Connection connection=null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection=DAOConnection.getConnection();
preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Code");
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Date Of Birth Is NULL");
}
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
 throw new DAOException("Basic Salary is Null");
}
if(basicSalary.signum()==-1) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
} 
throw new DAOException("Basic Salary Is Negative");
}
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null || panNumber.trim().length()==0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
 throw new DAOException("PAN Number is NULL");
}
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null || aadharCardNumber.trim().length()==0) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar Card Number Is NULL");
}
try
{
boolean panNumberExists;
preparedStatement=connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?");
preparedStatement.setString(1,panNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
panNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
boolean aadharCardNumberExists;
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=? and employee_id<>?");
preparedStatement.setString(1,aadharCardNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
aadharCardNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(panNumberExists && aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("PAN Number ("+panNumber+") exists and Aadhar Card Number ("+aadharCardNumber+") Exists.");
}
if(panNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("PAN Number ("+panNumber+") exists.");
}
if(aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar Card Number ("+aadharCardNumber+") Exists.");
}
preparedStatement=connection.prepareStatement("update employee set name=?,designation_code=?,date_of_birth=?,basic_salary=?,gender=?,is_indian=?,pan_number=?,aadhar_number=? where employee_id=?");
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDate=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDate);
preparedStatement.setBigDecimal(4,basicSalary);
preparedStatement.setString(5,String.valueOf(gender));
preparedStatement.setBoolean(6,isIndian);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.setInt(9,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Employee Id. Required");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Employee Id. Required");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
throw new DAOException("Invalid Id.");
}
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Employee Id.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<>();
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select * from employee");
EmployeeDTOInterface employeeDTO;
java.util.Date dateOfBirth;
java.sql.Date sqlDateOfBirth;
char gender;
while(resultSet.next())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=(resultSet.getString("gender")).charAt(0);
if(gender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
employees.add(employeeDTO);
}
resultSet.close();
statement.close();
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
return employees;
}
public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
if(designationCode<=0)throw new DAOException("Invalid designation code = "+designationCode);
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<>();
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Designation Code ="+designationCode);
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select * from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.util.Date dateOfBirth;
java.sql.Date sqlDateOfBirth;
char gender;
while(resultSet.next())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=(resultSet.getString("gender")).charAt(0);
if(gender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
employees.add(employeeDTO);
}
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
return employees;
}
public boolean isDesignationAlloted(int designationCode) throws DAOException
{
if(designationCode<=0) throw new DAOException("Invalid Code = "+designationCode);
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Designation Code ="+designationCode);
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
boolean designationAlloted=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return designationAlloted;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null || employeeId.trim().length()==0) throw new DAOException("Employee Id Is Null");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception e)
{
throw new DAOException("Invalid Employee ID.");
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Employee Id.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select * from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.util.Date dateOfBirth;
java.sql.Date sqlDateOfBirth;
String gender;
resultSet.next();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
if(gender.equals("M")) employeeDTO.setGender(GENDER.MALE);
if(gender.equals("F")) employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null)throw new DAOException("PAN Number Is NULL");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("PAN Number Is NULL");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid PAN Number");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select * from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.util.Date dateOfBirth;
java.sql.Date sqlDateOfBirth;
String gender;
resultSet.next();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
if(gender.equals("M")) employeeDTO.setGender(GENDER.MALE);
if(gender.equals("F")) employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null)throw new DAOException("Aadhar Card Number Is NULL");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Aadhar Card Number Is NULL");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Aadhar Card Number");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select * from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.util.Date dateOfBirth;
java.sql.Date sqlDateOfBirth;
String gender;
resultSet.next();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
if(gender.equals("M")) employeeDTO.setGender(GENDER.MALE);
if(gender.equals("F")) employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null)throw new DAOException("Employee Id Is NULL");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("Employee Id Is NULL");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception e)
{
throw new DAOException("Invalid Employee ID.");
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
boolean exists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public boolean panNumberExists(String panNumber) throws DAOException
{
if(panNumber==null)throw new DAOException("PAN Number Is NULL");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("PAN Number Is NULL");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
boolean exists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public boolean aadharNumberExists(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null)throw new DAOException("Aadhar Card Number Is NULL");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Aadhar Card Number Is NULL");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
boolean exists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public int getCount() throws DAOException
{
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select count(*) from employee");
resultSet.next();
int count=resultSet.getInt(1);
resultSet.close();
statement.close();
connection.close();
return count;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
public int getCountByDesignation(int designationCode) throws DAOException
{
if(designationCode<=0) throw new DAOException("Invalid Code "+designationCode);
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Designation Code");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select count(*) from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
int count=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
return count;
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
}
}