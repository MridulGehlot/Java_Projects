import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class RequestProcessor extends Thread
{
private Socket socket;
private ServerUI serverUI;
RequestProcessor(Socket socket,ServerUI serverUI)
{
this.serverUI=serverUI;
this.socket=socket;
start();
}
public void run()
{
try
{
int i,j,k,chunkSize;
chunkSize=4096;
int requestLength;
int bytesReadCount;
byte ack[]=new byte[1];
String fileName;
OutputStream os;
InputStream is;

os=socket.getOutputStream();
is=socket.getInputStream();

bytesReadCount=0;
byte [] tmp=new byte[1024];
byte [] header=new byte[1024];
i=0;
j=0;
while(j<1024)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
//Process Header Seperate Name and Size
k=0;
fileName="";
while(k<1024)
{
if(header[k]==0) break;
fileName+=(char)header[k];
k++;
}

//To Shut Down Server
if(fileName.equalsIgnoreCase("MGShutDown"))
{
this.serverUI.add("Shutting Down The Server");
socket.close();
return;
}

i=1023;
j=1;
requestLength=0;
while(i>=k)
{
requestLength=requestLength+(header[i]*j);
j=j*10;
i--;
}
ack[0]=1;
os.write(ack,0,1);
os.flush();
this.serverUI.add("Request Arrived File Name = "+fileName);
//Write To File
File f=new File("uploads"+File.separator+fileName);
if(f.exists()) f.delete();
FileOutputStream fout=new FileOutputStream(f);
j=0;
this.serverUI.add("Total Length = "+requestLength);
bytesReadCount=0;
tmp=new byte[chunkSize];
while(j<requestLength)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
//fout.write((char)tmp[k]);
fout.write(tmp, 0, bytesReadCount); // ✅ Correct
fout.flush();
j=j+bytesReadCount;
}
fout.close();
/*
FULL Flow 
header<- ack-> file<- ack->
*/
ack[0]=1;
os.write(ack,0,1);
os.flush();
this.serverUI.add("File Saved To : "+f.getAbsolutePath());
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Server
{
private boolean flag;
private ServerSocket serverSocket;
private int portNumber;
private ServerUI serverUI;
Server(int portNumber,ServerUI serverUI)
{
try
{
this.serverUI=serverUI;
this.portNumber=portNumber;
this.serverSocket=new ServerSocket(this.portNumber);
this.flag=false;
//startListening();
new Thread(() -> startListening()).start();
}catch(Exception e)
{
System.out.println(e);
}
}
private void startListening()
{
try
{
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
if(flag) break;
this.serverUI.add("Server is Listening on Port Number : "+this.portNumber);
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket,this.serverUI);
}
this.serverUI.add("Out Of Loop");
}catch(Exception e)
{
System.out.println(e);
}
}
public void stop()
{
try
{
this.flag=true;
Socket socket;
socket=new Socket("localhost",this.portNumber);
byte header[]=new byte[1024];
String target="MGShutDown";
char [] ch=target.toCharArray();
for(int i=0;i<ch.length;i++)
{
header[i]=(byte)ch[i];
}
OutputStream os;
os=socket.getOutputStream();
os.write(header);
os.flush();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class ServerUI extends JFrame
{
private JLabel portNumberLabel;
private JTextArea textArea;
private JButton startStopButton;
private JTextField portNumberTextField;
private Container container;
private int portNumber;
enum STATUS{ON,OFF};
private STATUS currentStatus;
private Server server;
ServerUI()
{
initComponents();
addListeners();
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
private void initComponents()
{
this.currentStatus=STATUS.OFF;
portNumberLabel=new JLabel("Enter Port Number");
textArea=new JTextArea();
startStopButton=new JButton("Start");
portNumberTextField=new JTextField();
int lm=75;
int tm=40;
portNumberLabel.setBounds(lm+40,tm,150,40);
portNumberTextField.setBounds(lm+30+150+20,tm,80,40);
startStopButton.setBounds(lm+30+150+20+80+20,tm,80,40);
textArea.setBounds(lm,tm+40+15,600,400);

container=getContentPane();
container.setLayout(null);
container.add(portNumberLabel);
container.add(portNumberTextField);
container.add(startStopButton);
container.add(textArea);

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
this.startStopButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(currentStatus==STATUS.OFF)
{
try
{
portNumber=Integer.parseInt(portNumberTextField.getText().trim());
if(portNumber<=2000 || portNumber>=10000) throw new Exception("Choose Between 2000-10,000");
startServer(portNumber);
currentStatus=STATUS.ON;
portNumberTextField.setEnabled(false);
startStopButton.setText("STOP");
}catch(Exception e)
{
System.out.println(e);
}
}
else
{
currentStatus=STATUS.OFF;
portNumberTextField.setEnabled(true);
startStopButton.setText("START");
stopServer();
}
}
});
}
private void startServer(int portNumber)
{
this.server=new Server(portNumber,this);
}
private void stopServer()
{
this.server.stop();
}
public void add(String s)
{
SwingUtilities.invokeLater(new Runnable(){
public void run()
{
textArea.append(s+"\n");
}
});
}
public static void main(String gg[])
{
ServerUI a=new ServerUI();
}
}