class aaa implements Runnable
{
aaa()
{
Thread t=new Thread(this);
t.start();
}
public void run()
{
for(int i=410;i<500;i++) System.out.print(i+" ");
}
}
class psp
{
public static void main(String gg[])
{
aaa a=new aaa();
for(int i=0;i<100;i++) System.out.print(i+" ");
}
}