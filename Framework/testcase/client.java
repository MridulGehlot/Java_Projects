import com.mg.framework.*;
class client
{
public static void main(String gg[])
{
NFrameworkClient nfc=new NFrameworkClient();
Object result=nfc.process("/Calculator/add",10,40);
Integer ii=(Integer)result;
System.out.println(ii);
}
}