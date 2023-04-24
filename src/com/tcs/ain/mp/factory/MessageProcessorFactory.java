package com.tcs.ain.mp.factory;

import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.helper.Address;
import com.tcs.ain.mp.factory.impl.ApplicationObjectFactoryImpl;

/* $Revision: 1.4 $
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * MessageProcessorFactory -  A  java class that implements the factory pattern.
 * This class instantiates the required object for the given name.
 * 
 * @version 2.00 7 April 2009
 * @author Padma Killi
 * 
 * @see IApplicationObjectFactory
 * @see ApplicationObjectFactoryImpl
 * @see FatalException
*/

public class MessageProcessorFactory 
{
  private static IApplicationObjectFactory iAppObjFactoryImpl = null;
  
   
  /**
   * The API getMailingXMLServicesImpl() returns the object reference for the given 
   * name.
   * @param whichObject - This represents which object that needs to be instantiated.
   * @return Object - The object reference of the implementation.
   * @throws FatalException
   */
  public static Object getMailingXMLServicesImpl(String whichObject) throws FatalException
  {
      if ("FFRM".equals(whichObject))
           return (getApplicationObjectFactory().getNewMailingXMLServicesObject("gov.fda.furls.mp.ffrm.impl.FFRMMailingXMLServicesImpl"));
      else if ("DBP".equals(whichObject))
           return (getApplicationObjectFactory().getNewMailingXMLServicesObject("gov.fda.furls.mp.dbp.impl.DBPMailingXMLServicesImpl"));     
      else if ("ACCOUNTMANAGEMENT".equals(whichObject))
           return (getApplicationObjectFactory().getNewMailingXMLServicesObject("gov.fda.furls.mp.oaa.impl.OAAMailingXMLServicesImpl"));
      else      
          return null;
  }

  /**
   * The API getAddressObject() is used to create the address object
   * @param countryName - The country name.
   * @param addressTypeId - The address type identifier
   * @param name - The company name/address name
   * @param addressLine1 - The address line 1
   * @param addressLine2 - The address line 2
   * @param city - The city of the address
   * @param zipcode - The zip code of the address
   * @param stateOrProvince - The state name or province name in which address belongs
   * @param title - The title of the address
   * @param busPhCountryCode - The business phone country code of the address
   * @param busPhAreaCode - The business phone area code of the address
   * @param busPhNo - The business phone number of the address
   * @param busPhExtn - The business phone extension of the address
   * @param faxPhCountryCode - The fax phone country code of the address
   * @param faxPhAreaCode - The fax phone area code of the address
   * @param faxPhNo - The fax phone no of the address
   * @param email  - The email address of the person.
   * @return Address - The address object.
   */
  public static Address getAddressObject(String countryName,
                                         String addressTypeId,
                                         String name,
                                         String addressLine1,
                                         String addressLine2,
                                         String city,
                                         String zipcode,
                                         String stateOrProvince,
                                         String title,
                                         String busPhCountryCode,
                                         String busPhAreaCode,
                                         String busPhNo,
                                         String busPhExtn,
                                         String faxPhCountryCode,
                                         String faxPhAreaCode,
                                         String faxPhNo,
                                         String email)
  {
      Address address = new Address();
      address.setCountryName(countryName);
      address.setAddressTypeId(addressTypeId);
      address.setName(name);
      address.setAddressLine1(addressLine1);
      address.setAddressLine2(addressLine2);
      address.setCity(city);
      address.setZipCode(zipcode);
      address.setStateOrProvince(stateOrProvince);
      address.setTitile(title);
      address.setBusPhCountryCode(busPhCountryCode);
      address.setBusPhAreaCode(busPhAreaCode);
      address.setBusPhNo(busPhNo);
      address.setBusPhExtn(busPhExtn);
      address.setFaxPhCountryCode(faxPhCountryCode);
      address.setFaxPhAreaCode(faxPhAreaCode);
      address.setFaxPhNo(faxPhNo);
      address.setEmail(email);
      return (address);
  }
  
  /**
   * The private API getApplicationObjectFactory() is to get the 
   * object reference of IApplicationObjectFactory.
   * @return IApplicationObjectFactory
   */
  public static IApplicationObjectFactory getApplicationObjectFactory()
  {
     //Initializing the IApplicationObjectFactory Object
     return(iAppObjFactoryImpl == null ? new ApplicationObjectFactoryImpl() : iAppObjFactoryImpl);
  }
}
