package com.tcs.ain.mp.impl;

//JMS imports
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

// Oracle JMS imports
import com.tcs.ain.mp.common.MailingException;
import oracle.jms.AQjmsMessage;

//EJB and JMS imports
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.tcs.ain.mp.IMessageSender;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.ExceptionConstants;

//import java.io.*;
//import gov.fda.furls.mp.helper.*;
/* $Revision: 1.4 $
* Copyright � 2003  Global Net Services Inc
* All Rights Reserved
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/

/**
 * MessageSenderImpl - A java implementation of messaging related business logic.
 *  
 * @version 2.00 29 Aug 2003
 * @author Padma Killi
 * Modified on :12 April 2009
 * 
 * @see IMessageSender
 * @see FatalException
 * @see MailingException
 */

public class MessageSenderImpl implements IMessageSender
{
  //JMS QUEUE private variables
  private QueueConnection queueConnection = null;
  private Queue queue = null;
  private static final String JMSDATASOURCENAME = "java:comp/resource/cts/QueueConnectionFactories/PPF";
  private static final String QUEUENAME         = "java:comp/resource/cts/Queues/PPFQueue";
  private static final String JMSDATASOURCENAME2 = "java:comp/resource/cts/QueueConnectionFactories/PPF_TO_PAPER";
  private static final String QUEUENAME2         = "java:comp/resource/cts/Queues/PPF_TO_PAPERQueue";
  
  
  public MessageSenderImpl(){}

  /**
   * API processMessage is used to send the message to the PPFQueue
   * @param message - XML message.
   * @throws FatalException
   */
   public void processMessage(String message)
        throws FatalException
   {
       //JMS QUEUE variables
          QueueSender sender = null;
          QueueSession queueSession = null;
          String success = "";
          AQjmsMessage msg  = null;
          Context jndiContext = null;
          QueueConnectionFactory queueCF = null;
          
         try
          {

              // Get the Initial Context 
              jndiContext = new InitialContext();
             

              // Lookup the QueueTable and create the QueueConnectionFactory. The JNDI
              // lookup name has been specified in the application.xml file. FFRM_PPF
              // is the name of the Queue Table created in the database 
              queueCF = (QueueConnectionFactory)jndiContext.lookup(JMSDATASOURCENAME);

              // Create a Connection to the QueueTable. The connection is created in
              // stopped mode. No messages will be delivered until the
              // Connection.start method is explicitly called
              queueConnection = queueCF.createQueueConnection();

              
              // Lookup the Queue and create its object. FFRM_PPFQueue is the name of the
              // queue created in the database (Obtain the reference of the queue.
              queue = (Queue)jndiContext.lookup(QUEUENAME);

              
              // Start the Connection
              queueConnection.start();

              // Create or open a Queue Session on this connection. It taken in two parameters which are
              //  1) Transacted - Indicates whether the session is transacted. This
              //     is a boolean value.
              //  2) AcknowledgeMode - Indicates whether the consumer or the client
              //     will acknowledge any messages it receives; ignored if the
              //     session is transacted. Valid values are Session.AUTO_ACKNOWLEDGE,
              //     Session.CLIENT_ACKNOWLEDGE and Session.DUPS_OK_ACKNOWLEDGE.
              
              queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

              // Create an object through which this program enqueues
              sender = queueSession.createSender(queue);

             // Create a Text Message to be enqueued. A TextMessage object is used
             // to send a message containing a String.

              msg = (AQjmsMessage)queueSession.createTextMessage(message);

              msg.setIntProperty("JMS_OracleDelay",5);
              
            // Enqueue the message in the queue. This method uses the default
            // delivery mode, priority, and time to live. But, you can also
            // specify these parameters explicitly through other overloaded
            // methods within the QueueSender Class.
                        
            // Set the message to be stored persistently.
            sender.setDeliveryMode(DeliveryMode.PERSISTENT);

            //Send the message to the queue.
            sender.send(msg);
                 
            // Commit the transaction
            //queueSession.commit();
            success = "Message is Enqueued in "+QUEUENAME+" Successfully !!!";
            System.out.println("Message Id :"+msg.getJMSMessageID()+"   "+success);
          }
          catch(JMSException jmsex)
          {
            jmsex.printStackTrace();
            throw new FatalException("MessageSenderImpl.processMessage().."+ExceptionConstants.JMS_MESSAGING_EXCEPTION);
            
          }
          catch (NamingException nex)
          {
            nex.printStackTrace();
            throw new FatalException("MessageSenderImpl.processMessage().."+ExceptionConstants.JNDI_NAMING_EXCEPTION+":"+JMSDATASOURCENAME+" and the Queue "+QUEUENAME);
          }
          finally
          {
            // Close resources
            try
            {
                sender.close();
                queueSession.close();
                queueConnection.stop();
                queueConnection.close();
                jndiContext.close();
            } 
            catch(JMSException jmsex)
            {
              jmsex.printStackTrace();
               throw new FatalException("MessageSenderImpl.processMessage().."+ExceptionConstants.JMS_MESSAGING_EXCEPTION);
            }
            catch (NamingException nex)
            {
              nex.printStackTrace();
              throw new FatalException("MessageSenderImpl.processMessage()..while closing the JMS resources "+ExceptionConstants.JNDI_NAMING_EXCEPTION+":"+JMSDATASOURCENAME+" and the Queue "+QUEUENAME);
            }
          }
   }

