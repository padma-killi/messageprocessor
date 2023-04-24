package com.tcs.ain.mp.factory.impl;

import com.tcs.ain.mp.IMailingXMLServices;
import com.tcs.ain.mp.IMessageSender;
import com.tcs.ain.mp.alert.IApplicationErrorAlertServices;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.dao.ISendMailingDao;
import com.tcs.ain.mp.factory.AbstractApplicationObjectFactory;
import com.tcs.ain.mp.factory.IApplicationObjectFactory;
import com.tcs.ain.mp.logging.IMailLog;
import com.tcs.ain.mp.manager.ITransformXML;
import gov.fda.furls.mp.logging.impl.*;
/* $Revision: 1.4 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * ApplicationObjectFactoryImpl -  A  factory implementation of the interface IApplicationObjectFactory 
 * object that uses class reflection to obtain an instance of an object for the given class name. 
 * The client will know only the interface and the class name.
 * 
 * @version 2.00 7 April 2009
 * @author Padma Killi
 * 
 * @see IMailingXMLServices
 * @see IMessageSender
 * @see ITransformXML
 * @see FatalException
 * @see IApplicationErrorAlertServices
 * @see AbstractApplicationObjectFactory
*/

public class ApplicationObjectFactoryImpl extends AbstractApplicationObjectFactory implements IApplicationObjectFactory {

	private IMailingXMLServices iMailingXMLServices = null;
  private IMessageSender iMessageSender = null;
  private ITransformXML iTransformXML = null;
  private IApplicationErrorAlertServices  iApplicationErrorAlertServices = null;
  private IMailLog iMailLog = null;
  private ISendMailingDao iSendMailingDao = null;

	/**
	 * The API getNewMailingXMLServicesObject() returns the interface object
	 * IMailingXMLServices for the given implementation name.
	 * @param implClass - The name of the concrete implementation of the interface
   * @return IMailingXMLServices - The interface object reference.
   * @throws FatalException
	 */
	 public IMailingXMLServices getNewMailingXMLServicesObject(String implClass) throws FatalException {
		 return (iMailingXMLServices == null ? (IMailingXMLServices)getNewInstance(implClass) : iMailingXMLServices);
	 }

   /**
	 * The API getNewMessageSenderObject() returns the interface object
	 * IMailLog for the given implementation name.
	 * @param implClass - The name of the concrete implementation of the interface
   * @return IMailLog - The interface object reference.
   * @throws FatalException
	 */
   
   public IMailLog getXMLSenderObject(String implClass)throws FatalException {
  	 return (iMailLog == null ? (IMailLog)getNewInstance(implClass) : iMailLog);
   }
    /**
	 * The API getNewMessageSenderObject() returns the interface object
	 * IMailLog for the given implementation name.
	 * @param implClass - The name of the concrete implementation of the interface
   * @return IMailLog - The interface object reference.
   * @throws FatalException
	 */

   public ISendMailingDao getXMLMessageSenderObject(String implClass)throws FatalException {
  	 return (iSendMailingDao == null ? (ISendMailingDao)getNewInstance(implClass) : iSendMailingDao);
   }
   /**
	 * The API getNewMessageSenderObject() returns the interface object
	 * IMessageSender for the given implementation name.
	 * @param implClass - The name of the concrete implementation of the interface
   * @return IMessageSender - The interface object reference.
   * @throws FatalException
	 */
   public IMessageSender getNewMessageSenderObject(String implClass)throws FatalException {
  	 return (iMessageSender == null ? (IMessageSender)getNewInstance(implClass) : iMessageSender);
   }
   
   /**
	 * The API getNewTransformXMLObject() returns the interface object
	 * ITransformXML for the given implementation name.
	 * @param implClass - The name of the concrete implementation of the interface
   * @return ITransformXML - The interface object reference.
   * @throws FatalException
	 */
   public ITransformXML getNewTransformXMLObject(String implClass)throws FatalException {
  	 return (iTransformXML == null ? (ITransformXML)getNewInstance(implClass) : iTransformXML);
   }

   /**
	 * The API getNewApplicationErrorAlertObject() returns the interface object
	 * IApplicationErrorAlertServices for the given implementation name.
	 * @param implClass - The name of the concrete implementation of the interface
   * @return IApplicationErrorAlertServices - The interface object reference.
   * @throws FatalException
	 */
   public IApplicationErrorAlertServices getNewApplicationErrorAlertObject(String implClass)throws FatalException {
  	 return (iApplicationErrorAlertServices == null ? (IApplicationErrorAlertServices)getNewInstance(implClass) : iApplicationErrorAlertServices);
   }
}
