import com.mg.framework.*;
class server
{
public static void main(String gg[])
{
NFrameworkServer nfs=new NFrameworkServer();
nfs.registerClass(Calculator.class);
nfs.start();
}
}