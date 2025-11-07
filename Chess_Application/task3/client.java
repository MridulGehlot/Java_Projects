import com.mg.nframework.client.*;
public class client
{
public void checkStatus()
{
NFrameworkClient nfc=new NFrameworkClient();
String[][] pieces=chessBoard.getDS();
try
{
nfc.process("/server/stateChange",(Object)pieces);
}catch(Throwable t)
{
System.out.println(t);
}
}
private static ChessBoard chessBoard;
public static void main(String gg[])
{
Main m=new Main();
chessBoard=new ChessBoard(m);
chessBoard.setupPieces();
m.append(chessBoard);
m.addPlayer("Alice");
m.addPlayer("Bob");
m.addPlayer("Charlie");
client c=new client();
chessBoard.addClient(c);
chessBoard.updateBoard();
}
}