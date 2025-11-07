package com.mg.chess.client;
import com.mg.nframework.client.*;
public class Main
{
public static void main(String gg[])
{
try
{
String username=gg[0];
String password=gg[1];
NFrameworkClient nfc=new NFrameworkClient();
boolean authentic=(boolean)nfc.process("/MGChessServer/login",username,password);
if(authentic)
{
ChessUI chessUI=new ChessUI(username);
chessUI.showUI();
}
else System.out.println("Opps!! invalid credentials");
}catch(Throwable t)
{
System.out.println(t.getMessage());
}

}
}