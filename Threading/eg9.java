import java.util.concurrent.*;
import java.util.concurrent.locks.*;
class Common
{
private String k;
private ReentrantLock lock=new ReentrantLock();
public void keep(String s)
{
lock.lock();
k=s;
System.out.println(k);
try
{
Thread.sleep(1000);
}catch(Exception e){}
System.out.println(k);
lock.unlock();
}
}
class psp
{
public static void main(String gg[])
{
Common m=new Common();
ExecutorService es=Executors.newFixedThreadPool(3);
es.submit(()->{
m.keep("Hello");
});
es.submit(()->{
m.keep("Boys");
});
es.submit(()->{
m.keep("Girls");
});
es.shutdown();
}
}