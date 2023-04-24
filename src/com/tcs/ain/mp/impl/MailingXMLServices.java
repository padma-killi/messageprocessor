package com.tcs.ain.mp.impl;

import com.tcs.ain.mp.factory.impl.ApplicationObjectFactoryImpl;
import com.tcs.ain.mp.factory.MessageProcessorFactory;
import com.tcs.ain.mp.alert.IApplicationErrorAlertServices;
import com.tcs.ain.mp.helper.MailLogVO;
import com.tcs.ain.mp.manager.MailManager;
import com.tcs.ain.mp.manager.ITransformXML;
import com.tcs.ain.mp.IMessageSender;
import com.tcs.ain.mp.logging.IMailLog;
import com.tcs.ain.mp.dao.ISendMailingDao;

import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.XMLTransformException;
import com.tcs.ain.mp.common.ApplicationErrorAlertException;
import gov.fda.furls.mp.helper.*;
//Java Mail imports
import javax.mail.SendFailedException;
import javax.mail.MessagingException;

/* $Revision: 1.5 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * MailingXMLServices -  A  java super class that encapsulates the common API's to support
 * multiple application level implementation of mailing types.Individual Application level
 * implementation of mailing types must inherit this class.
 * 
 * @version 2.00 14 April 2009
 * @author Padma Killi
 * 
 * @see ITransformXML
 * @see IMessageSender
 * @see ApplicationObjectFactoryImpl
 * @see IApplicationErrorAlertServices
 * @see FatalException
 * @see XMLTransformException
 * @see ApplicationErrorAlertException
*/

public class MailingXMLServices implements java.io.Serializable
{
  private IMessageSender iMessageSender = null;
  private ITransformXML iTransformXML = null;
  private IApplicationErrorAlertServices iApplicationErrorAlertServices = null;
  private IMailLog iMailLog = null;
  private ISendMailingDao iSendMailingDao = null;

  public MailingXMLServices(){}

  /**
   * The API sendEmail is used to send message in text format via email to the customer.
   * provided there is an email address in the message.
   * @param to - To address
   * @param from - From Address
   * @param subject - Subject of the Email
   * @param message - Contents of the email
   * @throws Exception
   * @throws SendFailedException
   * @throws MessagingException
   */
   public void sendEmail(String to, String from, String subject, String message)
      throws Exception,SendFailedException,MessagingException
   {
         MailManager.sendMail(to,from,subject,message);
   }
   
  /**
   * The API sendTextEmail is used to send message in text format via email to the customer.
   * provided there is an email address in the message.
   * @param to - To address
   * @param from - From Address
   * @param subject - Subject of the Email
   * @param message - Contents of the email
   * @throws Exception
   * @throws SendFailedException
   * @throws MessagingException
   */
   public void sendTextEmail(String to, String from, String subject, String message)
      throws Exception,SendFailedException,MessagingException
   {
         MailManager.sendMail(to,from,subject,message);
   }
    /**
   * The API sendHTMLMail is used to send message in html format via email to the customer.
   * provided there is an email address in the message.
   * @param to - To address
   * @param from - From Address
   * @param subject - Subject of the Email
   * @param htmlText - Html Contents of the email
   * @throws Exception
   * @throws SendFailedException
   * @throws MessagingException
   */
   public void sendHTMLMail(String to, String from, String subject, String htmlText)
        throws Exception,SendFailedException,MessagingException
   {
         MailManager.sendHTMLMail(to,from,subject,htmlText);
   }
   /**
   * The API sendEmailWithAttachment is used to send message via email with attachment to the customer.
   * provided there is an email address in the message.
   * @param to - To address
   * @param from - From Address
   * @param subject - Subject of the Email
   * @param message - Contents of the email
   * @param fileToAttach - The filename that goes as an attachment in the email.
   * @throws Exception
   * @throws SendFailedException
   * @throws MessagingException
   */
   public void sendMailWithAttachment(String to, String from, String subject, String message,String fileToAttach)
      throws Exception,SendFailedException,MessagingException
   {
         MailManager.sendMailWithAttachment(to,from,subject,message,fileToAttach);
   }
  /**
   * The API notifyPaperProcess is used to send message to PPF queue for further processing such
   * as sending the message via paper and log the message for records management.
   * @param message  - XML message.
   * @throws FatalException
   */
  public void notifyPaperProcess(String message)
      throws FatalException
   {
          getMessageSender().processMessage(message);
   }

   /**
    * The API getMessageSender() is to initialize the IMessageSender Object if it is
    * not initialized.
    * @return IMessageSender - The Message Sender Object.
    * @throws FatalException
    */
   public IMessageSender getMessageSender()
        throws FatalException
   {
      return (iMessageSender == null ? MessageProcessorFactory.getApplicationObjectFactory().getNewMessageSenderObject("gov.fda.furls.mp.impl.MessageSenderImpl") : iMessageSender);
   }
   //ADDED ON NOVEMBER 3rd 2009 for sending the MT-9 AND MT-12
   /**
   * The API notifyPaperProcess is used to send message to PPF queue for further processing such
   * as sending the message via paper and log the message for records management.
   * @param message  - XML message.
   * @throws FatalException
   */
  public void notifyPaperProcessNewMT(String message)
      throws FatalException
   {
          getMessageSenderNew().processMessageNewMT(message);
   }

