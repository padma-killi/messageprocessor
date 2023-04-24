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
 * FatalException -  Application level Fatal Exception class. 
 *
 * @version 2.00 8 April 2009
 * @author Padma Killi
 * 
 * @see java.lang.Exception
*/

public class FatalException  extends Exception
{
  public FatalException()
  {
    super();
  }
  public FatalException(String msg)
  {
    super(msg);
  }
  
  
}