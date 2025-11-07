import javax.swing.*;
import java.awt.*;
public class Main extends JFrame
{
private ChessBoard chessBoard;
private JPanel rightPanel;
private JList<String> playerList;
private DefaultListModel<String> playerListModel;
private JButton undoButton;
private JButton doneButton;

public Main()
{
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int x,y,height,width;
x=d.width/2;
y=d.height/2;
width=840;
height=640;
setLocation(x-width/2,y-height/2);
setSize(width,height);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setLayout(new BorderLayout());
// Initialize right panel
setupRightPanel();

chessBoard = new ChessBoard(this);    // Pass Main instance if needed
    chessBoard.setupPieces();
    chessBoard.updateBoard();
    append(chessBoard);   

add(rightPanel, BorderLayout.EAST);
setVisible(true);
}
public void append(JPanel c)
{
add(c,BorderLayout.CENTER);
}
public void main(String gg[])
{
Main m=new Main();
chessBoard=new ChessBoard(m);
chessBoard.setupPieces();
chessBoard.updateBoard();
m.append(chessBoard);
}

private void setupRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(200, 640)); // Adjust width as needed
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.RED);

        // Top - Logout Button
        JButton logoutButton = new JButton("Logout");
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(logoutButton);
        rightPanel.add(topPanel, BorderLayout.NORTH);

        // Center - Player List
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        playerList.setVisibleRowCount(10);
        JScrollPane scrollPane = new JScrollPane(playerList);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom - Undo and Done Buttons
        undoButton = new JButton("Undo");
        doneButton = new JButton("Done");
undoButton.setEnabled(false);
doneButton.setEnabled(false);


undoButton.addActionListener(e -> {
    chessBoard.undoLastMove();
});

doneButton.addActionListener(e -> {
    chessBoard.finalizeMove();
});
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(undoButton);
        bottomPanel.add(doneButton);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
public void addPlayer(String name) {
        playerListModel.addElement(name);
System.out.println("Adding player: " + name);

    }
public void setUndoAndDoneEnabled(boolean state) {
    undoButton.setEnabled(state);
    doneButton.setEnabled(state);
}

}