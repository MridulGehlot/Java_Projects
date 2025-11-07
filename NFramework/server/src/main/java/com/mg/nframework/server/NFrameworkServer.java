package com.mg.nframework.server;
import com.mg.nframework.server.annotations.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
public class NFrameworkServer
{
private ServerSocket serverSocket;
private Set<Class> tcpNetworkServiceClasses;
public NFrameworkServer()
{
tcpNetworkServiceClasses=new HashSet<>();
}
public void registerClass(Class c)
{
tcpNetworkServiceClasses.add(c);
}
public void start()
{
try
{
serverSocket=new ServerSocket(5500);
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Server is Listening on Port 5500....");
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket,this);
}
}catch(Exception e)
{
System.out.println(e);
}
}


public TCPService getTCPService(String path)
{
//System.out.println("path - "+path);
Path pathOnType;
Path pathOnMethod;
Method methods[];
String fullPath;
TCPService tcpService=null;
for(Class c:tcpNetworkServiceClasses)
{
//System.out.println("class name - "+c.getName());
//System.out.println("Is annotation present? " + c.isAnnotationPresent(Path.class));
//System.out.println("Loaded from: " + c.getProtectionDomain().getCodeSource().getLocation());
//System.out.println("Annotation class used in Calculator: " + c.getAnnotation(Path.class));
//System.out.println("Annotation class expected: " + Path.class.getName());
//System.out.println("Annotation class in Calculator classloader: " + Path.class.getClassLoader());
//System.out.println("Classloader of Calculator: " + c.getClassLoader());

pathOnType=(Path)c.getAnnotation(Path.class);
if(pathOnType==null) continue;
//System.out.println("pathOnType - "+pathOnType.value());
methods=c.getMethods();
for(Method m:methods)
{
pathOnMethod=(Path)m.getAnnotation(Path.class);
if(pathOnMethod==null) continue;
//System.out.println("pathOnMEthod - "+pathOnMethod.value());
fullPath=pathOnType.value()+pathOnMethod.value();
//System.out.println("fullPath - "+fullPath);
if(fullPath.equals(path))
{
tcpService=new TCPService();
tcpService.c=c;
tcpService.method=m;
tcpService.path=path;
//System.out.println(c.getName());
//System.out.println(m.getName());
return tcpService;
}
}
}//Class for loop ends
return null;
}//Method GetTCPService ends
}