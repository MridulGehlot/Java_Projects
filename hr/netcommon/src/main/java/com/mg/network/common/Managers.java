package com.mg.network.common;


public class Managers
{
public static final ManagerType DESIGNATION=ManagerType.DESIGNATION;
public static final ManagerType EMPLOYEE=ManagerType.EMPLOYEE;

public enum ManagerType{DESIGNATION,EMPLOYEE};
public enum Designation{ADD,UPDATE,REMOVE,GET_BY_CODE,GET_BY_TITLE,CODE_EXISTS,TITLE_EXISTS,GET_COUNT,GET_ALL};

public static String getManagerType(ManagerType m)
{
if(m==ManagerType.DESIGNATION)
{
return "DesignationManager";
}
else if(m==ManagerType.EMPLOYEE)
{
return "EmployeeManager";
}
else return "";
}
public static String getAction(Designation e)
{
if(e==Designation.ADD) return "addDesignation";
else if(e==Designation.UPDATE) return "updateDesignation";
else if(e==Designation.REMOVE) return "removeDesignation";
else if(e==Designation.GET_BY_CODE) return "getDesignationByCode";
else if(e==Designation.GET_BY_TITLE) return "getDesignationByTitle";
else if(e==Designation.CODE_EXISTS) return "designationCodeExists";
else if(e==Designation.TITLE_EXISTS) return "designationTitleExists";
else if(e==Designation.GET_COUNT) return "getDesignationCount";
else if(e==Designation.GET_ALL) return "getDesignations";
else return "";
}
}