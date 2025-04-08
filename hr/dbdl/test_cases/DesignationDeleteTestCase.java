import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
public class DesignationDeleteTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
try
{
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.delete(code);
System.out.println("Designation Deleted Successfully");
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}