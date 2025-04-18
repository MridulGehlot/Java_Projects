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
int i,j,k,chunkSize;
chunkSize=1024;
int requestLength,responseLength;
int bytesReadCount;
byte ack[]=new byte[1];
OutputStream os;
InputStream is;

os=socket.getOutputStream();
is=socket.getInputStream();

bytesReadCount=0;
byte [] tmp=new byte[1024];
byte [] header=new byte[1024];
i=0;
j=0;
while(j<chunkSize)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
i=1023;
j=1;
requestLength=0;
while(i>=0)
{
requestLength=requestLength+(header[i]*j);
j=j*10;
i--;
}
ack[0]=1;
os.write(ack,0,1);
os.flush();
j=0;
i=0;
byte requestObject[]=new byte[requestLength];
while(j<requestLength)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
requestObject[i]=tmp[k];
i++;
}
j=j+requestLength;
}
ByteArrayInputStream bais=new ByteArrayInputStream(requestObject);
ObjectInputStream ois=new ObjectInputStream(bais);
Student s=(Student)ois.readObject();
System.out.println("Request Arrived");
System.out.println("-----------------------");
System.out.println("Roll Number = "+s.rollNumber);
System.out.println("Name = "+s.name);
System.out.println("Gender = "+s.gender);
System.out.println("City Code = "+s.city.code);
System.out.println("City Name = "+s.city.name);
System.out.println("-----------------------");

/*
FULL Flow 
header<- ack-> data<-
header-> ack<- response-> ack<-

ack[0]=1;
os.write(ack,0,1);
os.flush();
*/
String response="Data Saved At Server Side";
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(response);
oos.flush();
byte responseObject[]=baos.toByteArray();
responseLength=responseObject.length;
header=new byte[1024];
i=1023;
k=responseLength;
while(k>0)
{
header[i]=(byte)(k%10);
i--;
k=k/10;
}
os.write(header,0,1024);
os.flush();
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
j=0;
while(j<responseLength)
{
if((responseLength-j)<chunkSize) chunkSize=responseLength-j;
os.write(responseObject,j,chunkSize);
os.flush();
j=j+chunkSize;
}
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
System.out.println("closing connection");
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Server3
{
private ServerSocket serverSocket;
Server3()
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

public void startListening()
{
try
{
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Server Is Listening On Port 5500...");
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket);
}
}catch(Exception e)
{
System.out.println(e);
}
}

public static void main(String gg[])
{
Server3 s=new Server3();
}
}