package com.mg.hr.bl.managers;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import java.util.*;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.managers.*;

public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationsMap;
private Map<String,DesignationInterface> titleWiseDesignationsMap;
private Set<DesignationInterface> designationsSet;
private static DesignationManager designationManager=null;
private DesignationManager() throws BLException
{
populateDataStructures();
}
private void populateDataStructures() throws BLException
{
this.codeWiseDesignationsMap=new HashMap<>();
this.titleWiseDesignationsMap=new HashMap<>();
this.designationsSet=new TreeSet<>();
try
{
Set<DesignationDTOInterface> dlDesignations;
dlDesignations=new DesignationDAO().getAll();
DesignationInterface blDesignation;
for(DesignationDTOInterface dlDesignation:dlDesignations)
{
blDesignation=new Designation();
blDesignation.setCode(dlDesignation.getCode());
blDesignation.setTitle(dlDesignation.getTitle());
this.codeWiseDesignationsMap.put((blDesignation.getCode()),blDesignation);
this.titleWiseDesignationsMap.put(blDesignation.getTitle().toUpperCase(),blDesignation);
this.designationsSet.add(blDesignation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
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
if(title.length()>0)
{
if(this.titleWiseDesignationsMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Designation : "+title+" exists.");
}
}
if(blException.hasExceptions()) throw blException;
try
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.add(designationDTO);
code=designationDTO.getCode();
designation.setCode(code);
DesignationInterface dsDesignation=new Designation();
dsDesignation.setCode(code);
dsDesignation.setTitle(title);
this.codeWiseDesignationsMap.put((code),dsDesignation);
this.titleWiseDesignationsMap.put(dsDesignation.getTitle().toUpperCase(),dsDesignation);
this.designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
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
boolean codeExists=this.codeWiseDesignationsMap.containsKey(code);
if(codeExists==false)
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
try
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(code);
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.update(designationDTO);
DesignationInterface dsDesignation;
dsDesignation=this.codeWiseDesignationsMap.get(code);
String fTitle=dsDesignation.getTitle();
this.titleWiseDesignationsMap.remove(fTitle.toUpperCase());
this.designationsSet.remove(dsDesignation);
dsDesignation.setTitle(title);
dsDesignation=new Designation();
dsDesignation.setCode(code);
dsDesignation.setTitle(title);
this.titleWiseDesignationsMap.put(title.toUpperCase(),dsDesignation);
this.designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void removeDesignation(int code)throws BLException
{
boolean codeExists;
if(code<=0) codeExists=false;
else codeExists=this.codeWiseDesignationsMap.containsKey(code);
if(codeExists==false)
{
BLException blException=new BLException();
blException.setGenericException("This Code Does not Exists.");
throw blException;
}
try
{
DesignationInterface designation;
designation=this.codeWiseDesignationsMap.get(code);
String fTitle=designation.getTitle();
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.delete(code);
this.codeWiseDesignationsMap.remove(code);
this.titleWiseDesignationsMap.remove(fTitle.toUpperCase());
this.designationsSet.remove(designation);
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

DesignationInterface getDSDesignationByCode(int code)
{
return this.codeWiseDesignationsMap.get(code);
}

public DesignationInterface getDesignationByCode(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.setGenericException("Code Should Be Greater Than Zero");
throw blException;
}
else
{
if(this.codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","This Code Doesn't Exists.");
throw blException;
}
}
DesignationInterface dsDesignation,blDesignation;
dsDesignation=this.codeWiseDesignationsMap.get(code);
blDesignation=new Designation();
blDesignation.setCode(code);
blDesignation.setTitle(dsDesignation.getTitle());
return blDesignation;
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
else if(this.titleWiseDesignationsMap.containsKey(title.toUpperCase())==false)
{
blException.addException("title","This Title Doesn't Exists.");
throw blException;
}
}
DesignationInterface dsDesignation,blDesignation;
dsDesignation=this.titleWiseDesignationsMap.get(title.toUpperCase());
blDesignation=new Designation();
blDesignation.setCode(dsDesignation.getCode());
blDesignation.setTitle(dsDesignation.getTitle());
return blDesignation;
}
public boolean designationCodeExists(int code)throws BLException
{
return this.codeWiseDesignationsMap.containsKey(code);
}
public boolean designationTitleExists(String title)throws BLException
{
if(title==null)
{
BLException blException=new BLException();
blException.setGenericException("Title is Required");
throw blException;
}
else
{
title=title.trim();
if(title.length()==0)
{
BLException blException=new BLException();
blException.addException("title","Title is Required");
throw blException;
}
}
return this.titleWiseDesignationsMap.containsKey(title.toUpperCase());
}
public int getDesignationCount()throws BLException
{
return this.designationsSet.size();
}
public Set<DesignationInterface> getDesignations()throws BLException
{
Set<DesignationInterface> list=new TreeSet<>();
DesignationInterface designation;
for(DesignationInterface d:designationsSet)
{
designation=new Designation();
designation.setCode(d.getCode());
designation.setTitle(d.getTitle());
list.add(designation);
}
return list;
}
}