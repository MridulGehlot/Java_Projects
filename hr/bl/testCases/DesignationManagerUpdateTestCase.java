import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
class DesignationManagerUpdateTestCase
{
public static void main(String gg[])
{
try
{
DesignationInterface designation=new Designation();
designation.setCode(1);
designation.setTitle("Clerk");
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
designationManager.updateDesignation(designation);
System.out.println("Designation Updated");
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