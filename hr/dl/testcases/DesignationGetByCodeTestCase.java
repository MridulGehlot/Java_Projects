import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
public class DesignationGetByCodeTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
try
{
DesignationDTOInterface designationDTO;
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDTO=designationDAO.getByCode(code);
System.out.println("Designation : "+designationDTO.getTitle()+" with code as : "+designationDTO.getCode());
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}