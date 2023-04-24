package com.tcs.ain.mp;

import com.tcs.ain.mp.common.MailingException;
import com.tcs.ain.mp.common.FatalException;

public interface IMailingXMLServices 
{
   /**
    * This is the common services API that each individual application related mailing
    * component has to implement.
    * @param message -  This is the XML message.
    * @throws MailingException
    * @throws FatalException
    */
   public void processMessage(String message) throws MailingException,FatalException;
}
