class Medium
{
private int num;
private boolean b=false;
synchronized public void setNum(int n)
{
if(b==true)
{
try
{
wait();
}catch(InterruptedException e)
{}
}
num=n;
System.out.println("Produced - "+num);
b=true;
notify();
}
synchronized public int getNum()
{
if(b==false)
{
try
{
wait();
}catch(InterruptedException e)
{}
}
b=false;
notify();
System.out.println("Consumed - "+num);
return num;
}
}
class Producer extends Thread
{
private Medium mdm;
Producer(Medium m)
{
mdm=m;
start();
}
public void run()
{
for(int i=1;i<=50;i++) mdm.setNum(i);
}
}
class Consumer extends Thread
{
private Medium mdm;
Consumer(Medium m)
{
mdm=m;
start();
}
public void run()
{
for(int i=1;i<=50;i++) mdm.getNum();
}
}
class psp
{
public static void main(String gg[])
{
Medium m=new Medium();
Producer p=new Producer(m);
Consumer c=new Consumer(m);
}
}