import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import java.math.*;
import java.text.*;
import com.mg.enums.*;

public class EmployeeUpdateTestCase
{
public static void main(String gg[])
{
String employeeId=gg[0];
String name=gg[1];
int designationCode=Integer.parseInt(gg[2]);
String dob=gg[3];
Date d=new Date();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
try
{
d=sdf.parse(dob);
}catch(ParseException pe)
{
System.out.println(pe.getMessage());
}
char gender=gg[4].charAt(0);
boolean isIndian=Boolean.parseBoolean(gg[5]);
BigDecimal basicSalary=new BigDecimal(gg[6]);
String panNumber=gg[7];
String aadharCardNumber=gg[8];
try
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(employeeId);
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
employeeDAO.update(employeeDTO);
System.out.println("Data Updated....");
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}