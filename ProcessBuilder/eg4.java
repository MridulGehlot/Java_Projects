import java.io.*;
class eg4psp
{
public static void main(String gg[])
{
try
{
String a="";
for(int i=0;i<gg.length;i++)
{
a+=gg[i];
if(i<gg.length-1) a+=" ";
}
Runtime r=Runtime.getRuntime();
Process p=r.exec("eg1.exe "+a);
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