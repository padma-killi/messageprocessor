package com.tcs.ain.mp.alert;

/* $Revision: 1.1.1.1 $
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

import com.tcs.ain.mp.alert.impl.ApplicationErrorAlertServicesImpl;

/**
 * IApplicationErrorAlertServices -  A  java interface that handles the application
 * error notification services.
 * 
 * @version 2.00 8 April 2009
 * @author Padma Killi
 * 
 * @see ApplicationErrorAlertServicesImpl
*/

public interface IApplicationErrorAlertServices 
{
   /**
    * The API sendErrorAlert() is used to notify the application help desk(Development)
    * about the application related errors.
    * @param message - The alert message.
    * @param isFatal - A boolean value that determines whether the error is fatal or not.
    */
   public void sendErrorAlert(String message,boolean isFatal);
}
