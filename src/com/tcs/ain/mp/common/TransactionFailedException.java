package com.tcs.ain.mp.common;

/* $Revision: 1.1 $
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * TransactionFailedException -  Application Exception that will be propagated 
 * to the beans client.
 * @version  1.0  01 Dec 2009
 * 
 * @author Padma Killi
 * @see java.lang.Exception
 * @since 1.0
 */
 
public class TransactionFailedException extends Exception
{
  public TransactionFailedException(){
    super();
  }
  public TransactionFailedException(String msg){
    super(msg);
  }
}
