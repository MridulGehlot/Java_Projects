import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import com.mg.hr.dl.interfaces.dao.*;
import com.mg.hr.dl.interfaces.dto.*;
import com.mg.hr.dl.dao.*;
import com.mg.hr.dl.dto.*;
import com.mg.hr.dl.exceptions.*;
class DesignationTableModel extends AbstractTableModel
{
private java.util.List<DesignationDTOInterface> designations;
private String title[];
DesignationTableModel()
{
populateDS();
}
public int getColumnCount()
{
return title.length;
}
public int getRowCount()
{
return designations.size();
}
public String getColumnName(int columnIndex)
{
return title[columnIndex];
}
public Class getColumnClass(int columnIndex)
{
Class c=null;
try
{
if(columnIndex==0 || columnIndex==1) c=Class.forName("java.lang.Integer");
else c=Class.forName("java.lang.String");
}catch(Exception e)
{
//Nothing will happen
}
return c;
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0)
{
return new Integer(rowIndex+1);
}
DesignationDTOInterface designation;
designation=designations.get(rowIndex);
if(columnIndex==1) return designation.getCode();
if(columnIndex==2) return designation.getTitle();
return null;
}
public boolean isCellEditable(int r,int c)
{
return false;
}
private void populateDS()
{
title=new String[3];
title[0]="S.no.";
title[1]="Code";
title[2]="Designaiton";
try
{
Set<DesignationDTOInterface> set;
DesignationDAOInterface designationDAO=new DesignationDAO();
set=designationDAO.getAll();
designations=new ArrayList<>(set);
}catch(DAOException dao)
{
System.out.println(dao.getMessage());
}
}
}
class firstUI extends JFrame
{
private JTable table;
private JScrollPane jsp;
private Container container;
private DesignationTableModel dtm;
firstUI()
{
dtm=new DesignationTableModel();
table=new JTable(dtm);
table.setFont(new Font("Times New Roman",Font.PLAIN,24));
table.setRowHeight(30);
JTableHeader header=table.getTableHeader();
header.setFont(new Font("Verdana",Font.BOLD,26));
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
container.setLayout(new FlowLayout());
container.add(jsp);
int width,height;
width=600;
height=600;
setSize(width,height);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
setLocation(x,y);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
}
class firstUIpsp
{
public static void main(String gg[])
{
firstUI a=new firstUI();
}
}