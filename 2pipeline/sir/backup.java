import java.net.*;
import java.util.*;
import java.io.*;
class PipeLines
{
public String connectionId;
public Sender sender;
public Receiver receiver;
}
interface Application
{
public byte[] onBytes(byte [] bytes); //when receiver give to an application
public void onBytes(String id,byte [] bytes); //when sender gives back to application
}
class Job
{
public String id;
public byte[] bytes;
}
class Server
{
private Application application;
private ServerSocket serverSocket1=null;
private ServerSocket serverSocket2=null;
private HashMap<String,Object[]> socketStreams;
private HashMap<String,PipeLines> pipelinesMap;
private Thread threadForServerSocket1;
private Thread threadForServerSocket2;
Server(Application application)
{
this.application=application;
this.pipelinesMap=new HashMap<>();
this.socketStreams=new HashMap<>();
}
public void start()
{
try
{
serverSocket1=new ServerSocket(5050);
serverSocket2=new ServerSocket(4040);

//Thread (Server Socket 1)
threadForServerSocket1=new Thread(()->{
Socket socket;
InputStream inputStream;
InputStreamReader inputStreamReader;
OutputStream outputStream;
OutputStreamWriter outputStreamWriter;
StringBuffer sb;
int x,i;
String id;
String request,response;
Object objects[];
while(true)
{
try
{
System.out.println("Server Socket1 is listening on port 5050...");
socket=serverSocket1.accept();
inputStream=socket.getInputStream();
inputStreamReader=new InputStreamReader(inputStream);
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
i=0;
x=0;
sb=new StringBuffer();
while(true)
{
i++;
x=inputStreamReader.read();
if(x=='#' || i==10) break;
sb.append((char)x);
}
if(x!='#')
{
response="INVALID#";
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
continue;
}
request=sb.toString();
if(!request.equals("CONNECT"))
{
response="INVALID#";
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
continue;
}
id=UUID.randomUUID().toString();
response=id+"#";
objects=new Object[6];
objects[0]=socket;
objects[1]=inputStream;
objects[2]=inputStreamReader;
objects[3]=outputStream;
objects[4]=outputStreamWriter;
objects[5]=new Date();
socketStreams.put(id,objects);
outputStreamWriter.write(response);
outputStreamWriter.flush();
}catch(Exception exception)
{
System.out.println(exception);
}
}//infinite Loop Ends here
});
threadForServerSocket1.start();

//Thread (Server Socket 2)
threadForServerSocket2=new Thread(()->{
Socket socket;
InputStream inputStream;
InputStreamReader inputStreamReader;
OutputStream outputStream;
OutputStreamWriter outputStreamWriter;
StringBuffer sb;
int x,i;
String id;
String request,response;
Object objects[];
while(true)
{
try
{
System.out.println("Server Socket2 is listening on port 4040...");
socket=serverSocket2.accept();
inputStream=socket.getInputStream();
inputStreamReader=new InputStreamReader(inputStream);
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
i=0;
x=0;
sb=new StringBuffer();
while(true)
{
i++;
x=inputStreamReader.read();
if(x=='#' || i==100) break;
sb.append((char)x);
}
if(x!='#')
{
response="INVALID#";
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
continue;
}
id=sb.toString();
objects=socketStreams.get(id);
if(objects==null)
{
response="INVALID#";
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
continue;
}
socketStreams.remove(id);
/*
create Objects of Sender and Receiver and wrap in PipeLines
1) App
2) id
3) Socket
4) Is
5) isr
6) os
7) osw
activate 2 pipeline
*/
}catch(Exception e)
{
System.out.println(e);
}
}//Infinite Loop Ends
});
threadForServerSocket2.start();

}catch(Exception exception)
{
System.out.println(exception);
}
}
}
class Sender extends Thread
{
private Application application;
private Socket socket;
private String clientId;
private boolean clientConnected;
private InputStream inputStream;
private InputStreamReader inputStreamReader;
private OutputStream outputStream;
private OutputStreamWriter outputStreamWriter;
private List<Job> dataQueue;
public Sender(Application application,Socket socket,String clientId,InputStream inputStream,InputStreamReader inputStreamReader,OutputStream outputStream,OutputStreamWriter outputStreamWriter)
{
this.clientConnected=true;
this.application=application;
this.socket=socket;
this.clientId=clientId;
this.inputStream=inputStream;
this.inputStreamReader=inputStreamReader;
this.outputStream=outputStream;
this.outputStreamWriter=outputStreamWriter;
this.dataQueue=Collections.synchronizedList(new ArrayList<Job>());
}
public boolean isClientConnected()
{
return this.clientConnected;
}
public void closeConnection()
{
try
{
this.socket.close();
this.clientConnected=false;
}catch(Exception e)
{
//do nothing
}
}
public String addData(byte [] data)
{
if(this.clientConnected==false) return null; //you may raise Exception
String id=UUID.randomUUID().toString();
Job j=new Job();
j.id=id;
j.bytes=data;
this.dataQueue.add(j);
this.resume(); //if thread is suspended it will be Resumed
return id;
}
public String getClientId()
{
return this.clientId;
}
public void run()
{
try
{
byte data[];
Job job;
while(true)
{
if(dataQueue.size()==0)
{
Thread.sleep(500);
continue;
}
job=dataQueue.get(0);
data=job.bytes;
/*
Code to send header with data length
Code to send data in chunks of 1024
code to collect back byte[] array
application.onBytes(job.id,array);
*/
}
}catch(Exception e)
{
this.clientConnected=false;
System.out.println(e);
}
}
}
class Receiver extends Thread
{
private Socket socket;
private InputStream inputStream;
private InputStreamReader inputStreamReader;
private OutputStream outputStream;
private OutputStreamWriter outputStreamWriter;
private String clientId;
private boolean clientConnected;
private Application application;
public Receiver(Application application,Socket socket,String clientId,InputStream inputStream,InputStreamReader inputStreamReader,OutputStream outputStream,OutputStreamWriter outputStreamWriter)
{
this.clientConnected=true;
this.application=application;
this.socket=socket;
this.clientId=clientId;
this.inputStream=inputStream;
this.outputStream=outputStream;
this.inputStreamReader=inputStreamReader;
this.outputStreamWriter=outputStreamWriter;
}
public boolean isClientConnected()
{
return this.clientConnected;
}
public void closeConnection()
{
try
{
this.socket.close();
this.clientConnected=false;
}catch(Exception e)
{
//do nothing
}
}
public String getClientId()
{
return this.clientId;
}
public void run()
{
try
{
byte bytes[]=null;
int x;
String request;

//lot of more variables
while(true)
{
x=this.inputStreamReader.read();
if(x==-1) continue;
/*
Code to Extract everything from isr
Header + Content (byte[]) then call some method of Application and send byte[]
*/
while(true)
{
x=this.inputStreamReader.read();
if(x=='#') break;
sb.append((char)x);
}
String request=sb.toString();
byte [] response=this.application.onBytes(bytes);
//send back response bytes
}
}catch(Exception e)
{
this.clientConnected=false;
System.out.println(e);
}
}
}