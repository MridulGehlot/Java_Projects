package com.mg.hr.dl.interfaces.dao;
import java.util.*;
import com.mg.hr.dl.exceptions.*;
import com.mg.hr.dl.interfaces.dto.*;
public interface DesignationDAOInterface
{
public void add(DesignationDTOInterface designationDTO) throws DAOException;
public void update(DesignationDTOInterface designationDTO) throws DAOException;
public void delete(int code) throws DAOException;
public Set<DesignationDTOInterface> getAll() throws DAOException;
public DesignationDTOInterface getByCode(int code) throws DAOException;
public DesignationDTOInterface getByTitle(String title) throws DAOException;
public boolean codeExists(int code) throws DAOException;
public boolean tileExists(String title) throws DAOException;
public int getCount() throws DAOException;
}