import com.mg.nframework.server.annotations.*;
@Path("/Calculator")
public class Calculator
{
@Path("/add")
public int add(int a,int b)
{
return a+b;
}
@Path("/sub")
public int sub(int a,int b)
{
return a-b;
}
}