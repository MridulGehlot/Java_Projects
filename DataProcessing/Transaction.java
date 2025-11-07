import java.sql.*;
public class Transaction
{
public static void main(String gg[])
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
c.setAutoCommit(false);
CallableStatement cs=c.prepareCall("{call takeAmount(?,?,?,?)}");
cs.setString(1,"aaa1");
cs.setString(2,"bbb1");
cs.setInt(3,3500);
cs.registerOutParameter(4,Types.INTEGER);
cs.execute();

int success=cs.getInt(4);
if(success==0)
{
c.rollback();
}
else
{
c.commit();
System.out.println("success");
}
cs.close();
c.close();
}catch(Exception e)
{
//Either use SQLException here and if Exception occurs rollback
System.out.println(e);
}
}
}