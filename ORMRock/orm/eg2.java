import com.google.gson.*;
import java.io.*;
import java.sql.*;
import java.util.*;
class eg2psp
{
public static void main(String gg[])
{
try
{
Credentials credentials = null;
try
{
Gson gson = new Gson();
File f = new File("conf.json");
if (!f.exists()) {
System.out.println("conf.json Doesn't Exist in this Package");
return;
}
RandomAccessFile raf = new RandomAccessFile(f, "rw");
StringBuilder sb = new StringBuilder();
while(raf.getFilePointer() < raf.length())
{
sb.append(raf.readLine());
}
raf.close();
String jsonString = sb.toString();
credentials = gson.fromJson(jsonString, Credentials.class);
}catch(Exception gsonException)
{
System.out.println("conf.json is malformed");
System.out.println("Issue : " + gsonException);
return;
}
String jdbcDriver = credentials.jdbcDriver;
String connectionURL = credentials.connectionURL;
String username = credentials.username;
String password = credentials.password;

Class.forName(jdbcDriver);
Connection connection = DriverManager.getConnection(connectionURL, username, password);

DatabaseMetaData databaseMetaData = connection.getMetaData();
String[] types = {"TABLE"};
ResultSet tables = databaseMetaData.getTables(null, null, "%", types);

while(tables.next())
{
String tableName = tables.getString("TABLE_NAME");
StringBuilder classContent = new StringBuilder();
classContent.append("@Table(name=\"").append(tableName).append("\")\r\n");
String className = toPascalCase(tableName) + "DTO";

classContent.append("public class ").append(className).append("\r\n{\r\n");
handleColumns(databaseMetaData, tableName, classContent);
classContent.append("}\r\n");

// Write DTO file
File file = new File(className + ".java");
BufferedWriter bw = new BufferedWriter(new FileWriter(file));
bw.write(classContent.toString());
bw.close();
System.out.println("Generated: " + file.getName());
}
tables.close();
connection.close();
}catch(Exception e)
{
System.out.println(e);
}
}
private static void handleColumns(DatabaseMetaData metaData, String tableName, StringBuilder content) throws Exception
{
ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
Set<String> pkSet = new HashSet<>();
while(primaryKeys.next())
{
pkSet.add(primaryKeys.getString("COLUMN_NAME"));
}
primaryKeys.close();
List<String[]> fields = new ArrayList<>();
ResultSet columns = metaData.getColumns(null, null, tableName, null);
while(columns.next())
{
String columnName = columns.getString("COLUMN_NAME");
String typeName = columns.getString("TYPE_NAME");
String isAutoIncrement = "NO";
try
{
isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
}catch (SQLException ignored) {}
String javaType = getJavaType(typeName);
String fieldName = toCamelCase(columnName);
if(pkSet.contains(columnName)) content.append("@PrimaryKey\r\n");
if("YES".equalsIgnoreCase(isAutoIncrement)) content.append("@AutoIncrement\r\n");
content.append("@Column(name=\"").append(columnName).append("\")\r\n");
content.append("private ").append(javaType).append(" ").append(fieldName).append(";\r\n");
fields.add(new String[]{javaType, fieldName});
}
columns.close();
// --- Default Constructor ---
content.append("public ").append(toPascalCase(tableName)).append("DTO() {}\r\n");
// --- Parameterized Constructor ---
content.append("public ").append(toPascalCase(tableName)).append("DTO(");
for(int i=0;i<fields.size();i++)
{
String[] f = fields.get(i);
content.append(f[0]).append(" ").append(f[1]);
if(i<fields.size()-1) content.append(", ");
}
content.append(")\r\n{\r\n");
for(String[] f : fields)
{
content.append("this.").append(f[1]).append("=").append(f[1]).append(";\r\n");
}
content.append("}\r\n");
// --- Generate Setters/Getters ---
for (String[] field : fields)
{
String type = field[0];
String name = field[1];
String camelName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
// Setter
content.append("public void set").append(camelName)
.append("(").append(type).append(" ").append(name).append(")\r\n{\r\n")
.append("this.").append(name).append("=").append(name).append(";\r\n")
.append("}\r\n");
// Getter
content.append("public ").append(type).append(" get").append(camelName)
.append("()\r\n{\r\n")
.append("return this.").append(name).append(";\r\n")
.append("}\r\n");
}
}
private static String getJavaType(String sqlType) {
sqlType = sqlType.toUpperCase();
if (sqlType.contains("INT")) return "int";
if (sqlType.contains("CHAR") || sqlType.contains("TEXT") || sqlType.contains("CLOB")) return "String";
if (sqlType.contains("DATE") || sqlType.contains("TIME")) return "java.util.Date";
if (sqlType.contains("DECIMAL") || sqlType.contains("NUMERIC")) return "java.math.BigDecimal";
if (sqlType.contains("FLOAT") || sqlType.contains("DOUBLE") || sqlType.contains("REAL")) return "double";
if (sqlType.contains("BOOL")) return "boolean";
return "String"; // default
}

private static String toCamelCase(String columnName) {
StringBuilder sb = new StringBuilder();
boolean upperNext = false;
for (char ch : columnName.toCharArray())
{
if (ch == '_') {
upperNext = true;
continue;
}
if (upperNext) {
ch = Character.toUpperCase(ch);
upperNext = false;
}
sb.append(ch);
}
return sb.toString();
}
private static String toPascalCase(String tableName) {
StringBuilder sb = new StringBuilder();
boolean upperNext = true;
for (char ch : tableName.toCharArray()) {
if (ch == '_') {
upperNext = true;
continue;
}
if (upperNext) {
ch = Character.toUpperCase(ch);
upperNext = false;
}
sb.append(ch);
}
return sb.toString();
}
}