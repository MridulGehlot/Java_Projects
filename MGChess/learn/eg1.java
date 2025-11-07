import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
class MyModel extends AbstractTableModel
{
private Object data[][];
private String title [];
MyModel()
{
data=new Object[2][2];
data[0][0]="One";
data[0][1]=new JButton("Button One");
data[1][0]="Two";
data[1][1]=new JButton("Button Two");
title=new String[2];
title[0]="A";
title[1]="B";
}
public int getRowCount()
{
return this.data.length;
}
public int getColumnCount()
{
return this.title.length;
}
public Object getValueAt(int r,int c)
{
return data[r][c];
}
public boolean isCellEditable(int r,int c)
{
if(c==1) return true;
return false;
}
public Class getColumnClass(int c)
{
return data[c].getClass();
}
public String getClassName(int c)
{
return data[c].getClass().getName();
}
public void setValueAt(Object data,int r,int c)
{
System.out.println(data+","+r+","+c);
}
}
class Whatever extends JFrame
{
private JTable table;
private MyModel model;
private Container container;
Whatever()
{
model=new MyModel();
table=new JTable(model);
table.getColumn("B").setCellRenderer(new ButtonRenderer());
table.getColumn("B").setCellEditor(new ButtonEditor());
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(table,BorderLayout.CENTER);
setSize(400,500);
setLocation(10,10);
setVisible(true);
}

class ButtonRenderer implements TableCellRenderer
{
public Component getTableCellRendererComponent(JTable table,Object value,boolean a,boolean b,int r,int c)
{
//System.out.println(value);
return (JButton)value;
}
}
class ButtonEditor extends DefaultCellEditor
{
private JButton button;
private boolean isClicked;
private int r,c;
ButtonEditor()
{
super(new JCheckBox());
button=new JButton("HI");
button.setOpaque(true);
button.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
fireEditingStopped();
}
});
}
public Component getTableCellEditorComponent(JTable table,Object value,boolean a,int r,int c)
{
this.r=r;
this.c=c;
button.setForeground(Color.black);
button.setBackground(UIManager.getColor("Button.background"));
this.isClicked=true;
return button;
}
public Object getCellEditorValue()
{
return "Cool";
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

public static void main(String gg[])
{
Whatever w=new Whatever();
}
}