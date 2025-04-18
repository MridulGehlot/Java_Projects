package com.mg.framework;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
public class RequestProcessor extends Thread
{
private List<Class> classes;
private Socket socket;
RequestProcessor(Socket socket,List<Class> classes)
{
this.classes=classes;
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
Response response=process(request);
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
private Response process(Request request)
{
Response response=new Response();
response.setSuccess(false);
String path=request.getPath();
Object arguments[]=request.getArguments();
int i=path.indexOf("/",1);
String classString=path.substring(1,i);
String methodString=path.substring(i+1);

System.out.println("**********************************************");
System.out.println("Inside Request Processor class process Method");
System.out.println(classString);
System.out.println(methodString);
System.out.println("**********************************************");

for(Class c:classes)
{
if(c.isAnnotationPresent(Path.class))
{
System.out.println("Present");
Path pp=(Path)c.getAnnotation(Path.class);
System.out.println("pp .value = "+pp.value());
String vl=pp.value();
vl=vl.substring(1);
if(classString.equalsIgnoreCase(vl))
{
Method [] methods=c.getMethods();
for(Method m:methods)
{
System.out.println("m,getName = "+m.getName());
if(methodString.equalsIgnoreCase(m.getName()))
{
try
{
Object obj=c.newInstance();
Object result=m.invoke(obj,arguments);
System.out.println("result = "+result);
response.setSuccess(true);
response.setResult(result);
}catch(Exception e)
{
System.out.println(e);
}
}
}//Methods for loop ends
}//classString if ends
}//c isAnnotationPresent ends
}//for loop Classes ends
return response;
}//Method process ends
}