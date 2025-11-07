import com.mg.nframework.client.*;
import javax.swing.*;
import java.awt.event.*;
public class client2
{
private Timer t;
private static ChessBoard chessBoard;
public client2()
{
t=new Timer(1000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(ask()) getNewDS();
}
});
t.start();
}
public static void stateChange(java.util.List<java.util.List<String>> arr)
{
chessBoard.updateDS(arr);
chessBoard.updateBoard();
}
public static void main(String gg[])
{
Main m=new Main();
chessBoard=new ChessBoard(m);
m.append(chessBoard);
client2 c2=new client2();
}
public boolean ask()
{
boolean result=false;
NFrameworkClient nfc=new NFrameworkClient();
try
{
result=(boolean)nfc.process("/server/isMoved");
}catch(Throwable t)
{
System.out.println(t);
}
return result;
}
public void getNewDS()
{
NFrameworkClient nfc=new NFrameworkClient();
java.util.List<java.util.List<String>> pieces=null;
try
{
pieces=( java.util.List<java.util.List<String>> )nfc.process("/server/getDS");
}catch(Throwable t)
{
System.out.println(t);
}
chessBoard.updateDS(pieces);
chessBoard.updateBoard();
}
}