   /**
    * The API getMessageSender() is to initialize the IMessageSender Object if it is
    * not initialized.
    * @return IMessageSender - The Message Sender Object.
    * @throws FatalException
    */
   public IMessageSender getMessageSenderNew()
        throws FatalException
   {
      return (iMessageSender == null ? MessageProcessorFactory.getApplicationObjectFactory().getNewMessageSenderObject("gov.fda.furls.mp.impl.MessageSenderImpl") : iMessageSender);
   }
    /**
    * 
    * The API logXMLFile is used to insert the xml file into the Furls Database,
    * for future references and for debugging purposes.Reg No, mailing type id,
    * mailing type name,created date and xml file is inserted into the database.
    */
   public  void logXMLFile(MailLogVO maillogvo)
   {
     try 
     {
       System.out.println("COMING HERE TO GET THE MAILING LOG INSERT CLASS ");
      // getXMLLoggerC().insertIntoMailLogger(maillogvo);
      getXMLLogger().insertMailingLog(maillogvo);
     }catch(Exception e) 
     {
       e.printStackTrace();
       System.out.println("DIDNT SEND MAILLOGVO TO THE LOGGING SERVICES ");
     }
   }
   
   /* 
    * 
    * 
    * */
   public  void updateRegistrationMailing(MailLogVO maillogvo) 
   {
     try 
     {
       System.out.println("COMING HERE TO UPDATE THE REG MAILING TABLE ");
      // getXMLLoggerC().insertIntoMailLogger(maillogvo);
      getXMLLogger().updateRegMailing(maillogvo);
     }catch(Exception e) 
     {
       e.printStackTrace();
       System.out.println("DIDNT SEND MAILLOGVO TO THE LOGGING SERVICES ");
     }
   }
    public ISendMailingDao getXMLLogger() throws FatalException
    {
         System.out.println("COMING HERE TO GET MAILING DAO FROM FACTORY ");
        //return (iMailLog == null ? MessageProcessorFactory.getApplicationObjectFactory().getXMLSenderObject("gov.fda.furls.mp.impl.MailLoggingServices") : iMailLog);
         return (iSendMailingDao == null ? MessageProcessorFactory.getApplicationObjectFactory().getXMLMessageSenderObject("gov.fda.furls.mp.dao.impl.SendMailingDaoImpl") : iSendMailingDao);
    }

   public IMailLog getXMLLoggerC() throws FatalException
    {
         System.out.println("COMING HERE TO GET MAILING DAO FROM FACTORY FOR CONNECTION BASED");
        return (iMailLog == null ? MessageProcessorFactory.getApplicationObjectFactory().getXMLSenderObject("gov.fda.furls.mp.logging.impl.MailLoggingServices") : iMailLog);
        // return (iSendMailingDao == null ? MessageProcessorFactory.getApplicationObjectFactory().getXMLMessageSenderObject("gov.fda.furls.mp.dao.impl.SendMailingDaoImpl") : iSendMailingDao);
    }



 
   /**
    * The API getTransformXMLManager() is to initialize the ITransformXML Object if it is
    * not initialized.
    * @return ITransformXML - The XML Transformation Manager Object.
    * @throws XMLTransformException
    */
   public ITransformXML getTransformXMLManager() 
       throws XMLTransformException
   {
      try {
        iTransformXML = (iTransformXML == null ? MessageProcessorFactory.getApplicationObjectFactory().getNewTransformXMLObject("gov.fda.furls.mp.manager.TransformXMLManager") : iTransformXML);
      }
      catch (FatalException fe){
         throw new XMLTransformException("MailingXMLServices.getTransformXMLManager() "+fe.getMessage());
      }
      return (iTransformXML);
   }

   /**
    * The API getAppAlertServicesObject() is to initialize the IApplicationErrorAlertServices Object if it is
    * not initialized.
    * @return IApplicationErrorAlertServices - The Application Error Alert Object.
    * @throws ApplicationErrorAlertException
    */
   public IApplicationErrorAlertServices getAppAlertServicesObject() 
        throws ApplicationErrorAlertException
   {
       try{
         iApplicationErrorAlertServices = (iApplicationErrorAlertServices == null ? MessageProcessorFactory.getApplicationObjectFactory().getNewApplicationErrorAlertObject("gov.fda.furls.mp.alert.impl.ApplicationErrorAlertServicesImpl") : iApplicationErrorAlertServices);
       }catch (FatalException fe){
          throw new ApplicationErrorAlertException("MailingXMLServices.getAppAlertServicesObject() "+fe.getMessage());
       }
       return (iApplicationErrorAlertServices);
   }
}