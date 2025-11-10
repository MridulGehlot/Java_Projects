interface Take2Give1<V1,V2,RV>
{
public RV doIt(V1 a,V2 b);
}
class Whatever
{
public int sum(int a,int b)
{
return a+b;
}
public String modify(char a,int f)
{
String s="";
for(int i=0;i<f;i++) s+=a;
return s;
}
}
class psp
{
public static void main(String gg[])
{
Whatever w=new Whatever();
Take2Give1<Integer,Integer,Integer> m1=w::sum;
System.out.println(m1.doIt(23,14));
Take2Give1<Character,Integer,String> m2=w::modify;
System.out.println(m2.doIt('Z',14));
}
}