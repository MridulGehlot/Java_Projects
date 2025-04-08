package com.mg.hr.pl.model;
import com.mg.hr.bl.exceptions.*;
import com.mg.hr.bl.interfaces.pojo.*;
import com.mg.hr.bl.interfaces.managers.*;
import com.mg.hr.bl.pojo.*;
import com.mg.hr.bl.managers.*;
import javax.swing.table.*;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
public class DesignationModel extends AbstractTableModel
{
private java.util.List<DesignationInterface> designations;
private String title[];
private DesignationManagerInterface designationManager;
public DesignationModel()
{
populateDataStructures();
}
public int getRowCount()
{
return designations.size();
}
public int getColumnCount()
{
return title.length;
}
public String getColumnName(int columnIndex)
{
return this.title[columnIndex];
}
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0) return Integer.class;
else return String.class;
}
public Object getValueAt(int r,int c)
{
if(c==0) return r+1;
else return this.designations.get(r).getTitle();
}
public boolean isCellEditable(int r,int c)
{
return false;
}
private void populateDataStructures()
{
title=new String[2];
title[0]="S.No.";
title[1]="Designations";
try
{
Set<DesignationInterface> blDesignations;
designationManager=DesignationManager.getDesignationManager();
blDesignations=designationManager.getDesignations();
this.designations=new LinkedList<>();
for(DesignationInterface designation:blDesignations)
{
this.designations.add(designation);
}
}catch(BLException blException)
{
//???? What to do
}
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}
//Application Specific Methods
public void add(DesignationInterface designation) throws BLException
{
designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public void update(DesignationInterface designation) throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public void remove(int code) throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> it=this.designations.iterator();
int index=0;
while(it.hasNext())
{
if(it.next().getCode()==code) break;
index++;
}
if(index==this.designations.size())
{
BLException bl=new BLException();
bl.setGenericException("Invalid Code = "+code);
throw bl;
}
this.designations.remove(index);
fireTableDataChanged();
}
public int indexOfDesignation(DesignationInterface designation) throws BLException
{
Iterator<DesignationInterface> it=this.designations.iterator();
int index=0;
DesignationInterface d;
while(it.hasNext())
{
d=it.next();
if(d.equals(designation)) return index;
index++;
}
BLException bl=new BLException();
bl.setGenericException("Invalid Designation = "+designation.getTitle());
throw bl;
}
public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
{
Iterator<DesignationInterface> it=this.designations.iterator();
int index=0;
DesignationInterface d;
while(it.hasNext())
{
d=it.next();
if(partialLeftSearch) if(d.getTitle().toUpperCase().startsWith(title.toUpperCase())) return index;
else if(d.getTitle().equalsIgnoreCase(title)) return index;
index++;
}
BLException bl=new BLException();
bl.setGenericException("Invalid title = "+title);
throw bl;
}
public DesignationInterface getDesignationAt(int index) throws BLException
{
if(index<0 || index>=this.designations.size())
{
BLException ble=new BLException();
ble.setGenericException("Invalid index");
throw ble;
}
return this.designations.get(index);
}
//Pdf 
public void exportToPDF(File file) throws BLException
{
String fileName=file.getAbsolutePath();
Document document=new Document();
try
{
PdfWriter.getInstance(document,new FileOutputStream(fileName));
document.open();
PdfPTable table=null;
int pageNumber=1;
int pageSize=20;
int sno=1;
boolean newPage=true;
int size=designations.size();
Font titleFont=new Font(Font.FontFamily.HELVETICA,16,Font.BOLD,BaseColor.WHITE);
Font dataFont=new Font(Font.FontFamily.HELVETICA,14,Font.NORMAL);
float [] columnWidth={1,4};
for(int i=0;i<size;i++)
{
if(newPage)
{
//create Header
newPage=false;
Image img=Image.getInstance(DesignationModel.class.getResource("/images/my.png"));
img.scaleToFit(50,50);
Paragraph logo=new Paragraph("MG Companies",new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD));
logo.setAlignment(Element.ALIGN_CENTER);

PdfPTable headerTable=new PdfPTable(2);
float [] colWidths={1,4};
headerTable.setWidths(colWidths);
headerTable.setWidthPercentage(100);
PdfPCell logoCell=new PdfPCell(img);
logoCell.setBorder(Rectangle.NO_BORDER);
PdfPCell titleCell=new PdfPCell(logo);
titleCell.setBorder(Rectangle.NO_BORDER);
logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
titleCell.setVerticalAlignment(Element.ALIGN_CENTER);
headerTable.addCell(logoCell);
headerTable.addCell(titleCell);

document.add(headerTable);
document.add(new Paragraph("\n"));
table=new PdfPTable(2);
table.setWidths(columnWidth);
table.setWidthPercentage(100);
PdfPCell cell1=new PdfPCell(new Phrase("S.No.",titleFont));
PdfPCell cell2=new PdfPCell(new Phrase("Designation",titleFont));
cell1.setBackgroundColor(BaseColor.DARK_GRAY);
cell2.setBackgroundColor(BaseColor.DARK_GRAY);
cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
table.addCell(cell1);
table.addCell(cell2);
}
//add Data
PdfPCell id=new PdfPCell(new Phrase(String.valueOf(sno),dataFont));
PdfPCell name=new PdfPCell(new Phrase(designations.get(i).getTitle(),dataFont));
id.setHorizontalAlignment(Element.ALIGN_RIGHT);
name.setHorizontalAlignment(Element.ALIGN_LEFT);
table.addCell(id);
table.addCell(name);
if(sno%pageSize==0 || sno==size)
{
//create Footer
newPage=true;
document.add(table);
document.add(new Paragraph("\n"));
document.add(new Paragraph("Software By : Mridul Gehlot(CEO @ MGCompanies)",new Font(Font.FontFamily.COURIER,12,Font.ITALIC,BaseColor.RED)));
if(sno<size) document.newPage();
}
sno++;
}
document.close();
System.out.println("New PDF Created = "+fileName);
}catch(Exception e)
{
BLException ble=new BLException();
ble.setGenericException(e.getMessage());
throw ble;
}
}
}