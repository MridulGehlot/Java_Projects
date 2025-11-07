import java.util.concurrent.*;
class psp
{
public static void main(String gg[])
{
ExecutorService es=Executors.newSingleThreadExecutor();
es.submit(()->{
for(int i=0;i<=100;i++) System.out.print(i+" ");
});
for(int i=200;i<=300;i++) System.out.print(i+" ");
es.shutdown();
}
}