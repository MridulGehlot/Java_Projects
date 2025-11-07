import com.mg.nframework.server.*;
import com.mg.nframework.server.annotations.*;
@Path("/Banking")
public class Bank
{
public Bank()
{
}
@Path("/branchName")
public String getBranchName(String city)
{
if(city.equals("Ujjain")) return "Freeganj";
else if(city.equals("Mumbai")) return "Colaba";
else return "No Branch in that City";
}
public static void main(String gg[])
{
NFrameworkServer server=new NFrameworkServer();
server.registerClass(Bank.class);
server.start();
}
}