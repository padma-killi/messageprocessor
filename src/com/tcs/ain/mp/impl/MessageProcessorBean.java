package com.tcs.ain.mp.impl;

//mp imports
import com.tcs.ain.mp.IMailingXMLServices;
import com.tcs.ain.mp.factory.MessageProcessorFactory;

//EJB imports
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

//java.io and common imports
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.MailingException;

/* $Revision: 1.3 $
* Copyright � 2003  Global Net Services Inc
* All Rights Reserved
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/

/**
 * MessageProcessorBean - This MessageProcessorBean component is  the central component for 
 * different applications under FURLS umbrella to facilitate all the mailing related requirements.
 *  
 * @version 2.00 29 Aug 2003
 * @author Padma Killi
 * Modified on :12 April 2009
 * 
 * @see javax.ejb.SessionBean
 * @see IMailingXMLServices
 * @see MessageProcessorFactory
 * @see FatalException
 * @see MailingException
 */
public class MessageProcessorBean implements SessionBean 
{

  public void ejbCreate()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbRemove()
  {
  }

  public void setSessionContext(SessionContext ctx)
  {
  }

  /**
   * The API processXMLMessage() is used to process the XML String message.
   * This API delegates the call to the factory to decide on which object to invoke
   * based on the initiator of the message.
   * 
   * @param message - This is the XML message from FFRM_MP queue.
   * @throws FatalException
   * @throws MailingException
   */
  public void processXMLMessage(String message, String initiator) 
     throws FatalException, MailingException
  {
     IMailingXMLServices iMailingXMLServices = null;
     Object obj = MessageProcessorFactory.getMailingXMLServicesImpl(initiator);
     if (obj != null)
     {
           //cast the object with IMailingXMLServices.
           iMailingXMLServices = (IMailingXMLServices)obj;
           //Call the business method of the object.
           iMailingXMLServices.processMessage(message);
     }
     else
     {
         throw new FatalException("MessageProcessorBean.processMessage()..Unable to instantiate the object to process the XML message. The Mailing XML Services Object is null.The initiator of this message is :"+initiator);
     }
  }
}