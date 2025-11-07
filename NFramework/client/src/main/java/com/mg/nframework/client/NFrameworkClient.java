package com.mg.nframework.client;
import com.mg.nframework.common.*;
import com.mg.nframework.common.exceptions.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
public class NFrameworkClient
{
private Socket socket;
public NFrameworkClient()
{
try
{
socket=new Socket("localhost",5500);
}catch(Exception e)
{
System.out.println(e);
}
}
public Object process(String path,Object ... arguments) throws Throwable
{
Request request=new Request();
request.setServicePath(path);
request.setArguments(arguments);
Response response=send(request);
if(response==null) System.out.println("Response is NULL");
//System.out.println("Success - "+response.getSuccess());
//System.out.println("response.getException - "+response.getException());
if(response.getSuccess())
{
Object result=response.getResult();
return result;
}
else
{
throw response.getException();
}
}
public Response send(Request request)
{
try
{
/*
if (this.socket == null || this.socket.isClosed()) {
            // Reconnect if the socket is closed
            System.out.println("Socket is closed. Reconnecting...");
            this.socket = new Socket("localhost", 5500);
        }
*/
this.socket = new Socket("localhost", 5500);

//System.out.println("Netwrok Client send method called");
String requestJSONString=JSONUtil.toJSON(request);
//System.out.println(requestJSONString);
byte [] requestByteArray=requestJSONString.getBytes(StandardCharsets.UTF_8);

OutputStream os;
InputStream is;
os=this.socket.getOutputStream();
is=this.socket.getInputStream();

int length=requestByteArray.length;
//System.out.println("Sending Header of size = "+length);
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
//System.out.println("Ack Received");
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
String responseJSONString=new String(responseByteArray,StandardCharsets.UTF_8);
//System.out.println(responseJSONString);
Response response=JSONUtil.fromJSON(responseJSONString,Response.class);
//socket.close();
return response;
}catch(Exception e)
{
System.out.println(e);
}
return null;
}//method send ends
}//class ends