   /*
    * MAILINGS TO BE SEND TO THE NEW PPF_TO_PAPER QUEUE
    * RIGHT NOW USED FOR MAILING TYPE 9 AND 12
    * 
    */
     public void processMessageNewMT(String message)
        throws FatalException
     {
       //JMS QUEUE variables
          QueueSender sender = null;
          QueueSession queueSession = null;
          String success = "";
          AQjmsMessage msg  = null;
          Context jndiContext = null;
          QueueConnectionFactory queueCF = null;
          try
          {

              // Get the Initial Context 
              jndiContext = new InitialContext();
              queueCF = (QueueConnectionFactory)jndiContext.lookup(JMSDATASOURCENAME2);
              queueConnection = queueCF.createQueueConnection();
              queue = (Queue)jndiContext.lookup(QUEUENAME2);
              queueConnection.start();
              queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
              sender = queueSession.createSender(queue);
              msg = (AQjmsMessage)queueSession.createTextMessage(message);
              msg.setIntProperty("JMS_OracleDelay",5);
              sender.setDeliveryMode(DeliveryMode.PERSISTENT);
              sender.send(msg);
              success = "Message is Enqueued in "+QUEUENAME2+" Successfully !!!";
              System.out.println("Message Id :"+msg.getJMSMessageID()+"   "+success);
          }
          catch(JMSException jmsex)
          {
            jmsex.printStackTrace();
            throw new FatalException("MessageSenderImpl.processMessageNewMT().."+ExceptionConstants.JMS_MESSAGING_EXCEPTION);
          }
          catch (NamingException nex)
          {
            nex.printStackTrace();
            throw new FatalException("MessageSenderImpl.processMessageMT().."+ExceptionConstants.JNDI_NAMING_EXCEPTION+":"+JMSDATASOURCENAME2+" and the Queue "+QUEUENAME2);
          }
          finally
          {
            // Close resources
            try
            {
                sender.close();
                queueSession.close();
                queueConnection.stop();
                queueConnection.close();
                jndiContext.close();
            } 
            catch(JMSException jmsex)
            {
              jmsex.printStackTrace();
               throw new FatalException("MessageSenderImpl.processMessageMT().."+ExceptionConstants.JMS_MESSAGING_EXCEPTION);
            }
            catch (NamingException nex)
            {
              nex.printStackTrace();
              throw new FatalException("MessageSenderImpl.processMessageMT()..while closing the JMS resources "+ExceptionConstants.JNDI_NAMING_EXCEPTION+":"+JMSDATASOURCENAME2+" and the Queue "+QUEUENAME2);
            }
          }
   }

 
}