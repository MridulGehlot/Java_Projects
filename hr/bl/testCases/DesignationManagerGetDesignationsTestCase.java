import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
class DesignationManagerGetDesignationsTestCase
{
public static void main(String gg[])
{
try
{
Set<DesignationInterface> list;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
list=designationManager.getDesignations();
System.out.println("--All Designations--");
for(DesignationInterface designation:list)
{
System.out.println("Code : "+designation.getCode());
System.out.println("Title : "+designation.getTitle());
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