package com.mg.chess.server;
import com.mg.nframework.server.*;
import com.mg.nframework.common.*;
import com.mg.nframework.server.annotations.*;
public class Main
{
public static void main(String gg[])
{
NFrameworkServer nfs=new NFrameworkServer();
nfs.registerClass(MGChessServer.class);
nfs.start();
}
}
