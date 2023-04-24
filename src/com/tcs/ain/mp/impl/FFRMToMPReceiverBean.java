package com.tcs.ain.mp.impl;

import javax.ejb.MessageDrivenBean;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.ejb.MessageDrivenContext;

import javax.jms.*;
import javax.naming.*;

import oracle.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;

import com.tcs.ain.mp.MessageProcessorLocalHome;
import com.tcs.ain.mp.MessageProcessorLocal;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.MailingException;
import com.tcs.ain.mp.common.ApplicationErrorAlertException;
import com.tcs.ain.mp.alert.IApplicationErrorAlertServices;
import com.tcs.ain.mp.factory.MessageProcessorFactory;
import java.util.Date;
import java.text.DateFormat;
import com.tcs.ain.mp.common.Util;


/* $Revision: 1.4 $
* Copyright � 2003  Global Net Services Inc
* All Rights Reserved
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/

/**
 * FFRMToMPReceiverBean - This MessageDriven Bean is used to receive messages from FFRM-MP Queue.
 * The FFRM-MP Queue is where all the messages from FFRM resides.
 * @version 2.00 22 Aug 2003
 * @author Padma Killi
 * 
 * @see javax.ejb.MessageDrivenBean
 * @see javax.jms.MessageListener
 */

public class FFRMToMPReceiverBean implements MessageDrivenBean, MessageListener 
{
    private transient MessageDrivenContext  m_ctx = null;
    private IApplicationErrorAlertServices iApplicationErrorAlertServices = null;
       
 /* Constructor, which is public and takes no arguments.*/
  public FFRMToMPReceiverBean(){}

  public void ejbCreate()
  {
      /* no implementation is necessary for this MDB
        An MDB does not carry state for an individual client. However, you can
        retrieve state for use across many calls for multiple clients - state
        such as an entity bean reference or a database connection. If so,
        retrieve these within the ejbCreate and remove them in the
        ejbRemove method. */
        
  }

  public void onMessage(Message msg)
  {
     /* The whole point for this message MDB is to receive and print
        messages. It is not complicated, but it shows how MDBs are set up to
        receive JMS messages from queues and topics. */

        System.out.println("FFRMTOMPReceiver recieves message from FFRM_MP Queue at :"+DateFormat.getDateTimeInstance().format(new Date()));

        TextMessage  textMsg = null;
        Context ctx          = null;
        String messageInitiator = null;
        String messageSubject = null;
        
        try
        {
            /* This message was created as a JMS TextMessage. */
            if (msg instanceof TextMessage)
            {
                /* Convert the BytesMessage into printable text... */
                textMsg = (TextMessage) msg;
                String  txt = ((AQjmsTextMessage)textMsg).getText();
              //  System.out.println(txt);
              //just of testing - 
                messageInitiator = "FFRM";//textMsg.getStringProperty("INITIATOR");
                messageSubject  = "MAILINGS";
                //messageInitiator = textMsg.getStringProperty("INITIATOR");
                //messageSubject  = textMsg.getStringProperty("MESSAGE-SUBJECT");

                System.out.println("The initiator :"+messageInitiator);
               System.out.println("The message subject :"+messageSubject);
                
                /* Print out message */
               // System.out.println(txt);

                 /**
                 * Call the message processor API for further processing of the message.
                 */
                ctx = getContext(); 
                MessageProcessorLocalHome messageProcessorLocalHome = getMessageProcessorLocalHome(ctx);
                MessageProcessorLocal messageProcessorLocal = messageProcessorLocalHome.create();

                messageProcessorLocal.processXMLMessage(txt,messageInitiator);
            }
        }
        catch (JMSException jmse)
        {
            jmse.printStackTrace();
            //send error alert to the development team.
            try{
                   getAppAlertServicesObject().sendErrorAlert("Fatal Error - FFRMToMPReceiverBean.onMessage().JMSException occurred.The Transaction is set to rollback.<br><br>Error Description :"+jmse.getMessage()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,true);
            }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
            
           //Use this method for error handling. If an exception occurs, 
           //setRollbackOnly marks the current transaction so that the only
           //possible outcome of the transaction is a rollback.
           //the container does not acknowledge processing the message.
           //In this case, the JMS provider will redeliver the unacknowledged 
           //message in the future.
           m_ctx.setRollbackOnly();
        }
        catch (NamingException ne)
        {
            //This is a severe exception
            ne.printStackTrace();
            //send error alert to the development team.
            try{
                   getAppAlertServicesObject().sendErrorAlert("Fatal Error - FFRMToMPReceiverBean.onMessage().NamingException occurred.The Transaction is set to rollback.<br><br>Error Description :"+ne.getMessage()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,true);
            }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
            
            //set the trans'n to roll back.
            m_ctx.setRollbackOnly();
        }
        catch (CreateException ce)
        {
           //This is a severe exception
            ce.printStackTrace();
            //send error alert to the development team.
            try{
                   getAppAlertServicesObject().sendErrorAlert("Fatal Error - FFRMToMPReceiverBean.onMessage().CreateException occurred.The Transaction is set to rollback.<br><br>Error Description :"+ce.getMessage()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,true);
            }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
            
            //set the trans'n to roll back.
            m_ctx.setRollbackOnly();
        }
        catch (FatalException fe)
        {
            //This is a severe exception
            fe.printStackTrace();
            //send error alert to the development team.
            try{
                   getAppAlertServicesObject().sendErrorAlert("Fatal Error - FFRMToMPReceiverBean.onMessage().FatalException occurred.The Transaction is set to rollback.<br><br>Error Description :"+fe.getMessage()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,true);
            }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
            //set the trans'n to roll back.
            m_ctx.setRollbackOnly();
        }
        catch (MailingException me)
        {
           //This is not a severe exception
            me.printStackTrace();
            //send error alert to the development team.
             try{
                   getAppAlertServicesObject().sendErrorAlert("Error - FFRMToMPReceiverBean.onMessage().MailingException occurred.<br><br>Error Description :"+me.getMessage()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,false);
             }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
        }
        catch (NullPointerException npe)
        {
           //send error alert to the development team.
             try{
                   getAppAlertServicesObject().sendErrorAlert("Fatal Error - FFRMToMPReceiverBean.onMessage().NullPointerException occurred.The Transaction is set to rollback.<br><br>Error Description :"+npe.getMessage()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,true);
             }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
          //set the trans'n to roll back.
            m_ctx.setRollbackOnly();   
        }
        catch (Throwable te) {
            te.printStackTrace();
            //send error alert to the development team.
             try{
                   getAppAlertServicesObject().sendErrorAlert("Error - FFRMToMPReceiverBean.onMessage().Other Exception occurred.The Transaction is set to rollback.<br><br>Error Description :"+te.toString()+"<br><br>Error Occurred at :"+Util.getDate(Util.getTimestamp().toString(),3)+"<br><br>Message Context :"+messageSubject,false);
             }catch (ApplicationErrorAlertException ae){/*No action is necessary here.*/}
          //set the trans'n to roll back.
           // m_ctx.setRollbackOnly();   
        }
        finally
        {
            try{ 
                 if (ctx != null)
                   ctx.close();
            } catch (Exception e){
              e.printStackTrace();
              System.out.println("FFRMTOMPReceiverBean.onMessage()..An error occured while closig the JNDI Context!");
            }
        }
  }

