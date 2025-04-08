import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
public class DesignationGetCountTestCase
{
public static void main(String gg[])
{
try
{
DesignationDAOInterface designationDAO=new DesignationDAO();
int count=designationDAO.getCount();
System.out.println("Number Of Records = "+count);
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}