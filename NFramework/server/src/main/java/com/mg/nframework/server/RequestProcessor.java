package com.mg.nframework.server;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.lang.reflect.*;
import com.mg.nframework.common.*;
public class RequestProcessor extends Thread
{
private NFrameworkServer server;
private Socket socket;
RequestProcessor(Socket socket,NFrameworkServer server)
{
this.server=server;
this.socket=socket;
start();
}
public void run()
{
try
{
//System.out.println("Request Arrived in RequestProcessor.java");

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
//System.out.println("Header Received Now will process header");
while(i<=1023)
{
requestLength=requestLength+(header[i]*j);
j=j*10;
i++;
}
//System.out.println("Request Length = "+requestLength);
ack[0]=1;
os.write(ack,0,1);
os.flush();
//System.out.println("Ack Sent");
j=0;
i=0;
byte requestObject[]=new byte[requestLength];
while(j<requestLength)
{
//System.out.println("Inside Doubt Loop");
bytesReadCount=is.read(tmp);
//System.out.println("Bytes Read Count = "+bytesReadCount);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
requestObject[i]=tmp[k];
i++;
}
j=j+requestLength;
//System.out.println("j = "+j);
}
//System.out.println("Request Object Arrived");
String requestJSONString=new String(requestObject,StandardCharsets.UTF_8);
//System.out.println(requestJSONString);
Request request=JSONUtil.fromJSON(requestJSONString,Request.class);
String servicePath=request.getServicePath();
//System.out.println("calling get TCP service");
TCPService tcpService=server.getTCPService(servicePath);
//System.out.println("Successfully called get TCP service");
Response response=new Response();
//System.out.println("*********Response Object Created*******");
if(tcpService==null)
{
System.out.println("TCP service is null");
response.setSuccess(false);
response.setResult(null);
response.setException(new RuntimeException("Invalid Path : "+servicePath));
//System.out.println("Response.getException()");
//System.out.println(response.getException());
}
else
{
Object arguments[]=request.getArguments();
Class c=tcpService.c;
Method m=tcpService.method;
//System.out.println(c.getName());
//System.out.println(m.getName());
try{
Object obj=c.newInstance();

Class<?>[] parameterTypes = m.getParameterTypes();
Object[] convertedArgs = new Object[arguments.length];

for (int x = 0; x < arguments.length; x++) {
Object arg = arguments[x];
Class<?> expectedType = parameterTypes[x];
if (arg instanceof Number) {
Number n = (Number) arg;
if (expectedType == int.class || expectedType == Integer.class) {
convertedArgs[x] = n.intValue();
} else if (expectedType == long.class || expectedType == Long.class) {
convertedArgs[x] = n.longValue();
} else if (expectedType == double.class || expectedType == Double.class) {
convertedArgs[x] = n.doubleValue();
} else if (expectedType == float.class || expectedType == Float.class) {
convertedArgs[x] = n.floatValue();
} else {
convertedArgs[x] = arg; // fallback
}
} else {
convertedArgs[x] = arg;
}
}

Object result=m.invoke(obj,convertedArgs);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(InstantiationException ie)
{
response.setSuccess(false);
response.setResult(null);
response.setException(new RuntimeException("Invalid Path : "+servicePath));
//System.out.println("Unable TO Create Object For Service Path");
}catch(IllegalAccessException iae)
{
response.setSuccess(false);
response.setResult(null);
response.setException(new RuntimeException("Invalid Path : "+servicePath));
//System.out.println("Unable TO Create Object For Service Path");
}catch(InvocationTargetException ite)
{
Throwable t=ite.getCause();
response.setSuccess(false);
response.setResult(null);
response.setException(t);
}
}


/*
FULL Flow 
header<- ack-> data<-
header-> ack<- response-> ack<-
*/

if(response==null) System.out.println("*****OHHO Response oBJECT is NULL****");
String responseJSONString=JSONUtil.toJSON(response);
//System.out.println(responseJSONString);
byte responseObject[]=responseJSONString.getBytes(StandardCharsets.UTF_8);
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
//System.out.println("closing connection");
//socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}