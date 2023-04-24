package com.tcs.ain.mp.oaa.impl;

//mp imports
import com.tcs.ain.mp.IMailingXMLServices;
import com.tcs.ain.mp.impl.MailingXMLServices;
import com.tcs.ain.mp.manager.ResourceManager;
import com.tcs.ain.mp.ppf.jaxb.ObjectFactory;
import com.tcs.ain.mp.ppf.jaxb.Transaction;
import com.tcs.ain.mp.ppf.jaxb.TransactionType;

//mp specific JAXB imports
import gov.fda.furls.mp.ppf.jaxb.*;

//java.io and util imports
import java.io.*;
import java.util.*;

//JAXB imports
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.*;

//Common Imports
import com.tcs.ain.mp.common.ExceptionConstants;
import com.tcs.ain.mp.common.MailingException;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.ApplicationErrorAlertException;
import com.tcs.ain.mp.common.Util;

//Java Mail imports
import javax.mail.SendFailedException;
import javax.mail.MessagingException;

/**
 * OAAMailingXMLServicesImpl -  A  java implementation of the interface IMailingXMLServices
 * that implements the Online Account Administration's mailing notifications. 
 * 
 * @version 2.00 7 April 2009
 * @author Padma Killi
 * 
 * @see IMailingXMLServices
 * @see MailingXMLServices
 * @see ExceptionConstants
 * @see ResourceManager
 * @see MailingException
 * @see FatalException
 * @see ApplicationErrorAlertException
 * @see Util
 * @see javax.mail.SendFailedException
 * @see javax.mail.MessagingException
*/

public class OAAMailingXMLServicesImpl extends MailingXMLServices implements IMailingXMLServices, java.io.Serializable
{
  public OAAMailingXMLServicesImpl(){}

  /**
   * The API processMessage() is used to process the OAA related mailing requests.
   * @param message - The orginal XML message that OAA initiated.
   * @throws MailingException
   * @throws FatalException
   */
  public void processMessage(String message) 
     throws MailingException,FatalException
  {
     boolean isXMLsentToPPF = false;
     String errorMessage = null;
     try 
     {
           JAXBContext jaxbContext = JAXBContext.newInstance("gov.fda.furls.mp.ppf.jaxb",this.getClass().getClassLoader());
           Unmarshaller um = jaxbContext.createUnmarshaller();
       
           Transaction transaction = (Transaction)um.unmarshal( new StreamSource( new StringReader( message.toString() ) ) );
           TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();
              
           if ("ACCOUNTMANAGEMENT".equals(controlAreaType.getInitiator()))
           {
              TransactionType.DataAreaType.SimpleTextMailingType sTextMailingType =  transaction.getDataArea().getSimpleTextMailing();

              //System.out.println("To Address :"+sTextMailingType.getEmail()+" From Address :"+ResourceManager.getInstance().getAcctMgmtFromAddress());
              //System.out.println("Email Content :"+sTextMailingType.getMailcontent());
          
              if (sTextMailingType.getEmail()  != null){
                 
                 try {
                     sendEmail(sTextMailingType.getEmail(),ResourceManager.getInstance().getAcctMgmtFromAddress(),ResourceManager.getInstance().getAcctMgmtEmailSubject(),sTextMailingType.getMailcontent());
                 }
                 catch (SendFailedException sfe)
                 {
                      sfe.printStackTrace();
                      //send the OAA mailing request to PPF.
                      notifyPaperProcessWithEmailErrors(message,ExceptionConstants.EMAIL_ERROR_CODE,ExceptionConstants.EMAIL_ERROR_DESC);
                      isXMLsentToPPF = true;
                 }
                 catch(MessagingException me)
                 {
                      me.printStackTrace();
                      //send the OAA mailing request to PPF.
                      notifyPaperProcessWithEmailErrors(message,ExceptionConstants.EMAIL_ERROR_CODE,ExceptionConstants.EMAIL_ERROR_DESC);
                      isXMLsentToPPF = true;
                 }
                 catch (Exception me)
                 {
                      me.printStackTrace();
                      //send the OAA mailing request to PPF.
                      notifyPaperProcessWithEmailErrors(message,ExceptionConstants.EMAIL_ERROR_CODE,ExceptionConstants.EMAIL_ERROR_DESC);
                      isXMLsentToPPF = true;
                 }
                 if (!isXMLsentToPPF){
                   //If there is no exception which indicates that the email has been sent successfully
                   //Then log the XML message to PPF queue.
                   addLogToXMLAndNotifyPaperProcess(message);
                 }
              }
              else //send the XML message to the PPF queue to send it via paper.
              {
                notifyPaperProcess(message);
              }
           }
     }
     catch (MailingException me)
     {
       //This is not a severe exception.
       me.printStackTrace();
       errorMessage = "ERROR: OAAMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.MAILING_EXCEPTION;
       throw new MailingException(errorMessage);
       
     }
     catch(JAXBException jaxe) 
     {
        //This is a severe exception and the transaction should  roll back. The JMS provider should re-send the message.
        jaxe.printStackTrace();
        errorMessage = "FATAL ERROR: OAAMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.JAXB_EXCEPTION;
        throw new FatalException(errorMessage);
     }  
     catch(FatalException fe) 
     {
       //This is a severe exception and the transaction should roll back. The JMS provider should re-send the message.
        fe.printStackTrace();
        errorMessage = "FATAL ERROR: OAAMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp(),3)+" "+fe.getMessage();
        throw new FatalException(errorMessage);
     }
  }

