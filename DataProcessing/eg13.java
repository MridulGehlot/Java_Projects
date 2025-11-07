import java.util.*;
import java.sql.*;
class Customer
{
public int code;
public String name;
public int amountDue;
public int amountPaid;
}
class Supplier
{
public int code;
public String name;
public int amountDue;
public int amountPaid;
}
class ReportGenerator
{
private Connection c;
private PreparedStatement ps;
private ResultSet rs;
private List<Customer> customers;
private List<Supplier> suppliers;
ReportGenerator()
{
customers=new ArrayList<>();
suppliers=new ArrayList<>();
fetch();
}
private void fetch()
{
try
{
Customer cp;
Supplier sp;
Class.forName("com.mysql.cj.jdbc.Driver");
c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
Statement s=c.createStatement();
rs=s.executeQuery("select * from customer order by code");
while(rs.next())
{
cp=new Customer();
cp.code=rs.getInt("code");
cp.name=rs.getString("name");
cp.amountDue=0;
cp.amountPaid=0;
customers.add(cp);
}
rs.close();
s.close();
System.out.println("customers fetched");

s=c.createStatement();
rs=s.executeQuery("select * from supplier order by code");
while(rs.next())
{
sp=new Supplier();
sp.code=rs.getInt("code");
sp.name=rs.getString("name");
sp.amountDue=0;
sp.amountPaid=0;
suppliers.add(sp);
}
rs.close();
s.close();
System.out.println("suppliers fetched");

//Now Calculate
s=c.createStatement();
rs=s.executeQuery("select customer_code,quantity,rate from sale");
int customerCode;
int price;
while(rs.next())
{
customerCode=rs.getInt(1);
cp=customers.get(customerCode-1);
price=rs.getInt(2)*rs.getInt(3);
cp.amountDue+=price;
}
s.close();
rs.close();

s=c.createStatement();
rs=s.executeQuery("select supplier_code,quantity,rate from purchase");
int supplierCode;
while(rs.next())
{
supplierCode=rs.getInt(1);
sp=suppliers.get(supplierCode-1);
price=rs.getInt(2)*rs.getInt(3);
sp.amountDue+=price;
}
s.close();
rs.close();
System.out.println("due amnt calculated");

//now Advance amount;
s=c.createStatement();
rs=s.executeQuery("select customer_code,amount from receipt order by customer_code");
while(rs.next())
{
customerCode=rs.getInt(1);
cp=customers.get(customerCode-1);
price=rs.getInt(2);
cp.amountPaid=price;
}
s.close();
rs.close();

s=c.createStatement();
rs=s.executeQuery("select supplier_code,amount from payment order by supplier_code");
while(rs.next())
{
supplierCode=rs.getInt(1);
sp=suppliers.get(supplierCode-1);
price=rs.getInt(2);
sp.amountPaid=price;
}
s.close();
rs.close();
System.out.println("advance calculated");

}catch(Exception e)
{
System.out.println(e);
}
}
public void show()
{
Customer cp;
Supplier sp;
System.out.println("Customer	|   toReceive 	|	AdvancePaid");
System.out.println();
for(int i=1;i<=100;i++)
{
cp=customers.get(i-1);
System.out.println(cp.name+"	|	"+cp.amountDue+"	|	"+cp.amountPaid);
}
for(int i=1;i<=100;i++)
{
sp=suppliers.get(i-1);
System.out.println(sp.name+"	|	"+sp.amountDue+"	|	"+sp.amountPaid);
}

}
}
class psp
{
public static void main(String g[])
{
ReportGenerator rp=new ReportGenerator();
rp.show();
}
}