import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class RequestProcessor extends Thread
{
private Socket socket;
RequestProcessor(Socket socket)
{
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
System.out.println("Request Arrived File Name = "+fileName);
//Write To File
File f=new File(fileName);
if(f.exists()) f.delete();
FileOutputStream fout=new FileOutputStream(f);
j=0;
System.out.println("Total Length = "+requestLength);
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
System.out.println("closing connection");
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Server4 extends JFrame
{
private enum STATUS{ON,OFF};
private STATUS currentStatus;
private int portNumber;
private ServerSocket serverSocket;
private JLabel portLabel;
private JTextField portInput;
private JTextArea textArea;
private JButton controllButton;
private Container container;
Server4()
{
initComponents();
addListeners();
}

private void initComponents()
{
currentStatus=STATUS.OFF;
portLabel=new JLabel("Enter Port Number");
portInput=new JTextField(10);
textArea=new JTextArea();
controllButton=new JButton("Start");

portLabel.setBounds(10,10,100,50);
portInput.setBounds(10+100+10,10,100,50);
controllButton.setBounds(10+100+10+100+10,10,100,50);
textArea.setBounds(10,10+50+10,480,200);

container=getContentPane();
container.setLayout(null);
container.add(portLabel);
container.add(portInput);
container.add(textArea);
container.add(controllButton);
setLocation(150,100);
setSize(500,300);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}

private void addListeners()
{
this.controllButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(currentStatus==STATUS.OFF)
{
try
{
String input=portInput.getText();
if(input==null) throw new Exception("No Port Number Found");
input=input.trim();
if(input.length()==0) throw new Exception("Port Number Required");
portNumber=Integer.parseInt(input);
System.out.println("Port NUmber = "+portNumber);
currentStatus=STATUS.ON;
controllButton.setText("STOP");
startListening();
}catch(Exception e)
{
System.out.println(e);
}
}
else
{
currentStatus=STATUS.OFF;
}
}
});

}

private void addTo(String s)
{
this.textArea.append(s);
}

public void startListening()
{
try
{
System.out.println("In Start Listening");
serverSocket=new ServerSocket(portNumber);
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Server Is Listening On Port "+portNumber+"...");
addTo("Server Is Listening On Port "+portNumber+"...");
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket);
}
}catch(Exception e)
{
System.out.println(e);
}
}

public static void main(String gg[])
{
Server4 s=new Server4();
}
}