  /**
   * The API addLogToXMLAndNotifyPaperProcess(String message) is used to unmarshall only one element and 
   * change the verb and marshall back again and send it to PPF queue.
   * @param message - XML message
   * @throws MailingException
   */
   private  void addLogToXMLAndNotifyPaperProcess(String message)
     throws MailingException
   {
       try
       {
           // create a JAXBContext
           JAXBContext jaxbContext = JAXBContext.newInstance("gov.fda.furls.mp.ppf.jaxb",this.getClass().getClassLoader());
           // create an unmarshaller
           Unmarshaller um = jaxbContext.createUnmarshaller();

           Transaction transaction = (Transaction)um.unmarshal( new StreamSource( new StringReader( message.toString() ) ) );
           TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();

           if ("Mailing Request".equals(controlAreaType.getVerb()) || "Send Email".equals(controlAreaType.getVerb()))
           {
              //Change the verb to Mailing Request Email Log
              controlAreaType.setVerb("Mailing Request Email Log");
                      
              //create a Marshaller and marshal to the StreamResult
              Marshaller m = jaxbContext.createMarshaller();
              m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
              StringWriter sw = new StringWriter();
              StreamResult result = new StreamResult(sw);
              m.marshal(transaction, result );

              System.out.println("OAAMailingXMLServicesImpl.addLogToXMLAndNotifyPaperProcess().. Logging original XML message to PPF queue");
               
              System.out.println(sw.toString());
               
              //Send the XML to the message Queue.
              notifyPaperProcess(sw.toString()); 
           }
       }
       catch (FatalException fe)
       {
           //This is not a severe exception that happenned in the context from where this API is called.
           //So throw a simple Mailing Exception back to the caller.
           throw new MailingException("OAAMailingXMLServices.addLogToXMLAndNotifyPaperProcess..Unable to notify PPF about OAA Mailing log.");
       }
       catch( JAXBException je )
       {
           //This is not a severe exception that happenned in the context from where this API is called.
           //So throw a simple Mailing Exception back to the caller.
           throw new MailingException("OAAMailingXMLServices.addLogToXMLAndNotifyPaperProcess()..Jaxb Exception - Unable to notify PPF about OAA Mailing log.");
       }
       catch(Exception e)
       {
          //This is not a severe exception that happenned in the context from where this API is called.
           //So throw a simple Mailing Exception back to the caller.
           throw new MailingException("OAAMailingXMLServices.addLogToXMLAndNotifyPaperProcess()..Unable to notify PPF about OAA Mailing log.");
       }
   }
  /**
    * The API throws notifyPaperProcessWithEmailErrors is used to add Exceptions to the XML message
    * incase en email cannot be sent to the customer.
    * @param message - XML message
    * @param code - The exception code
    * @param desc - The exception description
    * @throws FatalException
    */
    private  void notifyPaperProcessWithEmailErrors(String message,String code, String desc)
      throws FatalException
    {
          try
          {
                // create a JAXBContext
               JAXBContext jaxbContext = JAXBContext.newInstance("gov.fda.furls.mp.ppf.jaxb",this.getClass().getClassLoader());

               // create an ObjectFactory instance.
              // if the JAXBContext had been created with mutiple pacakge names,
              // we would have to explicitly use the correct package name when
              // creating the ObjectFactory.            
              ObjectFactory objFactory = new ObjectFactory();
          
               // create an unmarshaller
               Unmarshaller um = jaxbContext.createUnmarshaller();

               Transaction transaction = (Transaction)um.unmarshal( new StreamSource( new StringReader( message.toString() ) ) );

               //Get the Data Area Type
               TransactionType.DataAreaType dataAreaType = transaction.getDataArea();

               //Get the Control Area
               TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();
               
               //Get the reference of the Exception type
               TransactionType.DataAreaType.ExceptionsType exceptionsType = objFactory.createTransactionTypeDataAreaTypeExceptionsType();  //transaction.getDataArea().getExceptions();
                  
               TransactionType.DataAreaType.ExceptionsType.ExceptionType exceptionType = objFactory.createTransactionTypeDataAreaTypeExceptionsTypeExceptionType();
               
               //Get a reference to the Exception list
               List exceptionList = exceptionsType.getException();
               exceptionType.setCode(code);
               exceptionType.setDescription(desc); 
               exceptionList.add(exceptionType);
              
               //setting the exceptions to the dataarea.
               dataAreaType.setExceptions(exceptionsType);
               
               //Set the verb to Mailing Request incase the initiator is Account Management.
               if ("ACCOUNTMANAGEMENT".equals(controlAreaType.getInitiator()))
               {
                 controlAreaType.setVerb("Mailing Request");
               }
               //create a Marshaller and marshal to the StreamResult
               Marshaller m = jaxbContext.createMarshaller();
               m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
               StringWriter sw = new StringWriter();
               StreamResult result = new StreamResult(sw);
               m.marshal(transaction, result );

               System.out.println("Adding Exceptions to the original XML message to PPF queue"); 

               System.out.println(sw.toString());
               
               //Send the XML to the message Queue.
               notifyPaperProcess(sw.toString());
          }
          catch( JAXBException je )
          {
              //This is a severe exception that happenned in the context where this API is called.So throw FatalException.
              //The transaction should roll back and the JMS Provider should re-send the message.
              je.printStackTrace();
              throw new FatalException("OAAMailingXMLServicesImpl.notifyPaperProcessWithEmailErrors().."+ExceptionConstants.JAXB_EXCEPTION);
          }   
          catch (Exception e)
          {
             //This is a severe exception that happenned in the context where this API is called.So throw FatalException.
              //The transaction should roll back and the JMS Provider should re-send the message.
              e.printStackTrace();
              throw new FatalException("OAAMailingXMLServicesImpl.notifyPaperProcessWithEmailErrors().."+ExceptionConstants.GENERIC_EXCEPTION);
          }
    }
}
