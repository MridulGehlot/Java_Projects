import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
class EmployeeManagerEmployeeAadharCardNumberExistsTestCase
{
public static void main(String gg[])
{
String aadharCardNumber=gg[0];
try
{
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
System.out.println("Employee id Exists = "+employeeManager.employeeAadharCardNumberExists(aadharCardNumber));
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