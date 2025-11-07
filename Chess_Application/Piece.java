import javax.swing.*;
public class Piece
{
private Type type;
private COLOR color;
private ImageIcon icon;
public Piece(Type type,COLOR color,String iconPath)
{
this.type=type;
this.color=color;
this.icon=new ImageIcon(iconPath);
}
public Type getType() { return type; }
public COLOR getColor() { return color; }
public ImageIcon getIcon() { return icon; }
public boolean isValidMove(int fromRow,int fromCol,int toRow,int toCol,Piece[][] board)
{
if(fromRow==toRow && fromCol==toCol) return false;
int dr=toRow-fromRow;
int dc=toCol-fromCol;
switch(type)
{
case PAWN:
int dir=(color==COLOR.WHITE?-1:1);
// Normal move
if(dc==0 && board[toRow][toCol]==null)
{
if(dr==dir) return true;
// First move double step
if((color==COLOR.WHITE && fromRow==6 || color==COLOR.BLACK && fromRow==1) && dr==2*dir && board[fromRow+dir][fromCol]==null) return true;
}
// Capture
if(Math.abs(dc)==1 && dr==dir && board[toRow][toCol]!= null && board[toRow][toCol].color!=color) return true;
return false;

case ROOK:
if(dr==0 || dc==0) 
{
return isPathClear(fromRow,fromCol,toRow,toCol,board);
}
return false;

case BISHOP:
if(Math.abs(dr)==Math.abs(dc))
{
return isPathClear(fromRow,fromCol,toRow,toCol,board);
}
return false;

case QUEEN:
if(dr==0 || dc==0 || Math.abs(dr)==Math.abs(dc))
{
return isPathClear(fromRow,fromCol,toRow,toCol,board);
}
return false;

case KING:
if(Math.abs(dr)<=1 && Math.abs(dc)<=1)
{
return true;
}
return false;

case KNIGHT:
if((Math.abs(dr)==2 && Math.abs(dc)==1) || (Math.abs(dr)==1 && Math.abs(dc)==2))
{
return true;
}
return false;
}//switch case ends here
return false; //as default
}//valid ends here

private boolean isPathClear(int fromRow,int fromCol,int toRow,int toCol,Piece[][] board)
{
int dr=Integer.compare(toRow,fromRow);
int dc=Integer.compare(toCol,fromCol);
int r=fromRow+dr;
int c=fromCol+dc;
while(r!= toRow || c!=toCol)
{
if(board[r][c]!=null) return false;
r+=dr;
c+=dc;
}
return true;
}
}