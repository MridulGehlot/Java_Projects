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
public byte[] onRequestBytes(String id,byte [] bytes); //when receiver give to an application
public void onResponseBytes(String id,byte [] bytes); //when sender gives back to application
public void onConnected(String id);
}
class Job
{
public String id;
public byte[] bytes;
}
class ConnectionException extends Exception
{
ConnectionException(String message)
{
super(message);
}
}
class Client
{
/*
On Server Side 5050 is used for Listening/Receiving Request
and 4040 is used for sending
Hence it will be Opposite at Client Side
5050 for sending
4040 for Receiving
*/
private Application application;
private Socket socket2Send,socket2Receive;
private PipeLines pipeLines;
private String server;
int portNumber1,portNumber2;
Client(Application application,String server,int portNumber1,int portNumber2)
{
this.application=application;
this.server=server;
this.portNumber1=portNumber1;
this.portNumber2=portNumber2;
}
public void connect()
{
try
{
socket2Send=new Socket(server,portNumber1); //5050
InputStream inputStream1=socket2Send.getInputStream();
InputStreamReader inputStreamReader1=new InputStreamReader(inputStream1);
OutputStream outputStream1=socket2Send.getOutputStream();
OutputStreamWriter outputStreamWriter1=new OutputStreamWriter(outputStream1);

String request,response,clientId;
request="CONNECT#";
outputStreamWriter1.write(request);
outputStreamWriter1.flush();
StringBuffer sb=new StringBuffer();
int x;
while(true)
{
x=inputStreamReader1.read();
if(x=='#') break;
sb.append((char)x);
}
clientId=sb.toString();
socket2Receive=new Socket(server,portNumber2); //4040
InputStream inputStream2=socket2Receive.getInputStream();
InputStreamReader inputStreamReader2=new InputStreamReader(inputStream2);
OutputStream outputStream2=socket2Receive.getOutputStream();
OutputStreamWriter outputStreamWriter2=new OutputStreamWriter(outputStream2);
request=clientId+"#";
outputStreamWriter2.write(clientId);
outputStreamWriter2.flush();

sb=new StringBuffer();
while(true)
{
x=inputStreamReader2.read();
if(x=='#') break;
sb.append((char)x);
}
response=sb.toString();
if(response.equals("INVALID"))
{
throw new ConnectionException("Unalbe To Connect");
}
//create Threads for Sending And Receiving
Sender sender;
Receiver receiver;
sender=new Sender(application,socket2Send,clientId,inputStream1,inputStreamReader1,outputStream1,outputStreamWriter1);
receiver=new Receiver(application,socket2Receive,clientId,inputStream2,inputStreamReader2,outputStream2,outputStreamWriter2);
pipeLines=new PipeLines();
pipeLines.sender=sender;
pipeLines.receiver=receiver;
sender.start();
receiver.start();

}catch(Exception e)
{
System.out.println(e);
}
}
}
class Server
{
private Application application;
private ServerSocket serverSocket1=null;
private ServerSocket serverSocket2=null;
private HashMap<String,Object[]> socketStreams;
private HashMap<String,PipeLines> pipeLinesMap;
private Thread threadForServerSocket1;
private Thread threadForServerSocket2;
Server(Application application)
{
this.application=application;
this.pipeLinesMap=new HashMap<>();
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
Sender sender;
Receiver receiver;
PipeLines pipeLines;
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
sender=new Sender(application,socket,id,inputStream,inputStreamReader,outputStream,outputStreamWriter);
receiver=new Receiver(application,(Socket)objects[0],id,(InputStream)objects[1],(InputStreamReader)objects[2],(OutputStream)objects[3],(OutputStreamWriter)objects[4]);
pipeLines=new PipeLines();
pipeLines.sender=sender;
pipeLines.receiver=receiver;
pipeLines.connectionId=id;
pipeLinesMap.put(id,pipeLines);
response="CONNECTED#";
outputStreamWriter.write(response);
outputStreamWriter.flush();
sender.start();
receiver.start();
application.onConnected(id);
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
//SENDER CLASS
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
int size=data.length;
byte header[]=new byte[16];
int i=15;
while(size>0)
{
header[i]=(byte)size%10;
size/=10;
i--;
}
outputStreamWriter.write(header);
outputStreamWriter.flush();
//Header sent
//Receiving ACK
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
StringBuffer sb;
//lot of more variables
while(true)
{
x=this.inputStreamReader.read();
if(x==-1) continue;
/*
Code to Extract everything from isr
Header + Content (byte[]) then call some method of Application and send byte[]
*/
sb=new StringBuffer();
while(true)
{
x=this.inputStreamReader.read();
if(x=='#') break;
sb.append((char)x);
}
request=sb.toString();
byte [] response=this.application.onRequestBytes(clientId,bytes);
//send back response bytes
}
}catch(Exception e)
{
this.clientConnected=false;
System.out.println(e);
}
}
}
class ServerSidePQRApplication implements Application
{
private Server server;
ServerSidePQRApplication()
{}
public void start()
{
server=new Server(this);
server.start();
}
public void onResponseBytes(String id,byte[] bytes)
{
}
public byte[] onRequestBytes(String id,byte[] bytes)
{
return null;
}
public void onConnected(String id)
{}
public static void main(String args[])
{
ServerSidePQRApplication serverApp=new ServerSidePQRApplication();
serverApp.start();
}
}

class ClientSideApplication implements Application
{
private Client client;
ClientSideApplication()
{}
public void start()
{
client=new Client(this,"localhost",5050,4040);
client.connect();
}
public void onResponseBytes(String id,byte[] bytes)
{
}
public byte[] onRequestBytes(String id,byte[] bytes)
{
return null;
}
public void onConnected(String id)
{}
public static void main(String args[])
{
ClientSideApplication clientApp=new ClientSideApplication();
clientApp.start();
}
}