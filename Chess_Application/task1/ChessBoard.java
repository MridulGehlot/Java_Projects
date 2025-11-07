import javax.swing.*;
import java.awt.*;
import java.util.*;
public class ChessBoard extends JPanel
{
private client c;
private JButton squares[][]= new JButton[8][8];
private Piece pieces[][]=new Piece[8][8]; 
private int selectedRow=-1;
private int selectedCol=-1;
private COLOR currentTurn=COLOR.WHITE;
public ChessBoard()
{
this.c=null;
setLayout(new GridLayout(8,8));
initComponents();
}
private void initComponents()
{
for(int i=0;i<8;i++)
{
for(int j=0;j<8;j++)
{
JButton b=new JButton();
if((i+j)%2==0) b.setBackground(Color.GRAY);
else b.setBackground(Color.LIGHT_GRAY);
squares[i][j]=b;
add(b);
final int row=i;
final int col=j;
b.addActionListener(e->handleSquareClick(row,col));
}
}
}
private void handleSquareClick(int row,int col)
{
if(selectedRow==-1 && pieces[row][col]!=null && pieces[row][col].getColor()==currentTurn)
{
selectedRow=row;
selectedCol=col;
squares[row][col].setBackground(Color.BLUE);
}
else if(selectedRow!=-1)
{
if(row!=selectedRow || col!=selectedCol)
{
Piece p=pieces[selectedRow][selectedCol];
if(p!=null && (pieces[row][col]==null || pieces[row][col].getColor()!=p.getColor()) && p.isValidMove(selectedRow,selectedCol,row,col,pieces))
{
pieces[row][col]=p;
pieces[selectedRow][selectedCol]=null;
//Switch Turn
currentTurn = currentTurn==COLOR.WHITE?COLOR.BLACK:COLOR.WHITE;
}//valid move so capture
}//not same selection
resetSquareColors();
selectedRow = selectedCol = -1;
updateBoard();
}//new move
}
public void setupPieces()
{
//pawns
for(int i=0;i<8;i++)
{
Piece b=new Piece(Type.PAWN,COLOR.BLACK,"Black_Pawn.png");
Piece w=new Piece(Type.PAWN,COLOR.WHITE,"White_Pawn.png");
pieces[1][i]=b;
pieces[6][i]=w;
}
// Rooks
pieces[0][0] = new Piece(Type.ROOK, COLOR.BLACK, "Black_Rook.png");
pieces[0][7] = new Piece(Type.ROOK, COLOR.BLACK, "Black_Rook.png");
pieces[7][0] = new Piece(Type.ROOK, COLOR.WHITE, "White_Rook.png");
pieces[7][7] = new Piece(Type.ROOK, COLOR.WHITE, "White_Rook.png");
// Knights
pieces[0][1] = new Piece(Type.KNIGHT, COLOR.BLACK, "Black_Knight.png");
pieces[0][6] = new Piece(Type.KNIGHT, COLOR.BLACK, "Black_Knight.png");
pieces[7][1] = new Piece(Type.KNIGHT, COLOR.WHITE, "White_Knight.png");
pieces[7][6] = new Piece(Type.KNIGHT, COLOR.WHITE, "White_Knight.png");
// Bishops
pieces[0][2] = new Piece(Type.BISHOP, COLOR.BLACK, "Black_Bishop.png");
pieces[0][5] = new Piece(Type.BISHOP, COLOR.BLACK, "Black_Bishop.png");
pieces[7][2] = new Piece(Type.BISHOP, COLOR.WHITE, "White_Bishop.png");
pieces[7][5] = new Piece(Type.BISHOP, COLOR.WHITE, "White_Bishop.png");
// Queens
pieces[0][3] = new Piece(Type.QUEEN, COLOR.BLACK, "Black_Queen.png");
pieces[7][3] = new Piece(Type.QUEEN, COLOR.WHITE, "White_Queen.png");
// Kings
pieces[0][4] = new Piece(Type.KING, COLOR.BLACK, "Black_King.png");
pieces[7][4] = new Piece(Type.KING, COLOR.WHITE, "White_King.png");
}
public void updateBoard()
{
for(int i=0;i<8;i++)
{
for(int j=0;j<8;j++)
{
if(pieces[i][j]!=null)
{
squares[i][j].setIcon(pieces[i][j].getIcon());
}
else squares[i][j].setIcon(null);
}
}
if(this.c!=null) c.checkStatus();
}
private void resetSquareColors()
{
for(int i=0;i<8;i++)
{
for(int j=0;j<8;j++)
{
squares[i][j].setBackground((i + j)%2==0?Color.GRAY:Color.LIGHT_GRAY);
}
}
}
public void updateDS(java.util.List<java.util.List<String>> arr)
{
for(int i=0;i<arr.size();i++)
{
java.util.List<String> row=arr.get(i);
for(int j=0;j<row.size();j++)
{
pieces[i][j]=null;
String tmp=row.get(j);
if(tmp!=null)
{
String path;
char colour=tmp.charAt(0);
char type=tmp.charAt(1);
COLOR c=null;
Type t=null;
if(colour=='W')
{
c=COLOR.WHITE;
path="White_";
}
else
{
c=COLOR.BLACK;
path="Black_";
}
if(type=='K')
{
t=Type.KING;
path+="King";
}
if(type=='Q') 
{
t=Type.QUEEN;
path+="Queen";
}
if(type=='R')
{
t=Type.ROOK;
path+="Rook";
}
if(type=='B') 
{
t=Type.BISHOP;
path+="Bishop";
}
if(type=='T')
{
t=Type.KNIGHT;
path+="Knight";
}
if(type=='P')
{
t=Type.PAWN;
path+="Pawn";
}
path+=".png";
Piece p=new Piece(t,c,path);
pieces[i][j]=p;
}
}
}
}
public String[][] getDS()
{
String arr[][]=new String[8][8];
String tmp;
for(int i=0;i<8;i++)
{
for(int j=0;j<8;j++)
{
Piece p=pieces[i][j];
if(p!=null)
{
if(p.getColor()==COLOR.WHITE) tmp="W";
else tmp="B";
Type t=p.getType();
if(t==Type.KING) tmp+="K";
if(t==Type.QUEEN) tmp+="Q";
if(t==Type.ROOK) tmp+="R";
if(t==Type.BISHOP) tmp+="B";
if(t==Type.KNIGHT) tmp+="T";
if(t==Type.PAWN) tmp+="P";
arr[i][j]=tmp;
}
}
}
return arr;
}
public void addClient(client c)
{
this.c=c;
}
}