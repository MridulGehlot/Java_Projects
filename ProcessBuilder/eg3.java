import java.io.*;
class eg3psp
{
public static void main(String gg[])
{
try
{
Runtime r=Runtime.getRuntime();
Process p=r.exec("code.exe");
BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
String line;
while(true)
{
line=br.readLine();
if(line==null) break;
System.out.println(line);
}
System.out.println("Done");
}catch(Exception e)
{
System.out.println(e);
}
}
}