import java.io.*;
import java.net.*;
import java.util.*;
class CReceiver extends Thread
{
private Socket socket;
CReceiver(Socket socket)
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
StringBuffer sb=new StringBuffer();
int x;
is=socket.getInputStream();
isr=new InputStreamReader(is);
while(true)
{
while(true)
{
x=isr.read();
if(x==-1) continue;
else break;
}
sb.append((char)x);
while(true)
{
x=isr.read();
if(x==-1) break;
sb.append((char)x);
}
//processing
String response=sb.toString();
System.out.println(response);
if(response.equals("Sayonara")) break;
}
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class CSender extends Thread
{
private Socket socket;
CSender(Socket socket)
{
this.socket=socket;
start();
}
public void run()
{
try
{
OutputStream os;
OutputStreamWriter osw;
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
boolean flag=false;
Scanner sc=new Scanner(System.in);
while(true)
{
System.out.println("Enter A Message - ");
String input=sc.nextLine();
osw.write(input);
osw.flush();
if(input.indexOf("END")!=-1) flag=true;
if(flag) break;
}
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Client
{
private Socket socket1;
private Socket socket2;
private CSender sender;
private CReceiver receiver;
public static void main(String args[])
{
try
{
OutputStream os;
OutputStreamWriter osw;
InputStream is;
InputStreamReader isr;
StringBuilder sb=new StringBuilder();
int x;

socket1=new Socket("localhost",5050);

is=socket1.getInputStream();
isr=new InputStreamReader(is);
while(true)
{
x=isr.read();
if(x==-1) break;
sb.append((char)x);
}
String password=sb.toString();
System.out.println("We Got The Password - "+password);
sender=new CSender(socket1);

socket2=new Socket("localhost",6000);
os=socket2.getOutputStream();
osw=new OutputStreamWriter(os);
osw.write(password);
osw.flush();
receiver=new CReceiver(socket2);

}catch(Exception e)
{
System.out.println(e);
}

}
}