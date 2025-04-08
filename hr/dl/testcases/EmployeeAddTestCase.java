import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import java.math.*;
import java.text.*;
import com.mg.enums.*;

public class EmployeeAddTestCase
{
public static void main(String gg[])
{
String name=gg[0];
int designationCode=Integer.parseInt(gg[1]);
String dob=gg[2];
Date d=new Date();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
try
{
d=sdf.parse(dob);
}catch(ParseException pe)
{
System.out.println(pe.getMessage());
}
char gender=gg[3].charAt(0);
boolean isIndian=Boolean.parseBoolean(gg[4]);
BigDecimal basicSalary=new BigDecimal(gg[5]);
String panNumber=gg[6];
String aadharCardNumber=gg[7];
try
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDateOfBirth(d);
if(gender=='M' || gender=='m') employeeDTO.setGender(GENDER.MALE);
else if(gender=='F' || gender=='f') employeeDTO.setGender(GENDER.FEMALE);
else 
{
System.out.println("Gender Should Be (M/F) you entered = "+gender+"which is wrong");
return;
}
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.add(employeeDTO);
System.out.println("Data Added");
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}