import java.io.*;
import java.net.*;
class Client4
{
public static void main(String gg[])
{
try
{
String fileName=gg[0];
File f=new File(fileName);
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

Socket socket=new Socket("localhost",5500);
InputStream is;
OutputStream os;
os=socket.getOutputStream();
is=socket.getInputStream();

char arr[]=fileName.toCharArray();
requestLength=f.length();
System.out.println("Request Length = "+(int)requestLength);
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
}catch(Exception e)
{
System.out.println(e);
}
}
}