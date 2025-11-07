import java.sql.*;
public class UpdateableResultSet
{
public static void main(String gg[])
{
        String url = "jdbc:mysql://localhost:3307/newdb";
        String username = "newUser";
        String password = "newUser";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String sql = "SELECT * FROM emp";
            ResultSet rs = stmt.executeQuery(sql);

rs.last();
System.out.println("Before update: " + rs.getString("name"));
rs.updateString("name", "Imran");
rs.updateRow(); 
System.out.println("After update: " + rs.getString("name"));

rs.moveToInsertRow();
rs.updateInt("id", 10);
rs.updateString("name", "John Doe");
rs.updateString("department", "CSE");
rs.updateInt("salary", 80000);
rs.insertRow();


        } catch (SQLException e) {
            e.printStackTrace();
        }


}
}