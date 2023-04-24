package com.tcs.ain.mp.factory;

import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.Util;
import com.tcs.ain.mp.common.ExceptionConstants;

/* $Revision: 1.1.1.1 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * AbstractApplicationObjectFactory -  This is an abstract class that has a generic behaviour where by the 
 * the sub class can use class reflection to obtain an instance of an object for the given class name. 
 * 
 * @version 2.00 08 April 2009
 * @author Padma Killi
 * 
 * @see java.lang.Object
 * @see Util
 * @see FatalException
 * @see ExceptionConstants
*/
public abstract class AbstractApplicationObjectFactory {
	
	/**
	 * The API getNewInstance() uses  reflection to obtain an instance of an object
	 * for the given class name.
	 * 
	 * @param implClass - The name of the implementation class
	 * @return Object   - The object.
   * @throws FatalException
	 */
	 public Object getNewInstance(String implClass) 
      throws FatalException
	 {
			Object o = null;
			try{
				Class cls = Class.forName(implClass);
				o = cls.newInstance();
			}
			catch (ClassNotFoundException cnfe){
				cnfe.printStackTrace();
        throw new FatalException("INFO "+Util.getDate(Util.getTimestamp().toString(),3)+" AbstractApplicationObjectFactory.getNewInstance() "+implClass+" - "+ ExceptionConstants.CLASS_NOT_FOUND_EXCEPTION);        
			}catch (IllegalAccessException iAe){
				iAe.printStackTrace();
				throw new FatalException("INFO "+Util.getDate(Util.getTimestamp().toString(),3)+" AbstractApplicationObjectFactory.getNewInstance() "+implClass+" - "+ExceptionConstants.ILLEGAL_ACCESS_EXCEPTION);
			}catch (InstantiationException ie){
				ie.printStackTrace();
				throw new FatalException("INFO "+Util.getDate(Util.getTimestamp().toString(),3)+" AbstractApplicationObjectFactory.getNewInstance() "+implClass+" - "+ExceptionConstants.INSTANTIATE_EXCEPTION);
			}
			catch(Exception e){
				e.printStackTrace();
				throw new FatalException("INFO "+Util.getDate(Util.getTimestamp().toString(),3)+" AbstractApplicationObjectFactory.getNewInstance() "+implClass+" - "+ExceptionConstants.GENERIC_EXCEPTION);
			}
			
			return (o);
	 }

}
