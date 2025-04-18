package com.mg.framework;
public class Request implements java.io.Serializable
{
private String path;
private Object[] arguments;
public void setPath(String path)
{
this.path=path;
}
public String getPath()
{
return this.path;
}
public void setArguments(Object ...arguments)
{
this.arguments=arguments;
}
public Object[] getArguments()
{
return this.arguments;
}
}