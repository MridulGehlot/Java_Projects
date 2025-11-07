import com.mg.nframework.server.*;
import com.mg.nframework.server.annotations.*;
@Path("/server")
public class server
{
public static java.util.List<java.util.List<String>> ds;
public static boolean isMoved;
@Path("/isMoved")
public boolean isMoved()
{
return isMoved;
}
@Path("/getDS")
public java.util.List<java.util.List<String>> getDS()
{
isMoved=false;
return ds;
}
@Path("/stateChange")
public void stateChange(java.util.List<java.util.List<String>> dataStructure)
{
isMoved=true;
ds=dataStructure;
}
public static void main(String gg[])
{
isMoved=false;
ds=null;
NFrameworkServer nfs=new NFrameworkServer();
nfs.registerClass(server.class);
nfs.start();
}
}