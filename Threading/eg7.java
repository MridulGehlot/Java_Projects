interface MyCallable
{
int call();
}
class Service
{
public Future submit(MyCallable work)
{
return new Future(work);
}
}
class Future
{
private boolean status;
private int result;
Thread t;
Future(MyCallable work)
{
this.status=false;
this.result=0;
this.t=new Thread(()->{
int res=work.call();
setResult(res);
});
t.start();
}
synchronized public boolean isDone()
{
return status;
}
synchronized public int get()
{
if(this.status==false) 
{
try
{
wait();
}catch(Exception e){}
}
return result;
}
synchronized public void setResult(int n)
{
this.result=n;
this.status=true;
notify();
}
}
class psp
{
public static void main(String gg[])
{
Service s=new Service();
Future f=s.submit(()->{
try
{
Thread.sleep(5000);
}catch(Exception e){}

System.out.println("Working");
return 2543;
});
System.out.println(f.isDone());
System.out.println(f.get());
System.out.println(f.isDone());
}
}