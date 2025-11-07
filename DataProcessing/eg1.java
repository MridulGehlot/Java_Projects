import java.sql.*;
class psp
{
public static void main(String gg[])
{
Connection c=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
String call="{? = call addNumbers(?,?)}";
CallableStatement cs=c.prepareCall(call);
cs.registerOutParameter(1,Types.INTEGER);
cs.setInt(2,10);
cs.setInt(3,30);
cs.execute();
int ans=cs.getInt(1);
System.out.println("Result - "+ans);
cs.close();
c.close();
}catch(Exception e)
{
System.out.println(e);
}
finally
{
try
{
if(c!=null) c.close();
}catch(Exception ee) {System.out.println(ee);}
}
}
}