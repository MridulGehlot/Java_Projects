import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
public class DesignationGetAllTestCase
{
public static void main(String gg[])
{
try
{
Set<DesignationDTOInterface> designations;
DesignationDAOInterface designationDAO=new DesignationDAO();
designations=designationDAO.getAll();
designations.forEach((designationDTO)->{
System.out.println("Designation : "+designationDTO.getTitle()+" with code as : "+designationDTO.getCode());
});
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}