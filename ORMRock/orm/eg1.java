import com.google.gson.*;
import java.io.*;
import java.sql.*;
class eg1psp
{
public static void main(String gg[])
{
try
{
Credentials credentials=null;
try
{
Gson gson=new Gson();
File f=new File("conf.json");
if(f.exists()==false)
{
System.out.println("conf.json Doesn't Exists in this Package");
return;
}
RandomAccessFile raf=new RandomAccessFile(f,"rw");
StringBuffer sb=new StringBuffer();
while(raf.getFilePointer()<raf.length())
{
sb.append(raf.readLine());
}
String jsonString=sb.toString();
credentials=gson.fromJson(jsonString,Credentials.class);
}catch(Exception gsonException)
{
System.out.println("conf.json if malformed");
System.out.println("Issue : "+gsonException);
return;
}
String jdbcDriver=credentials.jdbcDriver;
String connectionURL=credentials.connectionURL;
String username=credentials.username;
String password=credentials.password;
Class.forName(jdbcDriver);
Connection connection=DriverManager.getConnection(connectionURL,username,password);
DatabaseMetaData databaseMetaData = connection.getMetaData();

            System.out.println("--- Reading metadata for database: mgschool ---");
            
            // Get all tables
            String[] types = {"TABLE"};
            ResultSet tables = databaseMetaData.getTables(null, null, "%", types);
            
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("\n=============================================");
                System.out.println("Table: " + tableName);
                System.out.println("=============================================");
                
                // Get and print column information
                System.out.println("\n--- Columns and data types ---");
                printColumns(databaseMetaData, tableName);
                
                // Get and print primary key constraints
                System.out.println("\n--- Primary Key ---");
                printPrimaryKeys(databaseMetaData, tableName);
                
                // Get and print foreign key constraints
                System.out.println("\n--- Foreign Keys ---");
                printForeignKeys(databaseMetaData, tableName);
            }

}catch(Exception e)
{
System.out.println(e);
}
}

private static void printForeignKeys(DatabaseMetaData metadata, String tableName) throws SQLException {
        ResultSet foreignKeys = metadata.getImportedKeys(null, null, tableName);
        boolean hasForeignKeys = false;
        while (foreignKeys.next()) {
            hasForeignKeys = true;
            String pkTableName = foreignKeys.getString("PKTABLE_NAME");
            String fkTableName = foreignKeys.getString("FKTABLE_NAME");
            String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
            String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
            String fkName = foreignKeys.getString("FK_NAME");
            System.out.printf("  - %s.%s -> %s.%s (Constraint Name: %s)\n", 
                fkTableName, fkColumnName, pkTableName, pkColumnName, fkName);
        }
        if (!hasForeignKeys) {
            System.out.println("  - None");
        }
    }

  private static void printPrimaryKeys(DatabaseMetaData metadata, String tableName) throws SQLException {
        ResultSet primaryKeys = metadata.getPrimaryKeys(null, null, tableName);
        boolean hasPrimaryKey = false;
        while (primaryKeys.next()) {
            hasPrimaryKey = true;
            String columnName = primaryKeys.getString("COLUMN_NAME");
            String pkName = primaryKeys.getString("PK_NAME");
            System.out.printf("  - Column: %s (Constraint Name: %s)\n", columnName, pkName);
        }
        if (!hasPrimaryKey) {
            System.out.println("  - None");
        }
    }

private static void printColumns(DatabaseMetaData metadata, String tableName) throws SQLException {
        ResultSet columns = metadata.getColumns(null, null, tableName, null);
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            int columnSize = columns.getInt("COLUMN_SIZE");
            String isNullable = columns.getString("IS_NULLABLE");
            System.out.printf("  - %-20s %-15s (%-5d) %s\n", 
                columnName, columnType, columnSize, isNullable.equals("NO") ? "NOT NULL" : "");
        }
    }

}