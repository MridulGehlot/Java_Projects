import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
import java.math.*;
import java.text.*;
import com.mg.enums.*;
class EmployeeManagerUpdateTestCase
{
public static void main(String gg[])
{
try
{
BigDecimal salary=new BigDecimal("6500000.00");
Date d=null;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
try
{
d=sdf.parse("14/06/2011");
}catch(ParseException pe)
{
System.out.println(pe.getMessage());
}
DesignationInterface designation=new Designation();
designation.setCode(4);
EmployeeInterface employee=new Employee();
employee.setEmployeeId("A10000001");
employee.setName("Nihal Gehlot");
employee.setDesignation(designation);
employee.setDateOfBirth(d);
employee.setGender(GENDER.MALE);
employee.setIsIndian(true);
employee.setBasicSalary(salary);
employee.setPANNumber("PAN555");
employee.setAadharCardNumber("UID555");
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employeeManager.updateEmployee(employee);
System.out.println("Employee Updated..");
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