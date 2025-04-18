package com.mg.hr.server;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.managers.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.network.common.*;
import com.mg.network.server.*;
public class RequestHandler implements RequestHandlerInterface
{
private DesignationManagerInterface designationManager;
public RequestHandler()
{
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
//do nothing
}
}
public Response process(Request request)
{
Response response=new Response();

String managerType=request.getManager();
String method=request.getAction();

System.out.println("Manager = "+managerType+" , method = "+method);

Object arguments=request.getArguments();
response.setSuccess(true);
response.setException(null);

try
{

if(managerType.equalsIgnoreCase("DesignationManager"))
{
if(designationManager==null)
{
//will implement later on
}
if(method.equals("addDesignation")) designationManager.addDesignation((DesignationInterface)arguments);
else if(method.equals("updateDesignation")) designationManager.updateDesignation((DesignationInterface)arguments);
else if(method.equals("removeDesignation")) designationManager.removeDesignation((int)arguments);
else if(method.equals("getDesignationByCode")) 
{
DesignationInterface d=designationManager.getDesignationByCode((int)arguments);
response.setResult(d);
}
else if(method.equals("getDesignationByTitle"))
{
DesignationInterface d=designationManager.getDesignationByTitle((String)arguments);
response.setResult(d);
}
else if(method.equals("designationCodeExists"))
{
boolean result=designationManager.designationCodeExists((int)arguments);
response.setResult(result);
}
else if(method.equals("designationTitleExists"))
{
boolean result=designationManager.designationTitleExists((String)arguments);
response.setResult(result);
}
else if(method.equals("getDesignationCount"))
{
int count=designationManager.getDesignationCount();
response.setResult(count);
}
else if(method.equals("getDesignations"))
{
Object resultSet=designationManager.getDesignations();
response.setResult(resultSet);
}
else
{
response.setSuccess(false);
response.setException(new Exception("Not A Valid Method"));
}
}
else if(managerType.equalsIgnoreCase("EmployeeManager"))
{
EmployeeManagerInterface manager=EmployeeManager.getEmployeeManager();
}
else
{
response.setSuccess(false);
response.setException(new Exception("No Manager Found"));
}

}catch(BLException blException)
{
response.setSuccess(false);
response.setException(blException);
}
return response;
}
}