interface abcd
{
public void doIt();
}
class pqrs
{
public static void sam()
{
System.out.println("Hii From Sam");
}
public static void tom()
{
System.out.println("I Am Tom");
}
}
class xyz
{
public void tommy(abcd a)
{
a.doIt();
}
}
class psp
{
public static void main(String gg[])
{
abcd a1=pqrs::sam;
abcd a2=pqrs::tom;
a1.doIt();
a2.doIt();
xyz xx=new xyz();
xx.tommy(pqrs::sam);
}
}