import java.io.*;
import java.net.*;
class RequestProcessor extends Thread
{
private Socket socket;
RequestProcessor(Socket socket)
{
this.socket=socket;
start();
}
public void run()
{
try
{
InputStream is;
InputStreamReader isr;
OutputStream os;
OutputStreamWriter osw;
StringBuffer sb;
int x;
int c1,c2;
String pc1,pc2,pc3;
int rollNumber;
String name,gender;
String request,response;
is=socket.getInputStream();
isr=new InputStreamReader(is);
sb=new StringBuffer();
while(true)
{
x=isr.read();
if(x==-1) break;
if(x=='#') break;
sb.append((char)x);
}
request=sb.toString();
System.out.println("----------REQUEST_ARRIVED----------");
System.out.println(request);
System.out.println("------------REQUEST_END------------");
c1=request.indexOf(",");
c2=request.indexOf(",",c1+1);
pc1=request.substring(0,c1);
pc2=request.substring(c1+1,c2);
pc3=request.substring(c2+1);
rollNumber=Integer.parseInt(pc1);
name=pc2;
gender=pc3;
System.out.println("Roll Number - "+rollNumber);
System.out.println("Name - "+name);
System.out.println("Gender - "+gender);
response="Data Saved At Server Side#";
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
osw.write(response);
osw.flush();
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Server2
{
private ServerSocket serverSocket;
public Server2()
{
try
{
serverSocket=new ServerSocket(5500);
startListening();
}catch(Exception e)
{
System.out.println(e);
}
}
private void startListening()
{
try
{
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Server is Ready To accept Request on port 5500");
socket=serverSocket.accept(); //Server Socket Goes In Wait Mode
requestProcessor=new RequestProcessor(socket);
}
}catch(Exception e)
{
System.out.println(e);
}
}
public static void main(String gg[])
{
Server2 server=new Server2();
}
}