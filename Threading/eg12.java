import java.sql.*;
import java.time.*;
import java.util.*;
class psp
{
public static void main(String g[])
{
try
{
System.out.println("Running");
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
Random random=new Random();
LocalDate date=LocalDate.now();
PreparedStatement ps=null;
for(int i=1;i<=100;i++)
{
ps=c.prepareStatement("insert into payment (reference_number,payment_date,supplier_code,amount) values(?,?,?,?)");
ps.setInt(1,i);
date.plusDays(random.nextInt(5)+1);
ps.setDate(2,java.sql.Date.valueOf(date));
ps.setInt(3,i);
ps.setInt(4,random.nextInt(50)+100);
ps.executeUpdate();
}
ps.close();
c.close();
System.out.println("Work DOne");
}catch(Exception e)
{
System.out.println(e);
}
}
}