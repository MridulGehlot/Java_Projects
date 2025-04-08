import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
class DesignationManagerGetDesignationByTitleTestCase
{
public static void main(String gg[])
{
String title=gg[0];
try
{
DesignationInterface designation;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
designation=designationManager.getDesignationByTitle(title);
System.out.println("Designation Fetched SuccessFully");
System.out.println("Code = "+designation.getCode());
System.out.println("Title = "+title);
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