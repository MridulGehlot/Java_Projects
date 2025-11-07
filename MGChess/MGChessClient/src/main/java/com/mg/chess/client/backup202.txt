package com.mg.chess.client;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.mg.nframework.client.*;
import java.util.*;
public class ChessUI extends JFrame
{
private String username;
private JTable availableMembers;
private JScrollPane availalbleMembersScrollPane;
private AvailableMembersModel availableMembersModel;
private NFrameworkClient client;
private Container container;
private javax.swing.Timer timer;
ChessUI(String username)
{
super("Member : "+username);
this.client=new NFrameworkClient();
this.username=username;
initComponents();
setAppearance();
addListeners();
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width,height;
width=500;
height=400;
setSize(width,height);
setLocation(d.width/2-width/2,d.height/2-height/2);
}
private void initComponents()
{
JPanel p1=new JPanel();
p1.setLayout(new BorderLayout());
p1.add(new JLabel("Members"),BorderLayout.NORTH);
availableMembersModel=new AvailableMembersModel();
availableMembers=new JTable(availableMembersModel);
availableMembers.getColumn(" ").setCellRenderer(new AvailableMembersRenderer());
availableMembers.getColumn(" ").setCellEditor(new AvailableMembersEditor());

availalbleMembersScrollPane= new JScrollPane(availableMembers,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

p1.add(availableMembers);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(p1,BorderLayout.EAST);
}
private void setAppearance()
{
//nothing right now
}
private void addListeners()
{
timer=new javax.swing.Timer(3000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
try
{
timer.stop();
java.util.List<String> members=(java.util.List<String>)client.process("/MGChessServer/getAvailableMembers",username);
ChessUI.this.availableMembersModel.setMembers(members);
timer.start();
}catch(Throwable t)
{
JOptionPane.showMessageDialog(ChessUI.this,t.toString());
}
}
});
addWindowListener(new WindowAdapter(){
public void windowClosing(WindowEvent e)
{
try
{
client.process("/MGChessServer/logout",username);
System.exit(0);
}catch(Throwable t)
{
JOptionPane.showMessageDialog(ChessUI.this,t.toString());
}
}
});
timer.start();
}
public void showUI()
{
setVisible(true);
}
private void sendInvitation(String toUsername)
{
try
{
client.process("/MGChessServer/invite",username,toUsername);
JOptionPane.showMessageDialog(this,"Invitation for game sent to "+toUsername);
}catch(Throwable t)
{
JOptionPane.showMessageDialog(this,t.toString());
}
}
//Inner Classes Starts Here
class AvailableMembersModel extends AbstractTableModel
{
private java.util.List<String> members;
private String title[]={"Members"," "};
private java.util.List<JButton> inviteButtons;
private boolean awaitingInvitationReply;
AvailableMembersModel()
{
awaitingInvitationReply=false;
inviteButtons=new LinkedList<>();
members=new LinkedList<>();
}
public int getRowCount()
{
return this.members.size();
}
public int getColumnCount()
{
return this.title.length;
}
public String getColumnName(int c)
{
return title[c];
}
public void setValueAt(Object data,int r,int c)
{
if(c==1)
{
JButton button=this.inviteButtons.get(r);
String text=(String)data;
button.setText(text);
if(text.equalsIgnoreCase("Invited"))
{
awaitingInvitationReply=true;
for(JButton b:inviteButtons) b.setEnabled(false);
this.fireTableDataChanged();
ChessUI.this.sendInvitation(this.members.get(r));
}
else if(text.equalsIgnoreCase("Invite"))
{
awaitingInvitationReply=false;
for(JButton b:inviteButtons) b.setEnabled(true);
this.fireTableDataChanged();
}
}
}
public Object getValueAt(int r,int c)
{
if(c==0) return this.members.get(r);
return this.inviteButtons.get(r);
}
public boolean isCellEditable(int r,int c)
{
if(c==1) return true;
return false;
}
public Class getColumnClass(int c)
{
if(c==0) return String.class;
return JButton.class;
}
public void setMembers(java.util.List<String> members)
{
if(awaitingInvitationReply) return;
this.members=members;
this.inviteButtons.clear();
for(int i=0;i<members.size();i++) this.inviteButtons.add(new JButton("Invite"));
fireTableDataChanged();
}
}

class AvailableMembersRenderer implements TableCellRenderer
{
public Component getTableCellRendererComponent(JTable table,Object value,boolean a,boolean b,int r,int c)
{
//System.out.println(value);
return (JButton)value;
}
}
class AvailableMembersEditor extends DefaultCellEditor
{
private boolean isClicked;
private int r,c;
private JButton button;
private ActionListener actionListener;
AvailableMembersEditor()
{
super(new JCheckBox());
this.actionListener=new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
fireEditingStopped();
}
};
}
public Component getTableCellEditorComponent(JTable table,Object value,boolean a,int r,int c)
{
this.r=r;
this.c=c;
this.button=(JButton)availableMembersModel.getValueAt(r,c);
this.button.removeActionListener(this.actionListener);
this.button.addActionListener(this.actionListener);
button.setForeground(Color.black);
button.setBackground(UIManager.getColor("Button.background"));
button.setOpaque(true);
this.isClicked=true;
return button;
}
public Object getCellEditorValue()
{
return "Invited";
}
public boolean stopCellEditing()
{
isClicked=false;
return super.stopCellEditing();
}
public void fireEditingStopped()
{
//do required
super.fireEditingStopped();
}
}
//Inner Calsses Ends Here

}