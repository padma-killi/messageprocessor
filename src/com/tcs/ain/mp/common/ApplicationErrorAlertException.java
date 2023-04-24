package com.tcs.ain.mp.common;

/* $Revision: 1.1.1.1 $
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * ApplicationErrorAlertException -  Application level Application Error Alert Exception class. 
 *
 * @version 2.00 8 April 2009
 * @author Padma Killi
 * 
 * @see java.lang.Exception
*/

public class ApplicationErrorAlertException  extends Exception
{
  public ApplicationErrorAlertException()
  {
    super();
  }
  public ApplicationErrorAlertException(String msg)
  {
    super(msg);
  }
  
  
}
