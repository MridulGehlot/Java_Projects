package com.mg.chess.server.dl;
import java.util.*;
public class MemberDAO
{
public List<MemberDTO> getAll()
{
List<MemberDTO> members=new LinkedList<>();
MemberDTO m=new MemberDTO();
m.username="mridul";
m.password="gehlot";
members.add(m);
m=new MemberDTO();
m.username="kavita";
m.password="gehlot";
members.add(m);
m=new MemberDTO();
m.username="nihal";
m.password="gehlot";
members.add(m);
m=new MemberDTO();
m.username="sanjay";
m.password="gehlot";
members.add(m);
m=new MemberDTO();
m.username="vishrut";
m.password="shastri";
members.add(m);
m=new MemberDTO();
m.username="rudransh";
m.password="solanki";
members.add(m);
m=new MemberDTO();
m.username="nishita";
m.password="solanki";
members.add(m);
m=new MemberDTO();
m.username="mayank";
m.password="lovevanshi";
members.add(m);
m=new MemberDTO();
m.username="shaurya";
m.password="vishwakarma";
members.add(m);
m=new MemberDTO();
m.username="pranjal";
m.password="pal";
members.add(m);
m=new MemberDTO();
m.username="paridhi";
m.password="gupta";
members.add(m);
return members;
}
}