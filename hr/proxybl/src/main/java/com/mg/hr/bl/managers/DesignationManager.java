package com.mg.hr.bl.managers;
import java.util.*;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.network.client.*;
import com.mg.network.common.exceptions.*;
import com.mg.network.common.*;
public class DesignationManager implements DesignationManagerInterface
{
private static DesignationManager designationManager=null;
private DesignationManager() throws BLException
{
}
public static DesignationManagerInterface getDesignationManager() throws BLException
{
if(designationManager==null) designationManager=new DesignationManager();
return designationManager;
}
public void addDesignation(DesignationInterface designation)throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation Required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code!=0)
{
blException.addException("code","Code Should be Zero");
}
if(title==null)
{
blException.addException("title","Title Required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title Required");
}
}
if(blException.hasExceptions()) throw blException;
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.ADD));
request.setArguments(designation);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
DesignationInterface d=(DesignationInterface)response.getResult();
designation.setCode(d.getCode());
}catch(NetworkException e)
{
blException.setGenericException(e.getMessage());
throw blException;
}
}
public void updateDesignation(DesignationInterface designation)throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation Required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code<=0)
{
blException.setGenericException("This Code Does not Exists.");
throw blException;
}
if(title==null)
{
blException.addException("title","Title Required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title Required");
}
}
if(blException.hasExceptions()) throw blException;
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.UPDATE));
request.setArguments(designation);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
public void removeDesignation(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.setGenericException("Invalid Code");
throw blException;
}
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.REMOVE));
request.setArguments(code);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}

public DesignationInterface getDesignationByCode(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.setGenericException("Code Should Be Greater Than Zero");
throw blException;
}
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.GET_BY_CODE));
request.setArguments(code);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
DesignationInterface d=(DesignationInterface)response.getResult();
return d;
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
public DesignationInterface getDesignationByTitle(String title)throws BLException
{
BLException blException=new BLException();
if(title==null)
{
blException.setGenericException("Title Required");
throw blException;
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.setGenericException("Title Required");
throw blException;
}
}
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.GET_BY_TITLE));
request.setArguments(title);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
DesignationInterface d=(DesignationInterface)response.getResult();
return d;
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
public boolean designationCodeExists(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.setGenericException("Code Should Be Greater Than Zero");
throw blException;
}
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.CODE_EXISTS));
request.setArguments(code);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
boolean result=(boolean)response.getResult();
return result;
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
public boolean designationTitleExists(String title)throws BLException
{
BLException blException=new BLException();
if(title==null)
{
blException.setGenericException("Title Required");
throw blException;
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.setGenericException("Title Required");
throw blException;
}
}
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.TITLE_EXISTS));
request.setArguments(title);
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
boolean ans=(boolean)response.getResult();
return ans;
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
public int getDesignationCount()throws BLException
{
BLException blException=new BLException();
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.GET_COUNT));
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
int count=(int)response.getResult();
return count;
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
public Set<DesignationInterface> getDesignations()throws BLException
{
BLException blException=new BLException();
Set<DesignationInterface> list;
NetworkClient networkClient=new NetworkClient();
Request request=new Request();
request.setManager(Managers.getManagerType(Managers.DESIGNATION));
request.setAction(Managers.getAction(Managers.Designation.GET_ALL));
System.out.println("Manager = "+request.getManager()+",action = "+request.getAction());
Response response;
try
{
response=networkClient.send(request);
if(response.hasException())
{
blException=(BLException)response.getException();
throw blException;
}
list=(Set<DesignationInterface>)response.getResult();
return list;
}catch(NetworkException ne)
{
blException.setGenericException(ne.getMessage());
throw blException;
}
}
}