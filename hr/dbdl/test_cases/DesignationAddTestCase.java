import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
public class DesignationAddTestCase
{
public static void main(String gg[])
{
String title=gg[0];
try
{
DesignationDTOInterface designationDTO=new DesignationDTO();
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDTO.setTitle(title);
designationDAO.add(designationDTO);
System.out.println("Designation : "+title+" addded with code as : "+designationDTO.getCode());
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}