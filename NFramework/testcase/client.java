import com.mg.nframework.client.*;
class client
{
public static void main(String gg[])
{
NFrameworkClient nfc=new NFrameworkClient();
Object result=null;
try
{
result=nfc.process("/Calculator/add",10,40);
}catch(Throwable t)
{
System.out.println(t);
}
if (result instanceof Number) {
    Number num = (Number) result;
    int ii = num.intValue();
    System.out.println(ii);
} else {
    System.out.println("Result is not a number: " + result);
}
}
}