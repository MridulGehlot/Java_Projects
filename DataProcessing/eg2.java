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
String call="{call upgrade(?,?)}";
CallableStatement cs=c.prepareCall(call);
cs.registerOutParameter(2,Types.INTEGER);
cs.setInt(1,10);
cs.execute();
int ans=cs.getInt(2);
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