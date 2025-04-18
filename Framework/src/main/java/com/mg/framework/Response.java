package com.mg.framework;
public class Response implements java.io.Serializable
{
private boolean success;
private Object result;
public void setSuccess(boolean success)
{
this.success=success;
}
public boolean getSuccess()
{
return this.success;
}
public void setResult(Object result)
{
this.result=result;
}
public Object getResult()
{
return this.result;
}
}