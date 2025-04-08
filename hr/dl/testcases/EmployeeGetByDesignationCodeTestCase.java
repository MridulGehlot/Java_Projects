import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import java.text.*;
public class EmployeeGetByDesignationCodeTestCase
{
public static void main(String gg[])
{
int designationCode=Integer.parseInt(gg[0]);
try
{
Set<EmployeeDTOInterface> employees;
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employees=employeeDAO.getByDesignationCode(designationCode);
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
for(EmployeeDTOInterface e:employees)
{
System.out.println("Employee ID : "+e.getEmployeeId());
System.out.println("Name : "+e.getName());
System.out.println("Designation Code : "+e.getDesignationCode());
System.out.println("Date Of Birth : "+sdf.format(e.getDateOfBirth()));
System.out.println("Gender : "+e.getGender());
System.out.println("Is Indian : "+e.getIsIndian());
System.out.println("Basic Salary : "+e.getBasicSalary().toPlainString());
System.out.println("Pan Number : "+e.getPANNumber());
System.out.println("Aadhar Card Number : "+e.getAadharCardNumber());
System.out.println("**************************************");
}
}catch(DAOException daoException)
{
System.out.println("Got Exception");
System.out.println(daoException.getMessage());
}
}
}