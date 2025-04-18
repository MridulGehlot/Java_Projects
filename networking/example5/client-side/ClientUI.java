import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
class Client extends Thread
{
private String server;
private int portNumber;
private File f;
private ClientUI clientUI;
private JLabel fileNameLabel;
private JProgressBar pb;
private JPanel panel;
Client(String server,int portNumber,File f,ClientUI clientUI)
{
this.server=server;
this.portNumber=portNumber;
this.f=f;
this.clientUI=clientUI;
start();
}
public void run()
{
try
{
clientUI.finalStatus.setText("Connected");
clientUI.finalStatus.setForeground(Color.green);
if(f.exists()==false)
{
System.out.println("File Not Found");
return;
}
int i,j,chunkSize;
long k;
chunkSize=4096;
long requestLength;
int bytesReadCount;

Socket socket=new Socket(server,portNumber);
InputStream is;
OutputStream os;
os=socket.getOutputStream();
is=socket.getInputStream();

String fileName=f.getName();
char arr[]=fileName.toCharArray();
requestLength=f.length();
System.out.println("Request Length = "+(int)requestLength);

fileNameLabel=new JLabel("File Name : "+fileName);
pb=new JProgressBar(0,(int)requestLength);
panel=new JPanel();
panel.setLayout(new BorderLayout());
panel.add(fileNameLabel,BorderLayout.CENTER);
panel.add(pb,BorderLayout.SOUTH);
clientUI.updateLog(panel);

byte [] header=new byte[1024];
for(i=0;i<fileName.length();i++)
{
header[i]=(byte)arr[i];
}
i=1023;
k=requestLength;
while(k>0)
{
header[i]=(byte)(k%10);
i--;
k=k/10;
}
os.write(header,0,1024);
os.flush();
byte ack[]=new byte[1];
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
FileInputStream fin=new FileInputStream(f);
byte tmp[]=new byte[chunkSize];
j=0;
while(j<requestLength)
{
if((requestLength-j)<chunkSize) chunkSize=(int)(requestLength-j);
for(i=0;i<chunkSize;i++)
{
tmp[i]=(byte)((char)fin.read());
}
os.write(tmp,0,chunkSize);
os.flush();
j=j+chunkSize;
pb.setValue(j);
//System.out.println("j = "+j);
}
fin.close();
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
//sent File To Server
/*
FULL Flow
header-> ack<- file-> ack<-
*/
System.out.println("File Uploaded");
socket.close();
clientUI.finalStatus.setText("Not Connected");
clientUI.finalStatus.setForeground(Color.red);
}catch(Exception e)
{
System.out.println(e);
clientUI.finalStatus.setText("Not Connected");
clientUI.finalStatus.setForeground(Color.red);
}
}
}
class ClientUI extends JFrame
{
private JLabel filesSelectedLabel;
private JLabel serverLabel;
private JTextField serverTextField;
private JLabel portNumberLabel;
private JTextField portNumberTextField;
private JButton fileChooseButton;
private JTable filesTable;
private JLabel statusLabel;
public JLabel finalStatus;
private JLabel filesCount;
private JButton startButton;
private JPanel leftPanel,rightPanel;
private JLabel progressLabel;
private JLabel fileName;
private JProgressBar progressBar;
private Container container;
private int lastRecord;
private DefaultTableModel tableModel;
private java.util.List<File> allFiles;
private String server;
private String portNumberString;
private int portNumber;
private JPanel rowsPanel;
ClientUI()
{
allFiles=new ArrayList<File>();
lastRecord=1;
initComponents();
addListeners();
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
private void initComponents()
{
filesSelectedLabel=new JLabel("Files Selected");
serverLabel=new JLabel("Server");
serverTextField=new JTextField();
portNumberLabel=new JLabel("Port Number");
portNumberTextField=new JTextField();
fileChooseButton=new JButton("Choose Files");
filesCount=new JLabel("Files Count : "+allFiles.size());
statusLabel=new JLabel("Status : ");
finalStatus=new JLabel("Not Connected");
startButton=new JButton("Start Uploading");
leftPanel=new JPanel();
rightPanel=new JPanel();
progressLabel=new JLabel("Progress");

String[] columnNames = {"S.no.","Name"};
tableModel = new DefaultTableModel(columnNames, 0);
filesTable = new JTable(tableModel);
//filesTable.isCellEditable(false);
JScrollPane tableScrollPane = new JScrollPane(filesTable);

filesSelectedLabel.setBounds(150,10,100,20);
serverLabel.setBounds(10,40,60,20);
serverTextField.setBounds(10+40+10,40,80,20);
portNumberLabel.setBounds(10+60+10+80+10,40,100,20);
portNumberTextField.setBounds(10+60+10+50+10+100+10,40,60,20);
fileChooseButton.setBounds(10,80,150,40);
filesCount.setBounds(250,90,100,20);
//filesTable.setBounds(10,80+50,360,350);
tableScrollPane.setBounds(10,80+50,360,350);
statusLabel.setBounds(10,80+50+350+10,50,50);
finalStatus.setBounds(10+50+10,80+50+350+10,100,50);
startButton.setBounds(10+50+150,80+50+350+20,150,30);

leftPanel.setLayout(null);
leftPanel.add(filesSelectedLabel);
leftPanel.add(serverLabel);
leftPanel.add(serverTextField);
leftPanel.add(portNumberLabel);
leftPanel.add(portNumberTextField);
leftPanel.add(fileChooseButton);
leftPanel.add(filesCount);
//leftPanel.add(filesTable);
leftPanel.add(tableScrollPane);
leftPanel.add(statusLabel);
leftPanel.add(startButton);
leftPanel.add(finalStatus);
rowsPanel=new JPanel(new GridLayout(0,1,1,1));
rightPanel.setLayout(new BorderLayout());
progressLabel.setHorizontalAlignment(SwingConstants.CENTER); // Ensure it's centered
rightPanel.add(progressLabel, BorderLayout.NORTH);
rightPanel.add(rowsPanel, BorderLayout.CENTER);

leftPanel.setBounds(10,10,380,540);
leftPanel.setBackground(Color.gray);
rightPanel.setBounds(10+380+10,10,380,540);
rightPanel.setBackground(Color.green);

container=getContentPane();
container.setLayout(null);
container.add(leftPanel);
container.add(rightPanel);

Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int x,y,width,height;
width=800;
height=600;
x=d.width/2-width/2;
y=d.height/2-height/2;
setLocation(x,y);
setSize(width,height);
setVisible(true);
}
private void addListeners()
{
fileChooseButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
JFileChooser jfc=new JFileChooser();
jfc.setCurrentDirectory(new File("."));
int selectedOption=jfc.showSaveDialog(ClientUI.this);
if(selectedOption==jfc.APPROVE_OPTION)
{
String fileSelected=jfc.getSelectedFile().getAbsolutePath();
File f=new File(fileSelected);
if(f.exists()==false)
{
System.out.println("No File Exists");
return;
}
if(f.isDirectory())
{
JOptionPane.showMessageDialog(ClientUI.this,"Cannot Upload Directory");
return;
}
allFiles.add(f);
filesCount.setText("Files Count : "+allFiles.size());
tableModel.addRow(new Object[]{lastRecord++,f.getName()});
}
}
});
startButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
try
{
server=serverTextField.getText().trim();
portNumberString=portNumberTextField.getText().trim();
portNumber=Integer.parseInt(portNumberString);
if(allFiles.size()>0)
{
Client c;
for(File f:allFiles)
{
c=new Client(server,portNumber,f,ClientUI.this);
}
}
else
{
JOptionPane.showMessageDialog(ClientUI.this,"No Files Selected");
}
}catch(Exception e)
{
System.out.println(e);
}
}
});
}

public void updateLog(JPanel panel)
{
this.rowsPanel.add(panel);
this.rightPanel.revalidate();
this.rightPanel.repaint();
}

public static void main(String gg[])
{
ClientUI client=new ClientUI();
}
}