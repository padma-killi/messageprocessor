package com.tcs.ain.mp.common;

/* $Revision: 1.1.1.1 $
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * ExceptionConstants -  A  final java class that has all the exception related descriptions
 * 
 * @version 2.00 8 April 2009
 * @author Padma Killi
*/

public final class ExceptionConstants 
{
  public static final String EMAIL_ERROR_CODE = "1000";
  public static final String EMAIL_ERROR_DESC = "EMAIL ERROR";
  public static final String MESSAGE_PROCESSOR_ERROR_CODE = "2000";
  public static final String MESSAGE_PROCESSOR_ERROR_DESC = "MESSAGE PROCESSING ERROR"; 
  
  public static final String CLASS_NOT_FOUND_EXCEPTION ="Fatal Error: Unable to find the class ";
  public static final String ILLEGAL_ACCESS_EXCEPTION= "Fatal Error: Illegally trying to access the object ";
  public static final String INSTANTIATE_EXCEPTION="Fatal Error: Unable to Instantiate the object ";
  public static final String MAILING_REQUEST_NOT_SEND_TO_PPF_QUEUE="Fatal Error: Unable to send the mailing request to PPF Queue ";
  public static final String XML_PARSE_EXCEPTION="Fatal Error: Unable to parse the XML message "; 
  public static final String JAXB_EXCEPTION="Fatal Error: Unable to process the xml because of JAXB error";
  public static final String GENERIC_EXCEPTION="Fatal Error: A generic exception occurred ";
  public static final String FILE_REMOVAL_EXCEPTION="Error: Unable to remove the file ";
  public static final String JMS_MESSAGING_EXCEPTION="Fatal Error: Unable to send the message to the destination queue/topic ";
  public static final String JNDI_NAMING_EXCEPTION="Fatal Error: Unable to find the JNDI name of the component ";
  public static final String MAILING_EXCEPTION="Error: A Mailing Exception occurred. This exception is not severe. ";
  
}
