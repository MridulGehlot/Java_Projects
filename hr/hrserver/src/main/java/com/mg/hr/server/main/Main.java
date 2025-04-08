package com.mg.hr.server.main;
import com.mg.network.server.*;
import com.mg.hr.server.*;
import com.mg.network.common.exceptions.*;
public class Main
{
public static void main(String gg[])
{
try
{
RequestHandlerInterface rh=new RequestHandler();
NetworkServer ns=new NetworkServer(rh);
ns.start();
}catch(NetworkException ne)
{
System.out.println(ne);
}
}
}