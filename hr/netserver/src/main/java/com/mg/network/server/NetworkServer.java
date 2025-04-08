package com.mg.network.server;
import java.net.*;
import com.mg.network.common.exceptions.*;
public class NetworkServer
{
private ServerSocket serverSocket;
private RequestHandlerInterface rh;
public NetworkServer(RequestHandlerInterface rh) throws NetworkException
{
if(rh==null)
{
throw new NetworkException("No Handler Found");
}
this.rh=rh;
}
public void start()
{
try
{
serverSocket=new ServerSocket(Configuration.getPort());
RequestProcessor requestProcessor;
Socket socket;
while(true)
{
System.out.println("Server is Listening on Port "+Configuration.getPort());
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket,this.rh);
}
}catch(Exception e)
{
System.out.println(e);
}
}
}