import java.sql.*;
public class ScrollableResultSet
{
public static void main(String gg[])
{
        String url = "jdbc:mysql://localhost:3307/newdb";
        String username = "newUser";
        String password = "newUser";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Create Statement with scrollable and read-only ResultSet
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT * FROM emp";
            ResultSet rs = stmt.executeQuery(sql);

            // Move to last row
            rs.last();
            int rowCount = rs.getRow();
            System.out.println("Total rows: " + rowCount);

            // Scroll through result set
            rs.beforeFirst();  // Move cursor before the first row
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

}
}