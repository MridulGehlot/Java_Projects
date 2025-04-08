import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
import java.text.*;
import java.math.*;
class EmployeeManagerGetEmployeesByDesignationCodeTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
try
{
Set<EmployeeInterface> employees;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employees=employeeManager.getEmployeesByDesignationCode(code);
for(EmployeeInterface employee:employees)
{
System.out.println("Employee Id = "+employee.getEmployeeId());
System.out.println("Name = "+employee.getName());
System.out.println("Designation Code = "+employee.getDesignation().getCode());
System.out.println("Designation Title = "+employee.getDesignation().getTitle());
System.out.println("Date Of Birth = "+sdf.format(employee.getDateOfBirth()));
System.out.println("Gender = "+employee.getGender());
System.out.println("Is Indian = "+employee.getIsIndian());
System.out.println("Basic Salary = "+employee.getBasicSalary().toPlainString());
System.out.println("PAN Number = "+employee.getPANNumber());
System.out.println("Aadhar Card Number = "+employee.getAadharCardNumber());
System.out.println("****************************");
}
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