  public void ejbRemove()
  {
     /* no implementation is necessary for this MDB*/
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc)
  {
    /* As with all EJBs, you must set the context in order to be
     able to use it at another time within the MDB methods. */
     this.m_ctx = mdc;       
  }

 /**
  * The API to get the Context object.
  * @return Context.
  * @throws NamingException
  */
  private static Context getContext() throws NamingException
  {
    return (new InitialContext());
  }
   
  /**
  * Get the EJB referrence of MessageProcessor Local Home
  * @param context - The JNDI context.
  * @throws NamingException
  */
  private MessageProcessorLocalHome getMessageProcessorLocalHome(Context context) throws NamingException
  {
      return (MessageProcessorLocalHome)context.lookup("java:comp/env/ejb/MessageProcessorLocal");
  }
  
  /**
    * The API getAppAlertServicesObject() is to initialize the IApplicationErrorAlertServices Object if it is
    * not initialized.
    * @return IApplicationErrorAlertServices - The Application Error Alert Object.
    * @throws ApplicationErrorAlertException
    */
   private IApplicationErrorAlertServices getAppAlertServicesObject() 
        throws ApplicationErrorAlertException
   {
       try{
         iApplicationErrorAlertServices = (iApplicationErrorAlertServices == null ? MessageProcessorFactory.getApplicationObjectFactory().getNewApplicationErrorAlertObject("gov.fda.furls.mp.alert.impl.ApplicationErrorAlertServicesImpl") : iApplicationErrorAlertServices);
       }catch (FatalException fe){
          throw new ApplicationErrorAlertException("FFRMToMPReceiverBean.getAppAlertServicesObject() "+fe.getMessage());
       }
       return (iApplicationErrorAlertServices);
   }

  private MessageProcessorLocalHome getMessageProcessorLocalHome() throws NamingException
  {
    final InitialContext context = new InitialContext();
    return (MessageProcessorLocalHome)context.lookup("java:comp/env/ejb/MessageProcessorLocal");
  }


}