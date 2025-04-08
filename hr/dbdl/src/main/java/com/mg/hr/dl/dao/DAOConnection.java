package com.mg.hr.dl.dao;
import java.sql.*;
import com.mg.hr.dl.exceptions.*;
public class DAOConnection
{
private DAOConnection(){}
public static Connection getConnection() throws DAOException
{
Connection c=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
c=DriverManager.getConnection("jdbc:mysql://localhost:3307/hrdb","hr","hr");
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
return c;
}
}