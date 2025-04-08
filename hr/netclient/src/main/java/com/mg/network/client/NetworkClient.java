package com.mg.network.client;
import com.mg.network.common.*;
import com.mg.network.common.exceptions.*;
import java.net.*;
import java.io.*;
public class NetworkClient
{
private Socket socket;
public NetworkClient()
{
try
{
System.out.println(Configuration.getHost()+" , "+Configuration.getPort());
this.socket=new Socket(Configuration.getHost(),Configuration.getPort());
}catch(Exception e)
{
System.out.println(e);
}
}
public Response send(Request request) throws NetworkException
{
/*
Network / Socket programming Code here
1. Serialize Request Object
2. Connect to server
3. send header -> Request ->
4. recv <- header <-Response
5. return resp
*/
try
{
System.out.println("Netwrok Client send method called");

ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(request);
oos.flush();
byte [] requestByteArray=baos.toByteArray();

OutputStream os;
InputStream is;
os=this.socket.getOutputStream();
is=this.socket.getInputStream();

int length=requestByteArray.length;
System.out.println("Sending Header of size = "+length);
byte []header=new byte[1024];
int i=0;
int xyz=length;
while(xyz>0)
{
header[i]=(byte)(xyz%10);
xyz/=10;
i++;
}
os.write(header,0,1024);
os.flush();
int bytesToSend,numberOfBytesReceived;
byte ack[]=new byte[1];
while(true)
{
numberOfBytesReceived=is.read(ack);
if(numberOfBytesReceived==-1) continue;
break;
}
System.out.println("Ack Received");
int j=0;
bytesToSend=1024;
while(j<length)
{
if((length-j)<1024) bytesToSend=length-j;
os.write(requestByteArray,j,bytesToSend);
os.flush();
j+=bytesToSend;
}

//Receiving Response
int bytesReadCount=0;
j=0;
i=0;
byte tmp[]=new byte[1024];
while(j<1024)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(int k=0;k<bytesReadCount;k++) 
{
header[i]=tmp[k];
i++;
}
j+=bytesReadCount;
}
ack[0]=1;
os.write(ack,0,1);
os.flush();
//process header
i=0;
length=0;
int base=1;
while(i<1024)
{
length+=header[i]*base;
base*=10;
i++;
}
byte [] responseByteArray=new byte[length];
j=0;
i=0;
while(j<length)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(int k=0;k<bytesReadCount;k++)
{
responseByteArray[i]=tmp[k];
i++;
}
j+=bytesReadCount;
}
ByteArrayInputStream bais=new ByteArrayInputStream(responseByteArray);
ObjectInputStream ois=new ObjectInputStream(bais);
Response response=(Response)ois.readObject();
socket.close();
return response;
}catch(Exception e)
{
System.out.println(e);
}
return null;
}
}