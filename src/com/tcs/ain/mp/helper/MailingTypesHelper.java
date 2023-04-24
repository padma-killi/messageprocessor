package com.tcs.ain.mp.helper;

import java.util.HashMap;

/* $Revision: 1.1.1.1 $
* Copyright � 2003  Global Net Services Inc
* All Rights Reserved
*
* This is unpublished proprietary source code.
* The copyright notice above does not evidence any actual or
* intended publication of such source code.
*/
/**
 * The MailingTypesHelper is a final class which is used to get the mapping code
 * for a given mailing type name
 * Date   :09/20/2003
 * @author :Padma Killi 
 * @version:2.0
 */
 
public final class MailingTypesHelper 
{
  final static HashMap mailingTypesMap = new HashMap();

  static 
  {
     mailingTypesMap.put("Initial Agent Return Receipt Pending","IARRP");
     mailingTypesMap.put("Initial Facility Return Receipt Pending","IFRRP");
     mailingTypesMap.put("Initial Agent Assignment Notification","IAAN");
     mailingTypesMap.put("Initial Facility Assignment Notification","IFAN");
     mailingTypesMap.put("Registration Modified by FDA","RMBF");
     mailingTypesMap.put("Registration Cancelled","RC");
     mailingTypesMap.put("Transfer of Ownership Notification","TOON");
     mailingTypesMap.put("Facility Merger Notification","FMN");
     mailingTypesMap.put("Initial Paper Registration","IPR");
     mailingTypesMap.put("Facility without US Agent","FWUA");
     mailingTypesMap.put("Agent Re-Assignment","ARA");
     mailingTypesMap.put("PIN Modification","PINM");
  }
  
  /**
  * The API getMailingTypeNameCode() is used to get the mailing type name code
  * for a given mailing type name.
  * @param mailingTypeName - The given mailing type name.
  */
  public static String getMailingTypeNameCode(String mailingTypeName)
  {
         return ((String)mailingTypesMap.get(mailingTypeName));
  }
}