package com.tcs.ain.mp.client;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;


import javax.naming.NamingException;



import java.sql.*;
import java.util.*;

//JMS Imports
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Queue;
import javax.jms.*;
import javax.xml.bind.*;


import oracle.AQ.*;
import oracle.jms.*;


public class MailManagementServicesClient
{
    //JMS QUEUE private variables
    private QueueConnection queueConnection = null;
    private Queue queue = null;
    private static final String JMSDATASOURCENAME1 = "java:comp/resource/cts/QueueConnectionFactories/FFRM_MP";
    private static final String QUEUENAME1         = "java:comp/resource/cts/Queues/FFRM_MPQueue";
    private static final String JMSDATASOURCENAME2 = "java:comp/resource/cts/QueueConnectionFactories/FFRM";
    private static final String QUEUENAME2         = "java:comp/resource/cts/Queues/FFRMQueue";
    
    public static void main(String [] args)
    {
      try
      { 
        // Context context = getInitialContext();
         MailManagementServicesClient mms = new MailManagementServicesClient();
         mms.receiveMsg();
      }catch(Exception e) 
      {
        e.printStackTrace();
      }
    }
    private static void receiveMsg()
    {
        QueueSession queueSession = null;
        QueueConnection queueConnection = null;
        Context jndiContext = null;
        QueueReceiver receiver = null;
        try
        {
            // Get the Initial Context
            jndiContext = getInitialContext() ; //new InitialContext();


            // Lookup the QueueTable and create the QueueConnectionFactory. The JNDI
            // lookup name has been specified in the application.xml file. FFRM_PPF
            // is the name of the Queue Table created in the database
            QueueConnectionFactory queueCF = (QueueConnectionFactory)jndiContext.lookup("java:comp/resource/cts/QueueConnectionFactories/FFRM_MP");

            // Lookup the Queue and create its object. FFRM_PPFQueue is the name of the
            // queue created in the database
            Queue queue = (Queue)jndiContext.lookup("java:comp/resource/cts/Queues/FFRM_MPQueue");

            // Create a Connection to the QueueTable. The connection is created in
            // stopped mode. No messages will be delivered until the
            // Connection.start method is explicitly called
            queueConnection = queueCF.createQueueConnection();

            // Start the Connection
            queueConnection.start();

            // Create a Session to the queue. It taken in two parameters which are
            //  1) Transacted - Indicates whether the session is transacted. This
            //     is a boolean value.
            //  2) AcknowledgeMode - Indicates whether the consumer or the client
            //     will acknowledge any messages it receives; ignored if the
            //     session is transacted. Valid values are Session.AUTO_ACKNOWLEDGE,
            //     Session.CLIENT_ACKNOWLEDGE and Session.DUPS_OK_ACKNOWLEDGE.
            queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Create an object through which this program enqueues
            receiver = queueSession.createReceiver(queue);
            // Create an object through which this program enqueues

            Message msg = receiver.receive();

            TextMessage textMsg = null;
            if (msg instanceof TextMessage)
            {
                /* Convert the BytesMessage into printable text... */
                textMsg = (TextMessage) msg;
                String  txt = ((AQjmsTextMessage)textMsg).getText();
                // String  txt = new  String(msgdata);
                /* Print out message */
                System.out.println("Message received=" + txt);
                System.out.println("Message Queue of XML data from FFRM-MP");
                String subject = "Message Queue of XML data from FFRM-MP" ;
                //SimpleMailer.sendMail("christophe_ludet@sra.com,vbose@gnsi.com", "gbush@abc.com", subject, txt);
            }
        }
        catch(JMSException jmsex)
        {
            jmsex.printStackTrace();
        }
        catch (NamingException nex)
        {
            nex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                queueConnection.close();
                // Close the JNDI Context
                jndiContext.close();

                queueSession.close();
                receiver.close();
            }
            catch(JMSException jmsex)
            {
                jmsex.printStackTrace();
            }
            catch (NamingException nex)
           {
               nex.printStackTrace();
           }
       }
  }

    
  private static Context getInitialContext() throws NamingException
  {
    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
    env.put(Context.SECURITY_PRINCIPAL, "admin");
    env.put(Context.SECURITY_CREDENTIALS, "welcome");
    env.put(Context.PROVIDER_URL, "ormi://localhost:23891/current-workspace-app");

    return new InitialContext(env);
  }
  private static Timestamp getTimestamp()
  {
     java.util.Date dt = new java.util.Date();
     Timestamp ts = new Timestamp(dt.getTime());
     return ts;
  }
 
}  