import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
class EmployeeManagerDesignationAllotedTestCase
{
public static void main(String gg[])
{
int designationCode=Integer.parseInt(gg[0]);
try
{
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
System.out.println("Designation Code Alloted = "+employeeManager.designationAlloted(designationCode));
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