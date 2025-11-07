package com.mg.chess.server;
import com.mg.chess.server.dl.*;
import com.mg.chess.common.*;
import com.mg.nframework.server.*;
import com.mg.nframework.server.annotations.*;
import java.util.*;
@Path("/MGChessServer")
public class MGChessServer
{
public static Map<String,Member> members;
public static Set<String> loggedInMembers;
public static Set<String> playingMembers;
public static Map<String,List<Message>> inboxes;
public static Map<String,Game> games;
static
{
populateDataStructure();
}
public MGChessServer()
{
}
private static void populateDataStructure()
{
members=new HashMap<>();
List<MemberDTO> allMembers=new MemberDAO().getAll();
Member member;
for(MemberDTO m:allMembers)
{
member=new Member();
member.username=m.username;
member.password=m.password;
members.put(member.username,member);
}
loggedInMembers=new HashSet<>();
playingMembers=new HashSet<>();
inboxes=new HashMap<>();
games=new HashMap<>();
}
//Create Services to enable clients to perform actions
//login
@Path("/login")
public boolean login(String username,String password)
{
boolean success=false;
Member m=members.get(username);
if(m==null) return success;
success=password.equals(m.password);
if(success) loggedInMembers.add(m.username);
return success;
}
//logout
@Path("/logout")
public void logout(String username)
{
loggedInMembers.remove(username);
//playingMembers.remove(username); //Will do it later
}
//get Available Players
@Path("/getAvailableMembers")
public List<String> getAvailableMembers(String username)
{
List<String> availableMembers=new LinkedList<>();
for(String u:loggedInMembers)
{
if(playingMembers.contains(u)==false && username.equals(u)==false) availableMembers.add(u);
}
return availableMembers;
}
//invite
@Path("/invite")
public void invite(String fromUsername,String toUsername)
{
List<Message> m=inboxes.get(toUsername);
if(m==null)
{
m=new LinkedList<>();
inboxes.put(toUsername,m);
}
Message message=new Message();
message.fromUsername=fromUsername;
message.toUsername=toUsername;
message.type=MESSAGE_TYPE.CHALLENGE;
m.add(message);
}
//getMessages
@Path("/getMessages")
public List<Message> getMessages(String username)
{
List<Message> messages=inboxes.get(username);
if(messages!=null && messages.size()>0)
{
inboxes.put(username,new LinkedList<Message>());
}
return messages;
}
}