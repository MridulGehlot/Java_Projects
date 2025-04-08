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
DesignationManagerInterface manager=(DesignationManager.getDesignationManager());
if(method.equals("addDesignation")) manager.addDesignation((DesignationInterface)arguments);
else if(method.equals("updateDesignation")) manager.updateDesignation((DesignationInterface)arguments);
else if(method.equals("removeDesignation")) manager.removeDesignation((int)arguments);
else if(method.equals("getDesignationByCode")) 
{
DesignationInterface d=manager.getDesignationByCode((int)arguments);
response.setResult(d);
}
else if(method.equals("getDesignationByTitle"))
{
DesignationInterface d=manager.getDesignationByTitle((String)arguments);
response.setResult(d);
}
else if(method.equals("designationCodeExists"))
{
boolean result=manager.designationCodeExists((int)arguments);
response.setResult(result);
}
else if(method.equals("designationTitleExists"))
{
boolean result=manager.designationTitleExists((String)arguments);
response.setResult(result);
}
else if(method.equals("getDesignationCount"))
{
int count=manager.getDesignationCount();
response.setResult(count);
}
else if(method.equals("getDesignations"))
{
Object resultSet=manager.getDesignations();
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