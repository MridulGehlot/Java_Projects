import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
import java.math.*;
import java.text.*;
import com.mg.enums.*;
class EmployeeManagerAddTestCase
{
public static void main(String gg[])
{
try
{
BigDecimal salary=new BigDecimal("789000.00");
Date d=null;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
try
{
d=sdf.parse("7/11/1995");
}catch(ParseException pe)
{
System.out.println(pe.getMessage());
}
DesignationInterface designation=new Designation();
designation.setCode(4);
EmployeeInterface employee=new Employee();
employee.setName("Sanjay Gehlot");
employee.setDesignation(designation);
employee.setDateOfBirth(d);
employee.setGender(GENDER.MALE);
employee.setIsIndian(true);
employee.setBasicSalary(salary);
employee.setPANNumber("PAN012");
employee.setAadharCardNumber("UID012");
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employeeManager.addEmployee(employee);
System.out.println("Employee Added with Id as : "+employee.getEmployeeId());
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
}
List<String> properties=blException.getProperties();
for(String property:properties)
{
System.out.println(blException.getException(property));
}
}
}
}