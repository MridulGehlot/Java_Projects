import java.io.*;
import java.net.*;
class City implements Serializable
{
public int code;
public String name;
}
class Student implements Serializable
{
public int rollNumber;
public String name;
public char gender;
public City city;
}
class Client3
{
public static void main(String gg[])
{
try
{
int rollNumber=Integer.parseInt(gg[0]);
String name=gg[1];
String gender=gg[2];
int cityCode=Integer.parseInt(gg[3]);
String cityName=gg[4];
City c=new City();
c.code=cityCode;
c.name=cityName;
Student s=new Student();
s.rollNumber=rollNumber;
s.name=name;
s.gender=gender.charAt(0);
s.city=c;
int i,j,k,chunkSize;
chunkSize=1024;
int requestLength,responseLength;
int bytesReadCount;

Socket socket=new Socket("localhost",5500);
InputStream is;
OutputStream os;
os=socket.getOutputStream();
is=socket.getInputStream();

ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(s);
oos.flush();
byte requestObject[]=baos.toByteArray();
requestLength=requestObject.length;
byte [] header=new byte[1024];
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
j=0;
while(j<requestLength)
{
if((requestLength-j)<chunkSize) chunkSize=requestLength-j;
os.write(requestObject,j,chunkSize);
os.flush();
j=j+chunkSize;
}
/*
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
*/
//sent all the Data Now Start Receiving Response
/*
FULL Flow
header-> ack<- data->
header<- ack-> response<- ack->
*/
//Header -> ack -> respose -> ack ->close
bytesReadCount=0;
byte [] tmp=new byte[1024];
i=0;
j=0;
while(j<chunkSize)
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
i=1023;
j=1;
responseLength=0;
while(i>=0)
{
responseLength=responseLength+(header[i]*j);
j=j*10;
i--;
}
ack[0]=1;
os.write(ack,0,1);
os.flush();
j=0;
i=0;
byte responseObject[]=new byte[responseLength];
while(j<responseLength)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
responseObject[i]=tmp[k];
i++;
}
j=j+responseLength;
}
ByteArrayInputStream bais=new ByteArrayInputStream(responseObject);
ObjectInputStream ois=new ObjectInputStream(bais);
String response=(String)ois.readObject();
System.out.println("Response Received : "+response);
ack[0]=1;
os.write(ack,0,1);
os.flush();
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}