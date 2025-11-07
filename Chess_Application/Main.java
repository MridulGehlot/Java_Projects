import javax.swing.*;
import java.awt.*;
public class Main extends JFrame
{
private ChessBoard chessBoard;
public Main()
{
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int x,y,height,width;
x=d.width/2;
y=d.height/2;
width=640;
height=640;
setLocation(x-width/2,y-height/2);
setSize(width,height);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setLayout(new BorderLayout());
chessBoard=new ChessBoard();
add(chessBoard);
}
public static void main(String gg[])
{
Main m=new Main();
}
}