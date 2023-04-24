package com.tcs.ain.mp.alert.impl;

import com.tcs.ain.mp.alert.IApplicationErrorAlertServices;
import com.tcs.ain.mp.manager.ResourceManager;
import com.tcs.ain.mp.manager.MailManager;

//Java Mail imports
import javax.mail.SendFailedException;
import javax.mail.MessagingException;

/* $Revision: 1.1.1.1 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * ApplicationErrorAlertServicesImpl -  A  java class that implements the IApplicationErrorAlertServices interface.
 * This class sends the application related errors via email.
 * 
 * @version 2.00 8 April 2009
 * @author Padma Killi
 * 
 * @see IApplicationErrorAlertServices
 * @see MailManager
 * @see ResourceManager
*/

public class ApplicationErrorAlertServicesImpl implements IApplicationErrorAlertServices, java.io.Serializable
{
  public ApplicationErrorAlertServicesImpl(){}

  /**
   * The API sendErrorAlert() sends the application related errors.
   * @param message - The error message.
   * @param isFatal - Determines whether the message is fatal or not.
   */
  public void sendErrorAlert(String message,boolean isFatal)
  {
     if ("ON".equals(ResourceManager.getInstance().getAppErrorAlertSwitch()))
     {
          try
          {
              if (isFatal)
                MailManager.sendHTMLMail(ResourceManager.getInstance().getAppErrorAlertTo(),ResourceManager.getInstance().getFromAddress(),ResourceManager.getInstance().getAppErrorAlertSubject()+" (Fatal)",message);
              else
                MailManager.sendHTMLMail(ResourceManager.getInstance().getAppErrorAlertTo(),ResourceManager.getInstance().getFromAddress(),ResourceManager.getInstance().getAppErrorAlertSubject()+" (Error)",message);
          }
          catch (SendFailedException sfe)
          {
            sfe.printStackTrace();
            System.out.println("ApplicationErrorAlertServicesImpl.sendErrorAlert()..Unable to send the application alert email");
          }
          catch (MessagingException me)
          {
            me.printStackTrace();
            System.out.println("ApplicationErrorAlertServicesImpl.sendErrorAlert()..Messaging Exception occurred :"+me.getMessage());          
          }
          catch (Exception e)
          {
             e.printStackTrace();
             System.out.println("ApplicationErrorAlertServicesImpl.sendErrorAlert()..A generic exception occurred :"+e.getMessage());
          }
     }
  }
}