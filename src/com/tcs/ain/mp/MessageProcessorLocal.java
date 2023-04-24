package com.tcs.ain.mp;
import javax.ejb.EJBLocalObject;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.MailingException;

public interface MessageProcessorLocal extends EJBLocalObject 
{
  void processXMLMessage(String message, String initiator) throws FatalException, MailingException;
}