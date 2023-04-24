package com.tcs.ain.mp.factory;

import com.tcs.ain.mp.IMailingXMLServices;
import com.tcs.ain.mp.IMessageSender;
import com.tcs.ain.mp.alert.IApplicationErrorAlertServices;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.logging.IMailLog;
import com.tcs.ain.mp.manager.ITransformXML;
import gov.fda.furls.mp.logging.impl.*;
import com.tcs.ain.mp.dao.ISendMailingDao;

/* $Revision: 1.3 $
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * IApplicationObjectFactory -  An interface that uses class reflection to obtain 
 * an instance of an object for the given class name. The client will know only the 
 * interface and the class name.
 * 
 * @version 2.00 7 April 2009
 * @author Padma Killi
 * 
 * @see IMailingXMLServices
 * @see IMessageSender
 * @see ITransformXML
 * @see IApplicationErrorAlertServices
*/
public interface IApplicationObjectFactory {

    /**
     * The public API getNewMailingXMLServicesObject() is used to get the implementation of the
     * interface IMailingXMLServices object.
     * @param implClass - The name of the concrete implementation class.
     * @return IMailingXMLServices - The interface object reference.
     * @throws FatalException
     */
	public IMailingXMLServices getNewMailingXMLServicesObject(String implClass) throws FatalException;

   /**
     * The public API getNewMessageSenderObject() is used to get the implementation of the
     * interface IMessageSender object.
     * @param implClass - The name of the concrete implementation class.
     * @return IMessageSender - The interface object reference.
     * @throws FatalException
     */  
  public IMessageSender getNewMessageSenderObject(String implClass) throws FatalException;
    /**
     * The public API getXMLSenderObject is used to get the implementation of the
     * interface IMailLog object.
     * @param implClass - The name of the concrete implementation class.
     * @return IMailLog - The interface object reference.
     * @throws FatalException
     */  
  public IMailLog getXMLSenderObject(String implClass) throws FatalException;

   /**
     * The public API getNewTransformXMLObject() is used to get the implementation of the
     * interface ITransformXML object.
     * @param implClass - The name of the concrete implementation class.
     * @return ITransformXML - The interface object reference.
     * @throws FatalException
     */  
  public ITransformXML getNewTransformXMLObject(String implClass) throws FatalException;

   /**
     * The public API getNewApplicationErrorAlertObject() is used to get the implementation of the
     * interface IApplicationErrorAlertServices object.
     * @param implClass - The name of the concrete implementation class.
     * @return IApplicationErrorAlertServices - The interface object reference.
     * @throws FatalException
     */  
  public IApplicationErrorAlertServices getNewApplicationErrorAlertObject(String implClass) throws FatalException;

	public ISendMailingDao getXMLMessageSenderObject(String implClass) throws FatalException;

  
}
