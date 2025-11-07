import com.mg.nframework.server.*;
import com.mg.nframework.server.annotations.*;
@Path("/server")
public class server
{
private static ChessBoard chessBoard;
@Path("/stateChange")
public static void stateChange(java.util.List<java.util.List<String>> arr)
{
chessBoard.updateDS(arr);
chessBoard.updateBoard();
}
public static void main(String gg[])
{
Main m=new Main();
chessBoard=new ChessBoard();
m.append(chessBoard);
NFrameworkServer nfs=new NFrameworkServer();
nfs.registerClass(server.class);
nfs.start();
}
}