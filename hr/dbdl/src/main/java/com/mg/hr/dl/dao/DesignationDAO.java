package com.mg.hr.dl.dao;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.dto.*;
import com.mg.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;
import java.sql.*;
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
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where title=?");
preparedStatement.setString(1,title);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Designation : "+title+" exists.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("insert into designation (title) values(?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,title);
preparedStatement.executeUpdate();
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int code=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
designationDTO.setCode(code);
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
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
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select title from designation where code=?");
p.setInt(1,code);
ResultSet r=p.executeQuery();
if(!r.next())
{
r.close();
p.close();
c.close();
throw new DAOException("Invalid Code : "+code);
}
r.close();
p.close();
p=c.prepareStatement("select code from designation where title=?"); 
p.setString(1,title);
r=p.executeQuery();
if(r.next())
{
if(code!=r.getInt("code"))
{
r.close();
p.close();
c.close();
throw new DAOException("Designation Exists Against Other Code");
}
}
r.close();
p.close();
p=c.prepareStatement("update designation set title=? where code=?"); 
p.setString(1,title);
p.setInt(2,code);
p.executeUpdate();
p.close();
c.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void delete(int code) throws DAOException
{
if(code<=0) throw new DAOException("Invalid Code = "+code);
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select title from designation where code=?");
p.setInt(1,code);
ResultSet r=p.executeQuery();
if(!r.next())
{
r.close();
p.close();
c.close();
throw new DAOException("Invalid Code : "+code);
}
if(new EmployeeDAO().isDesignationAlloted(code))
{
r.close();
p.close();
c.close();
throw new DAOException("Cannot Delete This Designation");
}
r.close();
p.close();
p=c.prepareStatement("delete from designation where code=?"); 
p.setInt(1,code);
p.executeUpdate();
r.close();
p.close();
c.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public Set<DesignationDTOInterface> getAll() throws DAOException
{
try
{
Set<DesignationDTOInterface> designations=new TreeSet<>();
Connection c=DAOConnection.getConnection();
PreparedStatement preparedStatement=c.prepareStatement("select * from designation");
ResultSet resultSet=preparedStatement.executeQuery();
DesignationDTOInterface designationDTO;
while(resultSet.next())
{
designationDTO=new DesignationDTO();
designationDTO.setCode(resultSet.getInt("code"));
designationDTO.setTitle(resultSet.getString("title"));
designations.add(designationDTO);
}
resultSet.close();
preparedStatement.close();
c.close();
return designations;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public DesignationDTOInterface getByCode(int code) throws DAOException
{
if(code<=0)throw new DAOException("Invalid Code : "+code);
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select title from designation where code=?");
p.setInt(1,code);
ResultSet r=p.executeQuery();
if(!r.next())
{
r.close();
p.close();
c.close();
throw new DAOException("Invalid Title");
}
DesignationDTOInterface designation=new DesignationDTO();
designation.setCode(code);
designation.setTitle(r.getString(1));
r.close();
p.close();
c.close();
return designation;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public DesignationDTOInterface getByTitle(String title) throws DAOException
{
if(title==null)throw new DAOException("Title is NULL");
title=title.trim();
if(title.length()==0)throw new DAOException("Title is NULL");
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select code from designation where title=?");
p.setString(1,title);
ResultSet r=p.executeQuery();
if(!r.next())
{
r.close();
p.close();
c.close();
throw new DAOException("Invalid Title");
}
DesignationDTOInterface designation=new DesignationDTO();
designation.setCode(r.getInt(1));
designation.setTitle(title);
r.close();
p.close();
c.close();
return designation;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean codeExists(int code) throws DAOException
{
if(code<=0)throw new DAOException("Invalid Code = "+code);
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select title from designation where code=?");
p.setInt(1,code);
ResultSet r=p.executeQuery();
if(r.next())
{
r.close();
p.close();
c.close();
return true;
}
r.close();
p.close();
c.close();
return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean tileExists(String title) throws DAOException
{
if(title==null)throw new DAOException("Title is NULL");
title=title.trim();
if(title.length()==0)throw new DAOException("Title is NULL");
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select code from designation where title=?");
p.setString(1,title);
ResultSet r=p.executeQuery();
if(r.next())
{
r.close();
p.close();
c.close();
return true;
}
r.close();
p.close();
c.close();
return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public int getCount() throws DAOException
{
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select count(*) from designation");
ResultSet r=p.executeQuery();
r.next();
int recordCount=r.getInt(1);
r.close();
p.close();
c.close();
return recordCount;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
}