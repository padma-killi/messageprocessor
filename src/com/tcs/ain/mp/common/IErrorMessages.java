package com.tcs.ain.mp.common;

/* $Revision: 1.1 $
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/
/**
 * IMessages - A java interface that defines a unique messages especially
 * for the system level checked exceptions.
 * 
 * @author Padma Killi  
 * @version $
 * @since 1.0
 */
public interface IErrorMessages 
{
  
  public final static String IO_EXCEPTION_INSERT_MAILING_LOG="An IO exception occured while inserting data into Mailings_Log database table";
  public final static String ROLL_BACK_INSERT_MAILING_LOG="An Error occurred while rolling back the insert record operation";
  public final static String SQL_EXCEPTION_INSERT_MAILING_LOG="A SQL exception occurred while inserting the data into Mailings_Log database table";
  public final static String EXCEPTION_INSERT_MAILING_LOG="A  exception occurred while inserting the data into Mailings_Log database table";
}
