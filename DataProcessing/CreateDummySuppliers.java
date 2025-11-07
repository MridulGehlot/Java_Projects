import java.sql.*;
class CreateDummySuppliers
{
public static void main(String gg[])
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
PreparedStatement ps;
for(int i=1;i<=100;i++)
{
String customer="Supplier"+i;
ps=c.prepareStatement("insert into supplier (name,total_purchases,total_payments) values('"+customer+"',0.0,0.0)");
ps.executeUpdate();
ps.close();
}
c.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}