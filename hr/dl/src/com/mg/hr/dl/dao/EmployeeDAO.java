package com.mg.hr.dl.dao;
import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.math.*;
import com.mg.enums.*;

public class EmployeeDAO implements EmployeeDAOInterface
{
private static final String FILE_NAME="employee.data";
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("No Employee Found To Be Added");
String employeeId=employeeDTO.getEmployeeId();
String name=employeeDTO.getName();
if(name==null || name.trim().length()==0) throw new DAOException("Name is NULL");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("This Designation Doesn't Exists");
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationValid=designationDAO.codeExists(designationCode);
if(!isDesignationValid) throw new DAOException("This Designation Doesn't Exists");
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Date Of Birth Is NULL");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Basic Salary is Null");
if(basicSalary.signum()==-1) throw new DAOException("Basic Salary Is Negative");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null || panNumber.trim().length()==0) throw new DAOException("PAN Number is NULL");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null || aadharCardNumber.trim().length()==0) throw new DAOException("Aadhar Card Number Is NULL");
try
{
int lastGeneratedCode,recordCount;
String lastGeneratedCodeString,recordCountString;
lastGeneratedCode=10000000;
recordCount=0;
lastGeneratedCodeString="";
recordCountString="";
File f=new File(FILE_NAME);
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0) 
{
lastGeneratedCodeString=String.format("%-10d",lastGeneratedCode);
recordCountString=String.format("%-10d",recordCount);
raf.writeBytes(lastGeneratedCodeString+"\n");
raf.writeBytes(recordCountString+"\n");
}
else 
{
lastGeneratedCode=Integer.parseInt(raf.readLine().trim());
recordCount=Integer.parseInt(raf.readLine().trim());
}
String fpanNumber,faadharCardNumber;
boolean panNumberExists=false;
boolean aadharCardNumberExists=false;
while(raf.getFilePointer()<raf.length())
{
for(int i=0;i<7;i++) raf.readLine();
fpanNumber=raf.readLine();
faadharCardNumber=raf.readLine();
if(panNumberExists==false && fpanNumber.equalsIgnoreCase(panNumber)) panNumberExists=true;
if(aadharCardNumberExists==false && faadharCardNumber.equalsIgnoreCase(aadharCardNumber)) aadharCardNumberExists=true;
if(panNumberExists && aadharCardNumberExists) break;
}
if(panNumberExists && aadharCardNumberExists)
{
raf.close();
throw new DAOException("PAN Number ("+panNumber+") exists and Aadhar Card Number ("+aadharCardNumber+") Exists.");
}
if(panNumberExists)
{
raf.close();
throw new DAOException("PAN Number ("+panNumber+") exists.");
}
if(aadharCardNumberExists)
{
raf.close();
throw new DAOException("Aadhar Card Number ("+aadharCardNumber+") Exists.");
}
lastGeneratedCode++;
recordCount++;
employeeId='A'+String.valueOf(lastGeneratedCode);
raf.writeBytes(employeeId+"\n");
raf.writeBytes(name+"\n");
raf.writeBytes(designationCode+"\n");
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String dob=sdf.format(dateOfBirth);
raf.writeBytes(dob+"\n");
raf.writeBytes(gender+"\n");
raf.writeBytes(isIndian+"\n");
raf.writeBytes(basicSalary.toPlainString()+"\n");
raf.writeBytes(panNumber+"\n");
raf.writeBytes(aadharCardNumber+"\n");
raf.seek(0);
lastGeneratedCodeString=String.format("%-10d",lastGeneratedCode);
recordCountString=String.format("%-10d",recordCount);
raf.writeBytes(lastGeneratedCodeString+"\n");
raf.writeBytes(recordCountString+"\n");
raf.close();
employeeDTO.setEmployeeId(employeeId);
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("No Employee Found To Be Updated");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null || employeeId.trim().length()==0) throw new DAOException("No Employee Id Found To Be Updated");
String name=employeeDTO.getName();
if(name==null || name.trim().length()==0) throw new DAOException("Name is NULL");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("This Designation Doesn't Exists");
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationValid=designationDAO.codeExists(designationCode);
if(!isDesignationValid) throw new DAOException("This Designation Doesn't Exists");
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Date Of Birth Is NULL");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Basic Salary is Null");
if(basicSalary.signum()==-1) throw new DAOException("Basic Salary Is Negative");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null || panNumber.trim().length()==0) throw new DAOException("PAN Number is NULL");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null || aadharCardNumber.trim().length()==0) throw new DAOException("Aadhar Card Number Is NULL");
try
{
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No Data Found");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("No Data Found To Be Updated");
}
String fEmployeeId,dob;
String fPANNumber,fAadharCardNumber;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
boolean found=false;
raf.readLine();
raf.readLine();
while(raf.getFilePointer()<raf.length())
{
fEmployeeId=raf.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId)) found=true;
for(int i=1;i<=6;i++) raf.readLine();
fPANNumber=raf.readLine();
if(fPANNumber.equalsIgnoreCase(panNumber))
{
if(fEmployeeId.equalsIgnoreCase(employeeId)); //do nothing
else
{
raf.close();
throw new DAOException("Sorry Cannot Update This PAN Number Exists Against Other Employee");
}
}
fAadharCardNumber=raf.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
if(fEmployeeId.equalsIgnoreCase(employeeId)); //do nothing
else
{
raf.close();
throw new DAOException("Sorry Cannot Update This Aadhar Card Number Exists Against Other Employee");
}
}
}
if(!found)
{
raf.close();
throw new DAOException("Cannot Find This Employee Id");
}
raf.seek(0);
File ftmp=new File("tmp.tmp");
if(ftmp.exists()) ftmp.delete();
RandomAccessFile tmp=new RandomAccessFile(ftmp,"rw");
tmp.writeBytes(raf.readLine()+"\n");
tmp.writeBytes(raf.readLine()+"\n");
while(raf.getFilePointer()<raf.length())
{
fEmployeeId=raf.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
tmp.writeBytes(fEmployeeId+"\n");
tmp.writeBytes(name+"\n");
tmp.writeBytes(designationCode+"\n");
dob=sdf.format(dateOfBirth);
tmp.writeBytes(dob+"\n");
tmp.writeBytes(gender+"\n");
tmp.writeBytes(isIndian+"\n");
tmp.writeBytes(basicSalary.toPlainString()+"\n");
tmp.writeBytes(panNumber+"\n");
tmp.writeBytes(aadharCardNumber+"\n");
for(int i=1;i<=8;i++) raf.readLine();
}
else 
{
tmp.writeBytes(fEmployeeId+"\n");
for(int i=1;i<=8;i++) tmp.writeBytes(raf.readLine()+"\n");
}
}
raf.seek(0);
tmp.seek(0);
while(tmp.getFilePointer()<tmp.length())
{
raf.writeBytes(tmp.readLine()+"\n");
}
raf.setLength(tmp.length());
tmp.setLength(0);
tmp.close();
raf.close();
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Employee Id Is NULL");
if(employeeId.length()==0) throw new DAOException("No Employee Id Found To Be Deleted");
try
{
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No Data Found");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("No Data Found To Be Deleted");
}
int recordCount;
String fEmployeeId;
String recordCountString;
boolean found=false;
long foundAt=0;
raf.readLine();
recordCount=Integer.parseInt(raf.readLine().trim());
while(raf.getFilePointer()<raf.length())
{
foundAt=raf.getFilePointer();
fEmployeeId=raf.readLine();
for(int i=1;i<=8;i++) raf.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId)) 
{
found=true;
break;
}
}
if(!found)
{
raf.close();
throw new DAOException("Cannot Find This Employee Id");
}
File ftmp=new File("tmp.tmp");
if(ftmp.exists()) ftmp.delete();
RandomAccessFile tmp=new RandomAccessFile(ftmp,"rw");
while(raf.getFilePointer()<raf.length())
{
tmp.writeBytes(raf.readLine()+"\n");
}
recordCount--;
recordCountString=String.format("%-10d",recordCount);
raf.seek(0);
raf.readLine();
raf.writeBytes(recordCountString+"\n");
raf.seek(foundAt);
tmp.seek(0);
while(tmp.getFilePointer()<tmp.length())
{
raf.writeBytes(tmp.readLine()+"\n");
}
raf.setLength(raf.getFilePointer());
tmp.setLength(0);
tmp.close();
raf.close();
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<>();
try
{
File f=new File(FILE_NAME);
if(f.exists()==false) return employees;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0) 
{
raf.close();
return employees;
}
raf.readLine();
raf.readLine();
String employeeId,name;
int designationCode;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String dob;
Date dateOfBirth=null;
char gender;
boolean isIndian;
BigDecimal basicSalary=null;
String panNumber,aadharCardNumber;
while(raf.getFilePointer()<raf.length())
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(raf.readLine());
employeeDTO.setName(raf.readLine());
designationCode=Integer.parseInt(raf.readLine());
employeeDTO.setDesignationCode(designationCode);
dob=raf.readLine();
try
{
dateOfBirth=sdf.parse(dob);
}catch(ParseException pe)
{
raf.close();
throw new DAOException(pe.getMessage());
}
employeeDTO.setDateOfBirth(dateOfBirth);
gender=raf.readLine().charAt(0);
if(gender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
isIndian=Boolean.parseBoolean(raf.readLine());
employeeDTO.setIsIndian(isIndian);
basicSalary=new BigDecimal(raf.readLine());
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(raf.readLine());
employeeDTO.setAadharCardNumber(raf.readLine());
employees.add(employeeDTO);
}
raf.close();
return employees;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
if(designationCode<=0)throw new DAOException("Invalid designation code = "+designationCode);
DesignationDAO designationDAO=new DesignationDAO();
boolean isDesignationValid=designationDAO.codeExists(designationCode);
if(isDesignationValid==false) throw new DAOException("Invalid designation code = "+designationCode);
try
{
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<>();
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0) 
{
raf.close();
return employees;
}
raf.readLine();
raf.readLine();
String employeeId,name;
int fdesignationCode;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String dob;
Date dateOfBirth=null;
char gender;
boolean isIndian;
BigDecimal basicSalary=null;
String panNumber,aadharCardNumber;
while(raf.getFilePointer()<raf.length())
{
employeeId=raf.readLine();
name=raf.readLine();
fdesignationCode=Integer.parseInt(raf.readLine());
if(fdesignationCode==designationCode)
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(employeeId);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
dob=raf.readLine();
try
{
dateOfBirth=sdf.parse(dob);
}catch(ParseException pe)
{
raf.close();
throw new DAOException(pe.getMessage());
}
employeeDTO.setDateOfBirth(dateOfBirth);
gender=raf.readLine().charAt(0);
if(gender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
isIndian=Boolean.parseBoolean(raf.readLine());
employeeDTO.setIsIndian(isIndian);
basicSalary=new BigDecimal(raf.readLine());
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(raf.readLine());
employeeDTO.setAadharCardNumber(raf.readLine());
employees.add(employeeDTO);
}
else for(int i=1;i<=6;i++) raf.readLine();
}
raf.close();
return employees;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public boolean isDesignationAlloted(int designationCode) throws DAOException
{
if(designationCode<=0) throw new DAOException("Invalid Code = "+designationCode);
DesignationDAOInterface designaitonDAO=new DesignationDAO();
boolean isValid=designaitonDAO.codeExists(designationCode);
if(isValid==false)throw new DAOException("Invalid Code = "+designationCode);
try
{
File f=new File(FILE_NAME);
if(f.exists()==false) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(f,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int fdesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fdesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fdesignationCode==designationCode)
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null || employeeId.trim().length()==0) throw new DAOException("Employee Id Is Null");
try
{
EmployeeDTOInterface employeeDTO=null;
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0) 
{
raf.close();
throw new DAOException("No Data Exists");
}
raf.readLine();
raf.readLine();
String femployeeId,fname;
int fdesignationCode;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String dob;
Date dateOfBirth=null;
char fgender;
boolean fisIndian;
BigDecimal basicSalary=null;
String fpanNumber,faadharCardNumber;
boolean found=false;
while(raf.getFilePointer()<raf.length())
{
femployeeId=raf.readLine();
if(femployeeId.equalsIgnoreCase(employeeId)) found=true;
if(!found) for(int i=1;i<=8;i++) raf.readLine();
else
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(femployeeId);
employeeDTO.setName(raf.readLine());
fdesignationCode=Integer.parseInt(raf.readLine());
employeeDTO.setDesignationCode(fdesignationCode);
dob=raf.readLine();
try
{
dateOfBirth=sdf.parse(dob);
}catch(ParseException pe)
{
raf.close();
throw new DAOException(pe.getMessage());
}
employeeDTO.setDateOfBirth(dateOfBirth);
fgender=raf.readLine().charAt(0);
if(fgender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
fisIndian=Boolean.parseBoolean(raf.readLine());
employeeDTO.setIsIndian(fisIndian);
basicSalary=new BigDecimal(raf.readLine());
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(raf.readLine());
employeeDTO.setAadharCardNumber(raf.readLine());
break;
}
}
raf.close();
if(found==false)
{
throw new DAOException("No Record Found");
}
return employeeDTO;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null)throw new DAOException("PAN Number Is NULL");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("PAN Number Is NULL");
try
{
String fpanNumber="";
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("No data Exists");
}
raf.readLine();
raf.readLine();
boolean found=false;
String femployeeId="";
String fname="";
String fdateOfBirth="";
int fdesignationCode=0;
Date dateOfBirth=null;
char fgender='M';
boolean fisIndian=false;
BigDecimal basicSalary=null;
String faadharCardNumber="";
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
while(raf.getFilePointer()<raf.length())
{
femployeeId=raf.readLine();
fname=raf.readLine();
fdesignationCode=Integer.parseInt(raf.readLine());
fdateOfBirth=raf.readLine();
try
{
dateOfBirth=sdf.parse(fdateOfBirth);
}catch(ParseException pe)
{
raf.close();
throw new DAOException(pe.getMessage());
}
fgender=raf.readLine().charAt(0);
fisIndian=Boolean.parseBoolean(raf.readLine());
basicSalary=new BigDecimal(raf.readLine());
fpanNumber=raf.readLine();
faadharCardNumber=raf.readLine();
if(fpanNumber.equalsIgnoreCase(panNumber))
{
raf.close();
found=true;
break;
}
}
if(!found)
{
raf.close();
throw new DAOException("No Record Found.");
}
raf.close();
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(femployeeId);
employeeDTO.setName(fname);
employeeDTO.setDesignationCode(fdesignationCode);
employeeDTO.setDateOfBirth(dateOfBirth);
if(fgender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(fisIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(fpanNumber);
employeeDTO.setAadharCardNumber(faadharCardNumber);
return employeeDTO;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null)throw new DAOException("Aadhar Card Number Is NULL");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Aadhar Card Number Is NULL");
try
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
String faadharCardNumber="";
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("No data Exists");
}
raf.readLine();
raf.readLine();
boolean found=false;
String femployeeId="";
String fname="";
String fdateOfBirth="";
int fdesignationCode=0;
Date dateOfBirth=null;
char fgender='F';
boolean fisIndian=false;
BigDecimal basicSalary=null;
String fpanNumber="";
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
while(raf.getFilePointer()<raf.length())
{
femployeeId=raf.readLine();
fname=raf.readLine();
fdesignationCode=Integer.parseInt(raf.readLine());
fdateOfBirth=raf.readLine();
try
{
dateOfBirth=sdf.parse(fdateOfBirth);
}catch(ParseException pe)
{
raf.close();
throw new DAOException(pe.getMessage());
}
fgender=raf.readLine().charAt(0);
fisIndian=Boolean.parseBoolean(raf.readLine());
basicSalary=new BigDecimal(raf.readLine());
fpanNumber=raf.readLine();
faadharCardNumber=raf.readLine();
if(faadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
raf.close();
found=true;
break;
}
}
if(!found)
{
raf.close();
throw new DAOException("No Record Found.");
}
raf.close();
employeeDTO.setEmployeeId(femployeeId);
employeeDTO.setName(fname);
employeeDTO.setDesignationCode(fdesignationCode);
employeeDTO.setDateOfBirth(dateOfBirth);
if(fgender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(fisIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(fpanNumber);
employeeDTO.setAadharCardNumber(faadharCardNumber);
return employeeDTO;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null)throw new DAOException("Employee Id Is NULL");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("Employee Id Is NULL");
try
{
String fEmployeeId;
File f=new File(FILE_NAME);
if(f.exists()==false) return false;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return false;
}
raf.readLine();
raf.readLine();
while(raf.getFilePointer()<raf.length())
{
fEmployeeId=raf.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId)==true)
{
raf.close();
return true;
}
}
raf.close();
return false;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public boolean panNumberExists(String panNumber) throws DAOException
{
if(panNumber==null)throw new DAOException("PAN Number Is NULL");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("PAN Number Is NULL");
try
{
String fpanNumber;
File f=new File(FILE_NAME);
if(f.exists()==false) return false;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return false;
}
raf.readLine();
raf.readLine();
while(raf.getFilePointer()<raf.length())
{
for(int i=1;i<=7;i++) raf.readLine();
fpanNumber=raf.readLine();
if(fpanNumber.equalsIgnoreCase(panNumber))
{
raf.close();
return true;
}
raf.readLine();
}
raf.close();
return false;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public boolean aadharNumberExists(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null)throw new DAOException("Aadhar Card Number Is NULL");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Aadhar Card Number Is NULL");
try
{
String faadharCardNumber;
File f=new File(FILE_NAME);
if(f.exists()==false) return false;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return false;
}
raf.readLine();
raf.readLine();
while(raf.getFilePointer()<raf.length())
{
for(int i=1;i<=8;i++) raf.readLine();
faadharCardNumber=raf.readLine();
if(faadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
raf.close();
return true;
}
}
raf.close();
return false;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public int getCount() throws DAOException
{
try
{
File f=new File(FILE_NAME);
if(f.exists()==false) return 0;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0) 
{
raf.close();
return 0;
}
raf.readLine();
int recordCount=Integer.parseInt(raf.readLine().trim());
raf.close();
return recordCount;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public int getCountByDesignation(int designationCode) throws DAOException
{
if(designationCode<=0) throw new DAOException("Invalid Code "+designationCode);
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isValid=designationDAO.codeExists(designationCode);
if(isValid==false) throw new DAOException("Invalid Code "+designationCode);
try
{
int count=0;
File f=new File(FILE_NAME);
if(f.exists()==false) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("No data Exists");
}
raf.readLine();
raf.readLine();
while(raf.getFilePointer()<raf.length())
{
raf.readLine();
raf.readLine();
int code=Integer.parseInt(raf.readLine());
if(code==designationCode) count++;
for(int i=1;i<=6;i++) raf.readLine();
}
raf.close();
return count;
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
}