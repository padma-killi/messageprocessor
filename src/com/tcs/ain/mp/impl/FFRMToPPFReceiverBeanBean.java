package com.tcs.ain.mp.impl;

import javax.ejb.MessageDrivenBean;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.ejb.MessageDrivenContext;
import com.tcs.ain.mp.manager.MailManager;
import com.tcs.ain.mp.manager.ResourceManager;
import oracle.jms.*;
import javax.jms.*;
import java.util.Date;
import java.text.DateFormat;

//Java Mail imports
import javax.mail.SendFailedException;
import javax.mail.MessagingException;

/* $Revision: 1.2 $
* Copyright � 2003  Global Net Services Inc
* All Rights Reserved
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/

/**
 * FFRMToPPFReceiverBean - This MessageDriven Bean is used to receive messages from FFRM-PPF Queue.
 * The FFRM-PPF Queue is used to notify PPF help desk about various exceptions that occur while FFRM
 * process the Paper related registration informations.
 * @version 1.00 13 Jan 2009
 * @author Padma Killi
 * 
 * @see javax.ejb.MessageDrivenBean
 * @see javax.jms.MessageListener
 */
 
public class FFRMToPPFReceiverBeanBean implements MessageDrivenBean, MessageListener 
{

  private transient MessageDrivenContext  m_ctx = null;

  /* Constructor, which is public and takes no arguments.*/
  public FFRMToPPFReceiverBeanBean(){}

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

        System.out.println("FFRMTOPPFReceiver recieves message from FFRM_PPF Queue at :"+DateFormat.getDateTimeInstance().format(new Date()));

        TextMessage  textMsg = null;
        try
        {
            /* This message was created as a JMS TextMessage. */
            if (msg instanceof TextMessage)
            {
                /* Convert the BytesMessage into printable text... */
                textMsg = (TextMessage) msg;
                String  txt = ((AQjmsTextMessage)textMsg).getText();

                //Invoke the Mail Manager to send the email to PPF help desk.
                MailManager.sendHTMLMail(ResourceManager.getInstance().getPpfHelpDeskEmail(),ResourceManager.getInstance().getFromAddress(),ResourceManager.getInstance().getPpfHelpDeskEmailSubject(),txt);
            }
        }
        catch (JMSException jmse)
        {
          jmse.printStackTrace();
           //Use this method for error handling. If an exception occurs, 
           //setRollbackOnly marks the current transaction so that the only
           //possible outcome of the transaction is a rollback.
           m_ctx.setRollbackOnly();
        }
        catch (SendFailedException sfe){
          sfe.printStackTrace();
           m_ctx.setRollbackOnly();
        }
        catch (MessagingException me){
          me.printStackTrace();
          m_ctx.setRollbackOnly();
        }
        catch (Throwable te) {
            System.out.println("FFRMToPPFReceiverBean.onMessage: Exception: " +te.toString());
        }
        /*catch (Exception e){
            e.printStackTrace();
            throw  new  RuntimeException("onMessage throws exception");
        }*/
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
  
}