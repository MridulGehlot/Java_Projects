import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
class DesignationManagerRemoveTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
try
{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
designationManager.removeDesignation(code);
System.out.println("Designation Removed");
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
}
}
}
}