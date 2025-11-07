import java.io.*;
import java.net.*;
import java.util.*;
class Receiver extends Thread
{
private Socket socket;
Receiver(Socket socket)
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
String id="MGCompanies";
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
osw.write(id);
osw.flush();
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
String request=sb.toString();
System.out.println(request);
if(request.equals("END")) break;
}
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Sender extends Thread
{
private Socket socket;
private Queue<String> queue;
Sender(Socket socket)
{
this.socket=socket;
this.queue=new ArrayDeque<>();
start();
}
public void addToQueue(String msg)
{
this.queue.add(msg);
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
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
String response;
while(true)
{
x=isr.read();
if(x==-1) break;
sb.append((char)x);
}
String request=sb.toString();
if(!request.equals("MGCompanies"))
{
response="Invalid Creadentials";
osw.write(response);
osw.flush();
socket.close();
return;
}
boolean flag=false;
while(true)
{
//sending Message if something in queue
if(!this.queue.isEmpty())
{
while(!this.queue.isEmpty())
{
osw.write(this.queue.poll());
}
osw.flush();
}
else Thread.sleep(100);
if(flag) break;
}
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Server
{
private ServerSocket serverSocket1;
private ServerSocket serverSocket2;
private Receiver receiver;
private Sender sender;
Server()
{
try
{
serverSocket1=new ServerSocket(5050);
serverSocket2=new ServerSocket(6000);
startListening();
}catch(Exception e)
{
System.out.println(e);
}
}
public void startMessageTimer()
{
Timer t=new Timer();
final int [] count={1};
t.scheduleAtFixedRate(new TimerTask(){
int runs=0;
public void run()
{
if(runs>=3)
{
this.cancel();
return;
}
for(int i=0;i<3;i++)
{
sender.addToQueue("hii"+count[0]);
}
count[0]++;
runs++;
}
},10000,5000); //start after 10 sec and repeat after every 5 sec
}
public void startListening()
{
try
{
Socket socket;
while(true)
{
System.out.println("Server1 is Listening on port 5050...");
socket=serverSocket1.accept();
receiver=new Receiver(socket);
System.out.println("Server1 is Listening on port 6000...");
socket=serverSocket2.accept();
sender=new Sender(socket);
startMessageTimer();
}
}catch(Exception e)
{
System.out.println(e);
}
}
public static void main(String args[])
{
Server server=new Server();
}
}