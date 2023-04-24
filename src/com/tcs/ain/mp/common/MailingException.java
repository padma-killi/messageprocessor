package com.tcs.ain.mp.common;

/* $Revision: 1.1.1.1 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * MailingException -  Application Exception that will be propagated
 * to the bean.
 * @version 2.00 27 June 2003
 * @author Padma Killi
 * 
 * @see java.lang.Exception
*/
public class MailingException extends Exception
{
  public MailingException()
  {
     super();
  }
  public MailingException(String msg)
  {
    super(msg);
  }
}
