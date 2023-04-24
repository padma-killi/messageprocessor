package com.tcs.ain.mp;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;

public interface MessageProcessorLocalHome extends EJBLocalHome 
{
  MessageProcessorLocal create() throws CreateException;
}