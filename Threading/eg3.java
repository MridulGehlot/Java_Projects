class Common
{
private String something;
/*
synchronized public void keep(String item)
{}
*/
public void keep(String item)
{
this.something=item;
System.out.println("I have Kept "+this.something+" ,now i am tired and i am going to sleep");
try
{
Thread.sleep(1000);
}catch(InterruptedException ie)
{}
System.out.println("I am awaken i am picking what i kept "+this.something);
}
}
class Worker extends Thread
{
private String item;
private Common place;
Worker(String item,Common place)
{
this.item=item;
this.place=place;
start();
}
public void run()
{
synchronized(this.place)
{
this.place.keep(this.item);
}
}
}
class psp
{
public static void main(String gg[])
{
Common c=new Common();
Worker w1=new Worker("Gold",c);
Worker w2=new Worker("Silver",c);
Worker w3=new Worker("Bronze",c);
}
}