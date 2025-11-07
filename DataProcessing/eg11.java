import java.sql.*;
import java.time.*;
import java.util.*;
class psp
{
public static void main(String gg[])
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
PreparedStatement preparedStatement;

/*
java.util.Date d=new java.util.Date();
Random random=new Random();
for(int i=1;i<=30;i++)
{
int num=random.nextInt(5)+1;
System.out.println(d);
d.setDate(d.getDate()+num);
d.setMonth(d.getMonth()+num);
d.setYear(d.getYear()+num);
System.out.println(d);
}
*/



try {
            // Starting date = today
            LocalDate date = LocalDate.now();
            Random random = new Random();

            int totalDays = 40; // Total days to generate
            int recordsPerDay = 100;

            for (int i = 1; i <= totalDays; i++) {
                // Add random days between 1-5
                int daysToAdd = random.nextInt(5) + 1;
                date = date.plusDays(daysToAdd);

                // Generate 100 records for this date
                for (int r = 1; r <= recordsPerDay; r++) {
                    int randomValue = random.nextInt(100) + 1; // 1-100 random number
                    // Example: here you can insert into DB

preparedStatement=c.prepareStatement("insert into purchase (bill_number,supplier_code,item_code,quantity,rate) values(?,?,?,?,?)");
String billNumber="ABC"+r;
preparedStatement.setString(1,billNumber);
preparedStatement.setInt(2,randomValue);
randomValue = random.nextInt(400) + 1;
preparedStatement.setInt(3,randomValue);
randomValue = random.nextInt(5) + 1;
preparedStatement.setInt(4,randomValue);
randomValue = random.nextInt(10) + 10;
preparedStatement.setInt(5,randomValue);
preparedStatement.executeUpdate();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }







}catch(Exception e)
{
System.out.println(e);
}
}
}