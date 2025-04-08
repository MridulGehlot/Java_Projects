package com.mg.hr.pl.ui;
import com.mg.hr.pl.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.exceptions.*;
import javax.swing.table.*;
import java.io.*;
public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private JLabel titleLabel;
private JLabel searchLabel;
private JTextField searchTextField;
private JLabel searchNotFoundLabel;
private JButton cancelSearchButton;
private JTable table;
private DesignationModel dm;
private JScrollPane scrollPane;
private DesignationPanel designationPanel;
private Container container;
private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF}
private MODE mode;
private ImageIcon logoIcon,addIcon,saveIcon,editIcon,cancelIcon,undoIcon,deleteIcon,pdfIcon;
public DesignationUI()
{
initComponents();
setAppereance();
addListeners();
setViewMode();
designationPanel.setViewMode();
setIconImage(logoIcon.getImage());
}
private void initComponents()
{
logoIcon=new ImageIcon(DesignationUI.class.getResource("/images/my.png"));
addIcon=new ImageIcon(DesignationUI.class.getResource("/images/add.png"));
saveIcon=new ImageIcon(DesignationUI.class.getResource("/images/save.png"));
editIcon=new ImageIcon(DesignationUI.class.getResource("/images/edit.png"));
undoIcon=new ImageIcon(DesignationUI.class.getResource("/images/undo.png"));
cancelIcon=new ImageIcon(DesignationUI.class.getResource("/images/cancel.png"));
deleteIcon=new ImageIcon(DesignationUI.class.getResource("/images/delete.png"));
pdfIcon=new ImageIcon(DesignationUI.class.getResource("/images/pdf.png"));
titleLabel=new JLabel("Designations");
searchLabel=new JLabel("Search");
searchTextField=new JTextField();
searchNotFoundLabel=new JLabel("");
cancelSearchButton=new JButton(cancelIcon);
dm=new DesignationModel();
table=new JTable(dm);
scrollPane=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
}
private void setAppereance()
{
Font titleLabelFont=new Font("Verdana",Font.BOLD,22);
Font searchLabelFont=new Font("Verdana",Font.BOLD,20);
Font searchTextFieldFont=new Font("Verdana",Font.PLAIN,20);
titleLabel.setFont(titleLabelFont);
searchLabel.setFont(searchLabelFont);
searchTextField.setFont(searchTextFieldFont);
searchNotFoundLabel.setFont(new Font("Verdana",Font.BOLD,14));
searchNotFoundLabel.setForeground(Color.red);
int lm=10;
int tm=10;

table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
JTableHeader tHeader=table.getTableHeader();
tHeader.setFont(new Font("Times New Roman",Font.BOLD,14));
tHeader.setReorderingAllowed(false);
tHeader.setResizingAllowed(false);
table.setRowSelectionAllowed(true);
table.getColumnModel().getColumn(0).setPreferredWidth(20);
table.getColumnModel().getColumn(1).setPreferredWidth(300);
table.setColumnSelectionAllowed(false);

designationPanel=new DesignationPanel();
designationPanel.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));

