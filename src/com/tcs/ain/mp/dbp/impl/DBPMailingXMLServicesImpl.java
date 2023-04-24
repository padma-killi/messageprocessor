package com.tcs.ain.mp.dbp.impl;

//mp imports
import com.tcs.ain.mp.IMailingXMLServices;
import com.tcs.ain.mp.common.ExceptionConstants;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.MailingException;
import com.tcs.ain.mp.common.Util;
import com.tcs.ain.mp.impl.MailingXMLServices;
import com.tcs.ain.mp.ppf.jaxb.Transaction;
import com.tcs.ain.mp.ppf.jaxb.TransactionType;

//mp specific JAXB imports
import gov.fda.furls.mp.ppf.jaxb.*;

//java.io and util imports
import java.io.*;

//JAXB imports
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.*;

//Common Imports

//Java Mail imports
import javax.mail.SendFailedException;
import javax.mail.MessagingException;
public class DBPMailingXMLServicesImpl extends MailingXMLServices implements IMailingXMLServices, java.io.Serializable
{
  public DBPMailingXMLServicesImpl()
  {
  }
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
              
           if ("DBP".equals(controlAreaType.getInitiator()))
           {
                System.out.println("COMING HERE INSIDE DBP MAILINGS OF INVALID REGISTRATION");
//              TransactionType.DataAreaType.SimpleTextMailingType sTextMailingType =  transaction.getDataArea().getSimpleTextMailing();
//
//              //System.out.println("To Address :"+sTextMailingType.getEmail()+" From Address :"+ResourceManager.getInstance().getAcctMgmtFromAddress());
//              //System.out.println("Email Content :"+sTextMailingType.getMailcontent());
//          
//              if (sTextMailingType.getEmail()  != null){
//                 
//                 try {
//                     sendEmail(sTextMailingType.getEmail(),ResourceManager.getInstance().getAcctMgmtFromAddress(),ResourceManager.getInstance().getAcctMgmtEmailSubject(),sTextMailingType.getMailcontent());
//                 }
//                 catch (SendFailedException sfe)
//                 {
//                      sfe.printStackTrace();
//                      //send the OAA mailing request to PPF.
//                   //   notifyPaperProcessWithEmailErrors(message,ExceptionConstants.EMAIL_ERROR_CODE,ExceptionConstants.EMAIL_ERROR_DESC);
//                      isXMLsentToPPF = true;
//                 }
//                 catch(MessagingException me)
//                 {
//                      me.printStackTrace();
//                      //send the OAA mailing request to PPF.
//                      notifyPaperProcessWithEmailErrors(message,ExceptionConstants.EMAIL_ERROR_CODE,ExceptionConstants.EMAIL_ERROR_DESC);
//                      isXMLsentToPPF = true;
//                 }
//                 catch (Exception me)
//                 {
//                      me.printStackTrace();
//                      //send the OAA mailing request to PPF.
//                      notifyPaperProcessWithEmailErrors(message,ExceptionConstants.EMAIL_ERROR_CODE,ExceptionConstants.EMAIL_ERROR_DESC);
//                      isXMLsentToPPF = true;
//                 }
//                 if (!isXMLsentToPPF){
//                   //If there is no exception which indicates that the email has been sent successfully
//                   //Then log the XML message to PPF queue.
//                 //  addLogToXMLAndNotifyPaperProcess(message);
//                 }
              }
              else //send the XML message to the PPF queue to send it via paper.
              {
              //  notifyPaperProcess(message);
              }
         //  }
     }
//     catch (MailingException me)
//     {
//       //This is not a severe exception.
//       me.printStackTrace();
//       errorMessage = "ERROR: OAAMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.MAILING_EXCEPTION;
//       throw new MailingException(errorMessage);
//       
//     }
     catch(JAXBException jaxe) 
     {
        //This is a severe exception and the transaction should  roll back. The JMS provider should re-send the message.
        jaxe.printStackTrace();
        errorMessage = "FATAL ERROR: OAAMailingXMLServicesImpl.processMessage() "+ Util.getDate(Util.getTimestamp().toString(),3)+" "+ ExceptionConstants.JAXB_EXCEPTION;
        throw new FatalException(errorMessage);
     }  
     catch(Exception e) 
     {
       //This is a severe exception and the transaction should roll back. The JMS provider should re-send the message.
        e.printStackTrace();
        errorMessage = "FATAL ERROR: OAAMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp(),3)+" "+e.getMessage();
        //throw new FatalException(errorMessage);
     }
  }

}