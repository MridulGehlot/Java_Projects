import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
public class DesignationUpdateTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
String title=gg[1];
try
{
DesignationDTOInterface designationDTO=new DesignationDTO();
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDTO.setCode(code);
designationDTO.setTitle(title);
designationDAO.update(designationDTO);
System.out.println("Designation Updated Successfully");
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}