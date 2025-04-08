import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import java.text.*;
public class EmployeeDeleteTestCase
{
public static void main(String gg[])
{
String employeeId=gg[0];
try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);
System.out.println("Data Deleted Successfully");
}catch(DAOException daoException)
{
System.out.println("Got Exception");
System.out.println(daoException.getMessage());
}
}
}