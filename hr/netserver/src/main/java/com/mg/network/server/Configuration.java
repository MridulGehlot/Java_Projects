package com.mg.network.server;
import java.io.*;
//import com.google.gson.*;
import org.xml.sax.*;
import javax.xml.xpath.*;
import com.mg.network.common.exceptions.*;

public class Configuration implements java.io.Serializable
{
private static int port=-1;
private static boolean fileMissing=false;
private static boolean malformed=false;

static
{
try
{
File file=new File("server.xml");
if(file.exists())
{
/*
RandomAccessFile raf=new RandomAccessFile(file,"rw");
String dataString="";
while(raf.getFilePointer()<raf.length())
{
dataString+=raf.readLine();
}
System.out.println("data String = "+dataString);
Gson gson=new Gson();
Configuration c=gson.fromJson(dataString,Configuration.class);
System.out.println("Host = "+c.getHost()+", port = "+c.getPort());
raf.close();
*/
//New XML Part
InputSource inputSource=new InputSource("server.xml");
XPath xpath=XPathFactory.newInstance().newXPath();
String port=xpath.evaluate("//server/@port",inputSource);
Configuration.port=Integer.parseInt(port);
}
else
{
/*
System.out.println("server.cfg file does not exists");
System.exit(0);
*/
fileMissing=true;
}
}catch(Exception e)
{
malformed=true;
//do nothing
}
}
public static int getPort() throws NetworkException
{
if(fileMissing) throw new NetworkException("server.xml file is missing, read Documentation");
if(malformed) throw new NetworkException("server.xml file is not configured according to Documentation");
if(port<0 || port>49151) throw new NetworkException("server.xml contains invalid port number read Documentation");
return port;
}
}