titleLabel.setBounds(lm,tm,300,30);
searchLabel.setBounds(lm,tm+40+10,200,30);
searchTextField.setBounds(lm+90,tm+50,330,30);
searchNotFoundLabel.setBounds(lm+400-70,tm+10,160,60);
cancelSearchButton.setBounds(lm+300+20+100+10,tm+55,20,20);
scrollPane.setBounds(lm,tm+90,470,200);
designationPanel.setBounds(lm,tm+300,470,200);
container=getContentPane();
container.setLayout(null);
container.add(titleLabel);
container.add(scrollPane);
container.add(searchLabel);
container.add(searchTextField);
container.add(searchNotFoundLabel);
container.add(cancelSearchButton);
container.add(designationPanel);
int width=500;
int height=580;
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation((d.width/2)-(width/2),(d.height/2)-(height/2));
setSize(width,height);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
private void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
cancelSearchButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
searchTextField.setText("");
searchTextField.requestFocus();
}
});
table.getSelectionModel().addListSelectionListener(this);
}
public void changedUpdate(DocumentEvent de)
{
searchDesignation();
}
public void insertUpdate(DocumentEvent de)
{
searchDesignation();
}
public void removeUpdate(DocumentEvent de)
{
searchDesignation();
}
public void searchDesignation()
{
searchNotFoundLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0) return;
int index;
try
{
index=dm.indexOfTitle(title,true);
}catch(BLException bl)
{
searchNotFoundLabel.setText("Not Found");
return;
}
table.setRowSelectionInterval(index,index);
Rectangle rectangle=table.getCellRect(index,0,true);
table.scrollRectToVisible(rectangle);
}
public void valueChanged(ListSelectionEvent ev)
{
int index=table.getSelectedRow();
try
{
DesignationInterface d=dm.getDesignationAt(index);
designationPanel.setDesignation(d);
}catch(BLException ble)
{
designationPanel.clearDesignation();
}
}
private void setViewMode()
{
this.mode=MODE.VIEW;
if(dm.getRowCount()==0)
{
searchTextField.setEnabled(false);
cancelSearchButton.setEnabled(false);
table.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
cancelSearchButton.setEnabled(true);
table.setEnabled(true);
}
}
private void setAddMode()
{
this.mode=MODE.ADD;
searchTextField.setEnabled(false);
cancelSearchButton.setEnabled(false);
table.setEnabled(false);
}
private void setEditMode()
{
this.mode=MODE.EDIT;
searchTextField.setEnabled(false);
cancelSearchButton.setEnabled(false);
table.setEnabled(false);
}
private void setDeleteMode()
{
this.mode=MODE.DELETE;
searchTextField.setEnabled(false);
cancelSearchButton.setEnabled(false);
table.setEnabled(false);
}
private void setExportToPDFMode()
{
this.mode=MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
cancelSearchButton.setEnabled(false);
table.setEnabled(false);
}
//Inner Class
class DesignationPanel extends JPanel
{
private JLabel titleLabel;
private JLabel titleCaptionLabel;
private JTextField titleTextField;
private JButton clearTitleTextFieldButton;
private JButton addButton,editButton,cancelButton,deleteButton,exportToPDFButton;
private JPanel buttonsPanel;
private DesignationInterface designation;
DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
initComponents();
setAppearance();
addListeners();
}
private void initComponents()
{
designation=null;
titleLabel=new JLabel("Designation");
titleCaptionLabel=new JLabel("");
titleTextField=new JTextField();
clearTitleTextFieldButton=new JButton(cancelIcon);
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
cancelButton=new JButton(undoIcon);
deleteButton=new JButton(deleteIcon);
exportToPDFButton=new JButton(pdfIcon);
buttonsPanel=new JPanel();
}
private void setAppearance()
{
titleLabel.setFont(new Font("Verdana",Font.BOLD,16));
titleCaptionLabel.setFont(new Font("Verdana",Font.PLAIN,16));
titleTextField.setFont(new Font("Verdana",Font.PLAIN,16));
titleTextField.setVisible(false);
cancelButton.setEnabled(false);
clearTitleTextFieldButton.setEnabled(false);
setLayout(null);
int lm,tm;
lm=0;
tm=0;
titleLabel.setBounds(lm+10,tm+20,150,30);
titleCaptionLabel.setBounds(lm+10+120+5,tm+20,200,30);
titleTextField.setBounds(lm+10+120+5,tm+20,250,30);
clearTitleTextFieldButton.setBounds(lm+10+300+90,tm+20,30,30);
buttonsPanel.setBounds(lm+10,tm+70,450,110);
buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.red));
addButton.setBounds(50,25,50,50);
editButton.setBounds(70+60,25,50,50);
cancelButton.setBounds(70+50+20+50+20,25,50,50);
deleteButton.setBounds(70+50+20+50+20+50+25,25,50,50);
exportToPDFButton.setBounds(70+50+20+50+20+50+70+30,25,50,50);
buttonsPanel.setLayout(null);
buttonsPanel.add(addButton);
buttonsPanel.add(editButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(exportToPDFButton);
add(titleLabel);
add(titleCaptionLabel);
add(clearTitleTextFieldButton);
add(titleTextField);
add(buttonsPanel);
}
private void addListeners()
{
addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setAddMode();
}
else
{
if(addDesignation()) setViewMode();
}
}
});
editButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setEditMode();
}
else
{
if(updateDesignation()) setViewMode();
}
}
});
clearTitleTextFieldButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
titleTextField.setText("");
titleTextField.requestFocus();
}
});
cancelButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
setViewMode();
}
});
deleteButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
setDeleteMode();
}
});
exportToPDFButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
exportToPDFDesignation();
}
});

}
public void setDesignation(DesignationInterface designation)
{
this.designation=designation;
titleCaptionLabel.setText(this.designation.getTitle());
}
public void clearDesignation()
{
this.designation=null;
titleCaptionLabel.setText("");
}
private void setViewMode()
{
DesignationUI.this.setViewMode();
this.titleCaptionLabel.setVisible(true);
this.titleTextField.setVisible(false);
this.addButton.setVisible(true);
this.addButton.setEnabled(true);
this.cancelButton.setEnabled(false);
this.clearTitleTextFieldButton.setEnabled(false);
addButton.setIcon(addIcon);
editButton.setIcon(editIcon);
if(dm.getRowCount()>0)
{
editButton.setEnabled(true);
deleteButton.setEnabled(true);
exportToPDFButton.setEnabled(true);
}
else
{
editButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
}
private void setAddMode()
{
DesignationUI.this.setAddMode();
titleCaptionLabel.setVisible(false);
titleTextField.setVisible(true);
titleTextField.setText("");
cancelButton.setEnabled(true);
clearTitleTextFieldButton.setEnabled(true);
editButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
addButton.setIcon(saveIcon);
titleTextField.requestFocus();
}
private void setEditMode()
{
if(table.getSelectedRow()<0 || table.getSelectedRow()>=dm.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select Designation To Edit");
return;
}
DesignationUI.this.setEditMode();
titleCaptionLabel.setVisible(false);
titleTextField.setVisible(true);
titleTextField.setText(this.designation.getTitle());
addButton.setEnabled(false);
cancelButton.setEnabled(true);
clearTitleTextFieldButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
editButton.setIcon(saveIcon);
}
private void setDeleteMode()
{
if(table.getSelectedRow()<0 || table.getSelectedRow()>=dm.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select Designation To Delete");
return;
}
DesignationUI.this.setDeleteMode();
titleCaptionLabel.setVisible(true);
titleTextField.setVisible(false);
addButton.setEnabled(false);
editButton.setEnabled(false);
cancelButton.setEnabled(true);
clearTitleTextFieldButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
removeDesignation();
setViewMode();
}
private void setExportToPDFMode()
{
DesignationUI.this.setExportToPDFMode();
addButton.setEnabled(false);
editButton.setEnabled(false);
cancelButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
//methods
private boolean addDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation Required");
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setTitle(title);
int index=0;
try
{
dm.add(d);
index=dm.indexOfDesignation(d);
//highlight The Designation
table.setRowSelectionInterval(index,index);
Rectangle rectangle=table.getCellRect(index,0,true);
table.scrollRectToVisible(rectangle);
return true;
}catch(BLException bl)
{
if(bl.hasGenericException()) JOptionPane.showMessageDialog(this,bl.getGenericException());
else if(bl.hasException("title"))JOptionPane.showMessageDialog(this,bl.getException("title"));
titleTextField.requestFocus();
return false;
}
}
private boolean updateDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation Required");
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setCode(this.designation.getCode());
d.setTitle(title);
int index=0;
try
{
dm.update(d);
index=dm.indexOfDesignation(d);
//highlight The Designation
table.setRowSelectionInterval(index,index);
Rectangle rectangle=table.getCellRect(index,0,true);
table.scrollRectToVisible(rectangle);
return true;
}catch(BLException bl)
{
if(bl.hasGenericException()) JOptionPane.showMessageDialog(this,bl.getGenericException());
else JOptionPane.showMessageDialog(this,bl.getException("title"));
titleTextField.requestFocus();
return false;
}
}
private void removeDesignation()
{
try
{
String title=this.designation.getTitle();
int choice=JOptionPane.showConfirmDialog(this,"Want To Delete Designation : "+title+" ?","Confirm",JOptionPane.YES_NO_OPTION);
if(choice==JOptionPane.YES_OPTION)
{
dm.remove(this.designation.getCode());
JOptionPane.showMessageDialog(this,"Designation = "+title+" Deleted.");
this.clearDesignation();
}
//else //Do Nothing
}catch(BLException bl)
{
if(bl.hasGenericException()) JOptionPane.showMessageDialog(this,bl.getGenericException());
else JOptionPane.showMessageDialog(this,bl.getException("title"));
titleTextField.requestFocus();
}
}
private void exportToPDFDesignation()
{
JFileChooser jfc=new JFileChooser();
jfc.setCurrentDirectory(new File("."));
int selectedOption=jfc.showSaveDialog(this);
if(selectedOption==jfc.APPROVE_OPTION)
{
try
{
String pdfFile=jfc.getSelectedFile().getAbsolutePath();
if(pdfFile.endsWith(".")) pdfFile+="pdf";
if(pdfFile.endsWith(".pdf")==false) pdfFile+=".pdf";
File file=new File(pdfFile);
File parent=new File(file.getParent());
if(parent.exists()==false || parent.isDirectory()==false)
{
JOptionPane.showMessageDialog(this,"Path Doesn't Exists Or Not A Directory path = "+parent.getAbsolutePath(),"Invalid Path",JOptionPane.ERROR_MESSAGE);
return;
}
if(file.exists())
{
int ans=JOptionPane.showConfirmDialog(this,"File Already Exists Do You Want To Delte Previous Exported Data File ?","Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
if(ans==JOptionPane.NO_OPTION)
{
JOptionPane.showMessageDialog(this,"File Not Deleted","Success",JOptionPane.PLAIN_MESSAGE);
return;
}
else file.delete();
}
try
{
dm.exportToPDF(file);
JOptionPane.showMessageDialog(this,file.getAbsolutePath(),"PDF Created",JOptionPane.PLAIN_MESSAGE);
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException(),"Error",JOptionPane.ERROR_MESSAGE);
}
}
}catch(Exception e)
{
//what to do
}
}
}
}
}