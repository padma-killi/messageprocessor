package com.tcs.ain.mp;

import com.tcs.ain.mp.impl.MessageSenderImpl;
import com.tcs.ain.mp.common.FatalException;

/**
 * IMessageSender - A java interface for all the messages
 * 
 * @version 2.00 09 April 2009
 * @author Padma Killi
 * 
 * @see MessageSenderImpl
 * @see FatalException
 */
 
public interface IMessageSender 
{
  /**
   * The API processMessage() is a generic API that is used to push the JMS message to the
   * Queue/Topic.
   * @param message - The XML message document.
   * @throws FatalException
   */
  public void processMessage(String message) throws FatalException;
  public void processMessageNewMT(String message) throws FatalException;
}
