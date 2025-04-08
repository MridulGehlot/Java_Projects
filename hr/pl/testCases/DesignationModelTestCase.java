import com.mg.hr.pl.model.*;
import javax.swing.*;
import java.awt.*;

class DesignationModelTestCase extends JFrame
{
private JTable table;
private DesignationModel dm;
private JScrollPane jsp;
private Container container;
DesignationModelTestCase()
{
dm=new DesignationModel();
table=new JTable(dm);
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
container.setLayout(new FlowLayout());
container.add(jsp);
setLocation(100,200);
setSize(400,300);
setVisible(true);
}
public static void main(String gg[])
{
DesignationModelTestCase a=new DesignationModelTestCase();
}
}