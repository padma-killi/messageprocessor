package com.tcs.ain.mp.common;

/* $Revision: 1.1.1.1 $
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * ResourceException -  Application Exception that will be propagated
 * to the bean.
 * @version 2.00 27 June 2003
 * @author Padma Killi
 * 
 * @see java.lang.Exception
*/
public class ResourceException extends Exception
{
  public ResourceException()
  {
     super();
  }
  public ResourceException(String msg)
  {
    super(msg);
  }
}
