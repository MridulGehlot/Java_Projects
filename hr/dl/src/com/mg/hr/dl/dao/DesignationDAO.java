package com.mg.hr.dl.dao;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.dto.*;
import com.mg.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;
public class DesignationDAO implements DesignationDAOInterface
{
private static final String FILE_NAME="designation.data";
public void add(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null)throw new DAOException("Designation is null");
String title=designationDTO.getTitle();
if(title==null) throw new DAOException("Designation is null");
title=title.trim();
if(title.length()==0) throw new DAOException("Length of Designation is zero");
try
{
File f=new File(FILE_NAME);
RandomAccessFile raf=new RandomAccessFile(f,"rw");
int lastGeneratedCode=0;
int recordCount=0;
String lastGeneratedCodeString="";
String recordCountString="";
if(raf.length()==0)
{
lastGeneratedCodeString="0";
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString="0";
while(recordCountString.length()<10) recordCountString+=" ";
raf.writeBytes(lastGeneratedCodeString);
raf.writeBytes("\n");
raf.writeBytes(recordCountString);
raf.writeBytes("\n");
}
else
{
lastGeneratedCodeString=raf.readLine().trim();
recordCountString=raf.readLine().trim();
lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString);
recordCount=Integer.parseInt(recordCountString);
}
int fcode;
String fTitle;
while(raf.getFilePointer()<raf.length())
{
fcode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fTitle.equalsIgnoreCase(title))
{
raf.close();
throw new DAOException("Designation "+title+" exists.");
}
}
int code=lastGeneratedCode+1;
raf.writeBytes(String.valueOf(code));
raf.writeBytes("\n");
raf.writeBytes(title);
raf.writeBytes("\n");
designationDTO.setCode(code);
lastGeneratedCode++;
recordCount++;
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
raf.seek(0);
raf.writeBytes(lastGeneratedCodeString);
raf.writeBytes("\n");
raf.writeBytes(recordCountString);
raf.writeBytes("\n");
raf.close();
}catch(IOException ioe)
{
throw new DAOException(ioe.getMessage());
}
}
public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is Null");
int code=designationDTO.getCode();
if(code<=0) throw new DAOException("Invalid Code = "+code);
String title=designationDTO.getTitle();
if(title==null)throw new DAOException("Title is NULL");
title=title.trim();
if(title.length()==0)throw new DAOException("Title is NULL");
try
{
File f=new File(FILE_NAME);
if(!f.exists()) throw new DAOException("Invalid Code = "+code);
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("Invalid Code = "+code);
}
raf.readLine();
raf.readLine();
int fCode;
String fTitle;
boolean found=false;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode==code)
{
found=true;
break;
}
}
if(!found)
{
raf.close();
throw new DAOException("Invalid Code = "+code);
}
raf.seek(0);
raf.readLine();
raf.readLine();
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode!=code && fTitle.equalsIgnoreCase(title))
{
raf.close();
throw new DAOException("Title Exists Against Another code = "+code);
}
}
File tmpf=new File("tmp.data");
if(tmpf.exists()) tmpf.delete();
RandomAccessFile tmpraf=new RandomAccessFile(tmpf,"rw");
raf.seek(0);
tmpraf.writeBytes(raf.readLine());
tmpraf.writeBytes("\n");
tmpraf.writeBytes(raf.readLine());
tmpraf.writeBytes("\n");
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode==code)
{
tmpraf.writeBytes(String.valueOf(fCode));
tmpraf.writeBytes("\n");
tmpraf.writeBytes(title);
tmpraf.writeBytes("\n");
}
else
{
tmpraf.writeBytes(String.valueOf(fCode));
tmpraf.writeBytes("\n");
tmpraf.writeBytes(fTitle);
tmpraf.writeBytes("\n");
}
}
raf.seek(0);
tmpraf.seek(0);
while(tmpraf.getFilePointer()<tmpraf.length())
{
raf.writeBytes(tmpraf.readLine());
raf.writeBytes("\n");
}
raf.setLength(tmpraf.length());
tmpraf.setLength(0);
tmpraf.close();
raf.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void delete(int code) throws DAOException
{
if(code<=0) throw new DAOException("Invalid Code = "+code);
try
{
File f=new File(FILE_NAME);
if(!f.exists()) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("No data Exists");
}
raf.readLine();
raf.readLine();
int fCode;
String fTitle="";
boolean found=false;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode==code)
{
found=true;
break;
}
}
if(!found)
{
raf.close();
throw new DAOException("Invalid Code = "+code);
}
if(new EmployeeDAO().isDesignationAlloted(code))
{
raf.close();
throw new DAOException("Cannot Delete This Designation : "+fTitle);
}
File tmpf=new File("tmp.data");
if(tmpf.exists()) tmpf.delete();
RandomAccessFile tmpraf=new RandomAccessFile(tmpf,"rw");
raf.seek(0);
tmpraf.writeBytes(raf.readLine());
tmpraf.writeBytes("\n");
int recordCount=Integer.parseInt(raf.readLine().trim());
recordCount--;
String recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
tmpraf.writeBytes(recordCountString);
tmpraf.writeBytes("\n");
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode!=code)
{
tmpraf.writeBytes(String.valueOf(fCode));
tmpraf.writeBytes("\n");
tmpraf.writeBytes(fTitle);
tmpraf.writeBytes("\n");
}
}
raf.seek(0);
tmpraf.seek(0);
while(tmpraf.getFilePointer()<tmpraf.length())
{
raf.writeBytes(tmpraf.readLine());
raf.writeBytes("\n");
}
raf.setLength(tmpraf.length());
tmpraf.setLength(0);
tmpraf.close();
raf.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<DesignationDTOInterface> getAll() throws DAOException
{
try
{
Set<DesignationDTOInterface> designations=new TreeSet<>();
File f=new File(FILE_NAME);
if(!f.exists()) return designations;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return designations;
}
raf.readLine();
raf.readLine();
int fCode;
String fTitle;
DesignationDTOInterface designationDTO;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
designations.add(designationDTO);
}
raf.close();
return designations;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public DesignationDTOInterface getByCode(int code) throws DAOException
{
if(code<=0)throw new DAOException("Invalid Code : "+code);
try
{
File f=new File(FILE_NAME);
if(!f.exists()) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("Invalid Code = "+code);
}
raf.readLine();
int recordCount=Integer.parseInt(raf.readLine().trim());
if(recordCount==0)
{
raf.close();
throw new DAOException("Invalid Code = "+code);
}
int fCode;
String fTitle;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode==code)
{
raf.close();
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
return designationDTO;
}
}
raf.close();
throw new DAOException("Invalid Code = "+code);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public DesignationDTOInterface getByTitle(String title) throws DAOException
{
if(title==null)throw new DAOException("Title is NULL");
title=title.trim();
if(title.length()==0)throw new DAOException("Title is NULL");
try
{
File f=new File(FILE_NAME);
if(!f.exists()) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
throw new DAOException("Invalid Title = "+title);
}
raf.readLine();
raf.readLine();
int fCode;
String fTitle;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fTitle.equalsIgnoreCase(title))
{
raf.close();
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
return designationDTO;
}
}
raf.close();
throw new DAOException("Invalid Title = "+title);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean codeExists(int code) throws DAOException
{
if(code<=0)throw new DAOException("Invalid Code = "+code);
try
{
File f=new File(FILE_NAME);
if(!f.exists()) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return false;
}
raf.readLine();
raf.readLine();
int fCode;
String fTitle;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fCode==code)
{
raf.close();
return true;
}
}
raf.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean tileExists(String title) throws DAOException
{
if(title==null)throw new DAOException("Title is NULL");
title=title.trim();
if(title.length()==0)throw new DAOException("Title is NULL");
try
{
File f=new File(FILE_NAME);
if(!f.exists()) throw new DAOException("No Data Exists");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return false;
}
raf.readLine();
raf.readLine();
int fCode;
String fTitle;
while(raf.getFilePointer()<raf.length())
{
fCode=Integer.parseInt(raf.readLine());
fTitle=raf.readLine();
if(fTitle.equalsIgnoreCase(title))
{
raf.close();
return true;
}
}
raf.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public int getCount() throws DAOException
{
try
{
File f=new File(FILE_NAME);
if(!f.exists()) return 0;
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0)
{
raf.close();
return 0;
}
raf.readLine();
int recordCount=Integer.parseInt(raf.readLine().trim());
raf.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
}