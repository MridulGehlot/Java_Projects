import java.io.*;
class psp
{
public static void main(String gg[])
{
try
{
int lastGeneratedCode,temp;
String lastGeneratedCodeString,employeeId;
temp=999999999;
employeeId="A";
File f=new File("somework.tmp");
RandomAccessFile raf=new RandomAccessFile(f,"rw");
if(raf.length()==0) lastGeneratedCode=0;
else 
{
lastGeneratedCode=Integer.parseInt(raf.readLine().trim());
raf.seek(0);
}
lastGeneratedCode++;
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
raf.writeBytes(lastGeneratedCodeString);
raf.writeBytes("\n");
raf.close();
temp+=lastGeneratedCode;
employeeId+=String.valueOf(temp);
System.out.println("Written code in file");
System.out.println("Employee Id = "+employeeId);
}catch(IOException ioe)
{
System.out.println(ioe.getMessage());
}
}
}