package com.tcs.ain.mp;

import com.tcs.ain.mp.common.MailingException;
import com.tcs.ain.mp.common.FatalException;

/* $Revision: 1.4 $
* Copyright � 2003  Global Net Services Inc
* All Rights Reserved
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/

/**
 * IMailingXMLServices - A common interface for all the mailing types
 * 
 * @version 1.00 25 th October
 * @author Christy Mathew
 * 
 * @see MailingException
 * @see FatalException
 */

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