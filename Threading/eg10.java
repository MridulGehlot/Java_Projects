import java.util.concurrent.*;
class Common
{
private int num;
private boolean flag=false;
synchronized public void set(int n)
{
try
{
if(flag) wait();
}catch(InterruptedException e){}
System.out.println("Produced - "+n);
num=n;
flag=true;
notify();
}
synchronized public int get()
{
try
{
if(!flag) wait();
}catch(InterruptedException e){}
System.out.println("Consumed - "+num);
flag=false;
notify();
return num;
}
}
class psp
{
public static void main(String gg[])
{
Common common=new Common();
ExecutorService es=Executors.newFixedThreadPool(2);
es.submit(()->{
for(int i=1;i<=50;i++) common.set(i);
});
es.submit(()->{
for(int i=1;i<=50;i++) common.get();
});
es.shutdown();
}
}