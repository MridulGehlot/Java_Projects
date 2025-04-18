import java.net.*;
import java.io.*;
class Client2
{
public static void main(String gg[])
{
try
{
int rollNumber=Integer.parseInt(gg[0]);
String name=gg[1];
String gender=gg[2];
InputStream is;
InputStreamReader isr;
OutputStream os;
OutputStreamWriter osw;
StringBuffer sb;
String request,response;
request=rollNumber+","+name+","+gender+"#";
Socket socket=new Socket("localhost",5500);
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
osw.write(request);
osw.flush(); //very very important
is=socket.getInputStream();
isr=new InputStreamReader(is);
sb=new StringBuffer();
int x;
while(true)
{
x=isr.read();
if(x==-1) break;
if(x=='#') break;
sb.append((char)x);
}
response=sb.toString();
System.out.println("Response Arrived = "+response);
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}