import com.grapecity.documents.excel.*;
import com.grapecity.documents.excel.drawing.*;

import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.managers.*;
import java.util.*;
import java.text.*;
import java.math.*;

public class Testing
{
public static void main(String gg[])
{
int sz=0;
Object data[][]=null;
try
{
Set<EmployeeInterface> employees;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employees=employeeManager.getEmployees();
data=new Object[employees.size()+1][10];
sz=employees.size();
data[0][0]="Employee Id";
data[0][1]="Name";
data[0][2]="Designation Code";
data[0][3]="Title";
data[0][4]="Date Of Birth";
data[0][5]="Gender";
data[0][6]="Indian";
data[0][7]="Basic Salary";
data[0][8]="PAN Number";
data[0][9]="Aadhar Number";
int i=1;
for(EmployeeInterface employee:employees)
{
data[i][0]=employee.getEmployeeId();
data[i][1]=employee.getName();
data[i][2]=employee.getDesignation().getCode();
data[i][3]=employee.getDesignation().getTitle();
data[i][4]=sdf.format(employee.getDateOfBirth());
data[i][5]=employee.getGender();
data[i][6]=employee.getIsIndian();
data[i][7]=employee.getBasicSalary().toPlainString();
data[i][8]=employee.getPANNumber();
data[i][9]=employee.getAadharCardNumber();
i++;
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

Workbook workbook = new Workbook();
IWorksheet worksheet = workbook.getWorksheets().get(0);
worksheet.setStandardHeight(20);
worksheet.setStandardWidth(50);
String dim="A1:J";
System.out.println(sz+2);
dim+=sz+2;
worksheet.getRange(dim).setValue(data);
workbook.save("DsExcelFeatures.xlsx");
System.out.println("Running");
}
}