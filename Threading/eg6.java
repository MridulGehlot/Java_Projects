import java.util.concurrent.*;
class psp
{
public static void main(String g[])
{
Callable<Integer> work=()->{
TimeUnit.SECONDS.sleep(5);
return 540;
};
ExecutorService es=Executors.newSingleThreadExecutor();
Future<Integer> res=es.submit(work);
System.out.println(res.isDone());
try
{
System.out.println(res.get());
}catch(Exception e)
{}
System.out.println(res.isDone());
es.shutdown();
}
}