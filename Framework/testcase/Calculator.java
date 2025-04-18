import com.mg.framework.*;
@Path("/Calculator")
public class Calculator
{
public int add(int a,int b)
{
return a+b;
}
public int sub(int a,int b)
{
return a-b;
}
}