package com.mg.framework;
import java.net.*;
import java.util.*;
public class NFrameworkServer
{
List<Class> classes;
public NFrameworkServer()
{
classes=new ArrayList<>();
}
public void registerClass(Class c)
{
classes.add(c);
}
public void start()
{
try
{
ServerSocket serverSocket=new ServerSocket(5500);
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Server is Listening on Port 5500....");
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket,classes);
}
}catch(Exception e)
{
System.out.println(e);
}
}
}