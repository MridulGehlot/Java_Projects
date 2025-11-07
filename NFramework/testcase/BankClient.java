import com.mg.nframework.client.*;
public class BankClient
{
public static void main(String gg[])
{
String city=gg[0];
try
{
NFrameworkClient client=new NFrameworkClient();
Object result=client.process("/Banking/branchName",city);
String branch=(String)result;
System.out.println(branch);
}catch(Throwable t)
{
System.out.println(t);
}
}
}