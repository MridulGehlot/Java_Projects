class Worker extends Thread
{
private int data;
private boolean jobDone;
Worker()
{
start();
}
public void run()
{
try
{
Thread.sleep(4000);
}catch(Exception e){}
data=540;
jobDone=true;
}
public int getData()
{
while(jobDone==false)
{
try
{
Thread.sleep(1000);
}catch(Exception e){}
}
return data;
}
public boolean isDone()
{
return jobDone;
}
}
class psp
{
public static void main(String gg[])
{
Worker w=new Worker();
System.out.println(w.isDone());
System.out.println(w.getData());
System.out.println(w.isDone());
}
}