package com.mg.network.server;
import java.net.*;
import java.io.*;
import com.mg.network.common.*;
public class RequestProcessor extends Thread
{
private Socket socket;
private RequestHandlerInterface rh;
RequestProcessor(Socket socket,RequestHandlerInterface rh)
{
this.rh=rh;
this.socket=socket;
start();
}
public void run()
{
try
{
System.out.println("Request Arrived in RequestProcessor.java");

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
i=0;
j=1;
requestLength=0;
System.out.println("Header Received Now will process header");
while(i<=1023)
{
requestLength=requestLength+(header[i]*j);
j=j*10;
i++;
}
System.out.println("Request Length = "+requestLength);
ack[0]=1;
os.write(ack,0,1);
os.flush();
System.out.println("Ack Sent");
j=0;
i=0;
byte requestObject[]=new byte[requestLength];
while(j<requestLength)
{
System.out.println("Inside Doubt Loop");
bytesReadCount=is.read(tmp);
System.out.println("Bytes Read Count = "+bytesReadCount);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
requestObject[i]=tmp[k];
i++;
}
j=j+requestLength;
System.out.println("j = "+j);
}
System.out.println("Request Object Arrived");
ByteArrayInputStream bais=new ByteArrayInputStream(requestObject);
ObjectInputStream ois=new ObjectInputStream(bais);
Request request=(Request)ois.readObject();
Response response=rh.process(request);
/*
FULL Flow 
header<- ack-> data<-
header-> ack<- response-> ack<-
*/

ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(response);
oos.flush();
byte responseObject[]=baos.toByteArray();
responseLength=responseObject.length;
header=new byte[1024];
i=0;
k=responseLength;
while(k>0)
{
header[i]=(byte)(k%10);
k/=10;
i++;
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
System.out.println("closing connection");
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
