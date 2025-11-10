import java.util.function.*;
class psp
{
public static void main(String gg[])
{
BiConsumer<Integer,Integer> biConsumer=(x,y)->{
System.out.println("I am Biconsumer");
System.out.println("Consumed - "+x+" , "+y);
};
biConsumer.accept(10,20);


}
}