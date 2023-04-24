package com.tcs.ain.mp.ffrm.impl;

//mp imports
import com.tcs.ain.mp.alert.IApplicationErrorAlertServices;
import com.tcs.ain.mp.ppf.jaxb.ObjectFactory;
import com.tcs.ain.mp.ppf.jaxb.Transaction;
import com.tcs.ain.mp.ppf.jaxb.TransactionType;
import com.tcs.ain.mp.IMailingXMLServices;
import com.tcs.ain.mp.helper.StringUtil;
//mp specific JAXB imports
import gov.fda.furls.mp.ppf.jaxb.*;

//java.io and util imports
import java.io.*;
import java.util.*;

//JAXB imports
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.*;

//Common Imports
import com.tcs.ain.mp.common.ExceptionConstants;
import com.tcs.ain.mp.common.MPMailingConstants;
import com.tcs.ain.mp.manager.ResourceManager;
import com.tcs.ain.mp.common.EmailContent;
import com.tcs.ain.mp.helper.Address;
import com.tcs.ain.mp.helper.MailLogVO;
import com.tcs.ain.mp.helper.CountryRate;
import java.util.StringTokenizer;
import com.tcs.ain.mp.common.Util;
import com.tcs.ain.mp.common.FatalException;
import com.tcs.ain.mp.common.XMLTransformException;
import com.tcs.ain.mp.common.ApplicationErrorAlertException;
import com.tcs.ain.mp.common.MailingException;
import com.tcs.ain.mp.impl.MailingXMLServices;
import com.tcs.ain.mp.factory.MessageProcessorFactory;

//Java Mail imports
import javax.mail.SendFailedException;
import javax.mail.MessagingException;


 /** Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * FFRMMailingXMLServicesImpl -  A  java implementation of the interface IMailingXMLServices
 * that implements all the mailing notifications for Food Facility Registration Module(FFRM)
 * 
 * @version 2.00 07 April 2009
 * @author Christy Mathew
 * 
 * @see IMailingXMLServices
 * @see MailingXMLServices
 * @see IApplicationErrorAlertServices
 * @see FatalException
 * @see XMLTransformException
 * @see ApplicationErrorAlertException
 * @see MailingException
*/

public class FFRMMailingXMLServicesImpl extends MailingXMLServices implements IMailingXMLServices, java.io.Serializable
{
  
  private static final String FACILITY_RECIPIENT = "F";
  private static final String USAGENT_RECIPIENT  = "U";
  private static final String PARENT_COMPANY_RECIPIENT = "C";
  private static final String PREFERRED_MAILING_RECIPIENT = "P";

 
  public FFRMMailingXMLServicesImpl() {}

  /**
   * The API processMessage() is used to process all the FFRM related Mailings.
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  public void processMessage(String message) 
      throws MailingException, FatalException
  {
    String errorMessage = null;
    try 
     {
           JAXBContext jaxbContext = JAXBContext.newInstance("gov.fda.furls.mp.ppf.jaxb",this.getClass().getClassLoader());
           Unmarshaller um = jaxbContext.createUnmarshaller();
       
           Transaction transaction = (Transaction)um.unmarshal( new StreamSource( new StringReader( message.toString() ) ) );
           TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();
           if ("FFRM".equals(controlAreaType.getInitiator()))
           {
               if ("Mailing Request".equals(controlAreaType.getVerb()))
               {
                     TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType = transaction.getDataArea().getRegistration().getMailingTypes();
               
                     if ("Initial Agent Return Receipt Pending".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 1
                         processInitAgentReturnReceiptPendingNotification(transaction, mailingType, message);
                     }
                     else if ("Initial Facility Return Receipt Pending".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 2
                         processInitFacReturnReceiptPendingNotification(transaction,mailingType, message);
                     }
                     else if ("Initial Agent Assignment Notification".equals(mailingType.getMailingTypeName()))
                     {
                          //Mailing Type - 3
                          processInitAgentAssignmentNotification(transaction,mailingType, message);
                     }
                     else if ("Initial Facility Assignment Notification".equals(mailingType.getMailingTypeName()))
                     {
                          //Mailing Type - 4
                          processInitFacAssignmentNotification(transaction,mailingType, message);
                     }
                     else if ("Registration Modified by FDA".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 5
                         processRegModifiedByFDANotification(transaction,mailingType, message);
                     }
                     else if ("Registration Cancelled".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 6
                         processRegCancelledNotification(transaction,mailingType, message);
                     }
                     else if ("Transfer of Ownership Notification".equals(mailingType.getMailingTypeName()))  
                     {
                        //Mailing Type - 7
                        processTransOfOwnerNotification(transaction,mailingType, message);
                     }
                     else if ("Facility Merger Notification".equals(mailingType.getMailingTypeName()))
                     {
                       //Mailing Type - 8
                       processFacMergerNotification(transaction,mailingType, message);
                     }
                     else if("Facility without US Agent".equals(mailingType.getMailingTypeName()) ) 
                     {
                       //Mailing Type - 9
                       facilityWithoutUSAgentNotification(transaction,mailingType,message);
                     }
                     else if ("Agent Re-Assignment".equals(mailingType.getMailingTypeName()))
                     {
                        //Mailing Type - 11
                        processAgentReAssignNotification(transaction,mailingType, message);
                     }
                     else if ("PIN Modification".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 12
                         processPinModificationNotification(transaction,mailingType, message);
                     }
                     else if ("Send Registration Preview".equals(mailingType.getMailingTypeName())) //This API is for Registration Preview
                     {
                        //Not a mailing type defined in the database. This API can be used to send the entire registration
                        //info to the customer at their request. Mainly to support online registration preview.
                        processRegPreviewNotification(transaction,mailingType, message);
                     }
               }
           }
     }
     catch (MailingException me)
     {
       //This is not a severe exception.
       me.printStackTrace();
       errorMessage = "ERROR: FFRMMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.MAILING_EXCEPTION;
       throw new MailingException(errorMessage);
     }
     catch(JAXBException jaxe) 
     {
        //This is a severe exception and the transaction should  roll back. The JMS provider should re-send the message.
        jaxe.printStackTrace();
        errorMessage = "FATAL ERROR: FFRMMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.JAXB_EXCEPTION;
        throw new FatalException(errorMessage);
     }  
     catch(FatalException fe) 
     {
        //This is a severe exception and the transaction should roll back. The JMS provider should re-send the message.
        fe.printStackTrace();
        errorMessage = "FATAL ERROR: FFRMMailingXMLServicesImpl.processMessage() "+Util.getDate(Util.getTimestamp(),3)+" "+fe.getMessage();
        throw new FatalException(errorMessage);
     }
  }

  /**
   * MAILING-TYPE - 1
   * The API processInitAgentReturnReceiptPendingNotification() is used to send Initial Agent Return Pending Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processInitAgentReturnReceiptPendingNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
      throws MailingException, FatalException
  {
      boolean isXMLsentToPPF = false;
      String facilityEmail  = null;
      TransactionType.DataAreaType.RegistrationType regType = null;
      TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
      Address address  = null;
      String facName = null;
      
      //When a registration's Pin is modified, Send out an email/letter to the Facility or Preferred Address of the facility.
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
             //Get the reference of Registration  Type
             regType = transaction.getDataArea().getRegistration();
             //Check for the facility addressType and the email id of the Facility.
             addressesType = transaction.getDataArea().getRegistration().getAddresses();
             TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
             ListIterator iter = addressesType.getAddress().listIterator();

             boolean isPreferredValid = CheckValidMailingAddress(addressesType, "P");
             
             for(Iterator it=iter; it.hasNext(); ) 
             {
                  // get the reference of Address object
                  addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                  if ("F".equals(addrType.getAddressType()))
                  {
                       facilityEmail = null;
                       if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();

                       //Extract the facility Address
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                     facName = addrType.getName();
                  }
                  if ("P".equals(addrType.getAddressType()) && isPreferredValid )
                  {
                      facilityEmail = null;
                      if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();
                      //Extract the preferred facility Address and re-assign the address object with the Preferred address if one exist.
                      address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "P",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                  }
             }//End of for loop
             if (facilityEmail != null)
             {
                   try
                   {
                      //Sending the HTML text to the recipient.  This new method getMsgBodyForInvalidRegistration() added by Naidu Sanapala
                     // sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),"Invalid Registration Notification",EmailContent.getMsgBodyForPinModification(regType.getRegNbr(),regType.getPin(),address, facName));
                      sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),"Invalid Registration Notification",EmailContent.getMsgBodyForInvalidRegistration(regType.getRegNbr()));
                 
                   }
                   catch (SendFailedException sfe) 
                   {
                      sfe.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (MessagingException mex)
                   {
                      mex.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (Exception e)
                   {
                      e.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   if (!isXMLsentToPPF){
                         //if there is no exceptions in the email,log the email message to PPF records management
                          addressesType = transaction.getDataArea().getRegistration().getAddresses();
                          boolean isValid = CheckValidMailingAddress(addressesType, "P");
                          String[] recipients = null;
                          if (isValid)
                          {
                                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                          }
                          else
                          {                                         
                                recipients = new String[] { FACILITY_RECIPIENT };
                          }
                          fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                      }
             }
             else //email is null.
             {
                  addressesType = transaction.getDataArea().getRegistration().getAddresses();
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  String[] recipients = null;
                  if (isValid)
                  {
                        recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                  }
                  else
                  {                                         
                        recipients = new String[] { FACILITY_RECIPIENT };
                  }
                 fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
       }
       else if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
          //Send the XML message to the MP_PPF queue for further processing by PPF
          addressesType = transaction.getDataArea().getRegistration().getAddresses();
          boolean isValid = CheckValidMailingAddress(addressesType, "P");
          String[] recipients = null;
          if (isValid)
          {
                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
          }
          else
          {                                         
                recipients = new String[] { FACILITY_RECIPIENT };
          }
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }         
  }



   /**
   * MAILING-TYPE - 2
   * The API processInitFacReturnReceiptPendingNotification() is used to send Initial Facility Return Pending Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processInitFacReturnReceiptPendingNotification(Transaction transaction,TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
    throws MailingException, FatalException
  {
       boolean isXMLsentToPPF = false;
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
          String facilityEmail = null;
          String facName = null;
          //Get the reference of Registration Type
          TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();
                
         //get the recipient 1
         if (StringUtil.toLowerCase(mailingType.getRecipient1()).equals("facility"))
         {
            //Get the reference of the registrationMailingsType
             TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
                       
            //Check for the Facility addressType and the physical address of the facility.
             TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = transaction.getDataArea().getRegistration().getAddresses();
             ListIterator iter = addressesType.getAddress().listIterator();
             for(Iterator it=iter; it.hasNext(); ) 
             {
                // get the reference of Address object
                TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                if ("F".equals(addrType.getAddressType()))
                {
                   if (addrType.getEmail() != null)
                   {
                      facilityEmail = addrType.getEmail();
                   }
                   if (addrType.getName() != null && !"".equals(addrType.getName()))
                   {
                      facName = addrType.getName();
                   }
                   break; 
                }
             }//end of for loop
             if (facilityEmail != null)
             {
                     //Call the customization layer using XSLT to convert the XML data elements into
                     //HTML format
                     String uniqueCode = Util.generateUniqueCode();
                     String fileName = uniqueCode+"_registration.html";
                     String success = null;

                     try{
                          success = getTransformXMLManager().transformXMLintoHTML(message,fileName);
                     }catch (XMLTransformException xtfe) {success="ERROR";}
                     
                     if ("SUCCESS".equals(success))
                     {
                         try
                         {
                             sendMailWithAttachment(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName()+" Notification",EmailContent.getEmailMessageBody(ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),getRegMailingId("FACILITY",regMailingsType),facName,regType.getRegNbr(),getReceiptCode("FACILITY",regMailingsType)),fileName);
                         }
                         catch (SendFailedException sfe) 
                         {
                              sfe.printStackTrace();
                              //remove the file just created
                              try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacReturnReceiptPendingNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }
                              String[] recipients = new String[] { FACILITY_RECIPIENT };
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          catch(MessagingException me)
                          {
                              me.printStackTrace();
                              //remove the file just created
                              try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacReturnReceiptPendingNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }
                              String[] recipients = new String[] { FACILITY_RECIPIENT };
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          catch (Exception me)
                          {
                              me.printStackTrace();
                              //remove the file just created
                              try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacReturnReceiptPendingNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }
                              String[] recipients = new String[] { FACILITY_RECIPIENT };
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          if (!isXMLsentToPPF){
                              //If there is no exceptions in email,to make it thread safe, remove the html file that was created as part of transformation from the hard disk.
                               try {
                                      getTransformXMLManager().removeFile(fileName);
                                }catch (XMLTransformException xtfe ){
                                    try{
                                      getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacReturnReceiptPendingNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                    }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                                }

                               //Log the email message to PPF records management after adding mail to address section in XML.
                                String[] recipients = new String[] { FACILITY_RECIPIENT };
                                fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                          }
                     }
                     else if ("ERROR".equals(success))
                     {
                          System.out.println(mailingType.getMailingTypeName()+" : An error occured while sending transforming XML-HTML and so sending the XML to PPF queue to send via paper");
                         //If the transformation didn't happen, send it to PPF to process via paper.
                         String[] recipients = new String[] { FACILITY_RECIPIENT };
                         fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                     }
                            
             }
             else //Send via paper if email is null
             {
               //System.out.println("MessageProcessor mailingType.getMailingTypeName() .. The email is null and sending the message to PPF");
               String[] recipients = new String[] { FACILITY_RECIPIENT };
               fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE); 
             }
         }
       }
       else
       {
           System.out.println(mailingType.getMailingTypeName()+" : Media Code is letter and so sending the XML to PPF queue to send via paper");
           String[] recipients = new String[] { FACILITY_RECIPIENT };
           fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }
    
  }

  /**
   * MAILING-TYPE - 3
   * The API processInitAgentAssignmentNotification() is used to send Initial Agent Assignment Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processInitAgentAssignmentNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
    throws MailingException, FatalException
  {
       boolean isXMLsentToPPF = false;
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
          //Get the reference of Registration  Type
          TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();
                    
          String usAgentEmail = null;
          String facName = null;
          String facAddress = null;
          Address address = null;
          //get the recipient 1
          if (StringUtil.toLowerCase(mailingType.getRecipient1()).equals("us agent"))
          {
             //Get the reference of registrationMailingsType
             TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
                      
             //Check for the US AGENT addressType and the email id of the us agent.
             TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = transaction.getDataArea().getRegistration().getAddresses();
             ListIterator iter = addressesType.getAddress().listIterator();
             for(Iterator it=iter; it.hasNext(); ) 
             {
                // get the reference of Address object
                TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                if ("F".equals(addrType.getAddressType()))
                {
                    if (addrType.getName() != null && !"".equals(addrType.getName()))
                    {
                       facName = addrType.getName();
                       facAddress = addrType.getAddressLine1()+" , "+(addrType.getAddressLine2() == null  || "".equals(addrType.getAddressLine2()) ? "" : addrType.getAddressLine2()+" , ")+addrType.getCity()+" , "+addrType.getStateProvince()+" , "+(addrType.getZipcode() == null || "none".equals(addrType.getZipcode()) ? "":addrType.getZipcode()+" , ")+addrType.getCountry();
                    }
                }
                if ("U".equals(addrType.getAddressType()))
                {
                   if (addrType.getEmail() != null)
                   {
                       usAgentEmail = addrType.getEmail();
                      //Extract the US Agent Address Information.
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "U",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          (addrType.getTitle() == null ? "" : addrType.getTitle()),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                   }
                    break;
                }
             }//End of for loop
             if (usAgentEmail != null)
             {
                      //Call the customization layer using XSLT to convert the XML data elements into
                      //HTML format
                     /* String uniqueCode = Util.generateUniqueCode();
                      String fileName = uniqueCode+"_registration.html";
                      String success = null;          
                     try{ 
                           success = getTransformXMLManager().transformXMLintoHTML(message,fileName);
                      }catch (XMLTransformException xtfe) {success="ERROR";}*/
                      
                      //if ("SUCCESS".equals(success))
                      //{
                          try
                          {
                             System.out.println("Sending Email To The Address :"+usAgentEmail);
                             //sendMailWithAttachment(usAgentEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),EmailContent.getEmailMessageBody(ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),getRegMailingId("USAGENT",regMailingsType),facName,regType.getRegNbr(),getReceiptCode("USAGENT",regMailingsType)),fileName);
                             sendHTMLMail(usAgentEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),EmailContent.getMsgBodyForInitialAgentAssignNotification(address,facName,facAddress,getReceiptCode("USAGENT",regMailingsType),transaction.getDataArea().getRegistration().getRegNbr()));
                          }
                          catch (SendFailedException sfe) 
                          {
                              sfe.printStackTrace();
                              //remove the file just created
                              /*try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitAgentAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*///}
                              //}
                              String[] recipients = new String[] { USAGENT_RECIPIENT };
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          catch(MessagingException me)
                          {
                              me.printStackTrace();
                              //remove the file just created
                              /*try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitAgentAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*///}
                              //}
                              String[] recipients = new String[] { USAGENT_RECIPIENT };
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          catch (Exception me)
                          {
                              me.printStackTrace();
                              //remove the file just created
                              /*try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitAgentAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*///}
                              //}
                              String[] recipients = new String[] { USAGENT_RECIPIENT };
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          if (!isXMLsentToPPF){
                              //If there is no exceptions in the email,to make it thread safe, 
                              //remove the html file that was created as part of transformation from the hard disk.
                               /*try {
                                      getTransformXMLManager().removeFile(fileName);
                                }catch (XMLTransformException xtfe ){
                                    try{
                                      getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitAgentAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                    }catch (ApplicationErrorAlertException ae){/*No action is required here.*///}
                                //}
                                       
                               //Log the email message to PPF records management after adding mail to address section in XML.
                               String[] recipients = new String[] { USAGENT_RECIPIENT };
                               fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                          }
                      //}
                      /*else if ("ERROR".equals(success))
                      {
                            System.out.println(mailingType.getMailingTypeName()+" : An error occured while sending transforming XML-HTML and so sending the XML to PPF queue to send via paper");
                            //If the transformation didn't happen, send it to PPF to process via paper.
                             String[] recipients = new String[] { USAGENT_RECIPIENT };
                             fillMailToAddressAndNotifyPPF(message,recipients, false, true);
                      }*/
             }
             else //Send via paper if email is null
             {
                  System.out.println(mailingType.getMailingTypeName()+" : The email is null and so sending the XML to PPF queue to send via paper");
                  String[] recipients = new String[] { USAGENT_RECIPIENT };
                  fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
          }
       }
       else if(StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
         System.out.println(mailingType.getMailingTypeName()+" : Media Code is letter and so sending the XML to PPF queue to send via paper");
         //Send the XML message to the MP_PPF queue for further processing by PPF
          String[] recipients = new String[] { USAGENT_RECIPIENT };
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }
  }

  /**
   * MAILING-TYPE - 4
   * The API processInitFacAssignmentNotification() is used to send Initial Facility Assignment Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processInitFacAssignmentNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
     throws MailingException, FatalException
  {
       boolean isXMLsentToPPF = false;
       TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
       Address address = null;
       //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
          //Get the reference of Registration  Type
          TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();
                    
          String facilityEmail = null;
          String facName = null;
           //get the recipient 1
          if (StringUtil.toLowerCase(mailingType.getRecipient1()).equals("facility"))
          {
             //Get the reference of registrationMailingsType
             TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
                       
             //Check for the facility addressType and the email id of the Facility.
             addressesType = transaction.getDataArea().getRegistration().getAddresses();
             ListIterator iter = addressesType.getAddress().listIterator();
             //Check whether Preferred Address is valid/present.
             System.out.println(" Coming here 1 " + addressesType );
             boolean isPreferredValid = CheckValidMailingAddress(addressesType, "P");
             System.out.println(" Coming here 2 " + isPreferredValid );
             for(Iterator it=iter; it.hasNext(); ) 
             {
                // get the reference of Address object
                TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                          
                if (isPreferredValid) //If preferred Mailing address is valid.
                {
                    if ("P".equals(addrType.getAddressType())) 
                    {
                        //Check for Preferred Email also in this case.
                       if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                       {
                          facilityEmail = addrType.getEmail(); //Preferred Email.
                       }
                    }
                    if ("F".equals(addrType.getAddressType()))
                    {
                        /*if (addrType.getName() != null && !"".equals(addrType.getName()))
                        {
                          facName = addrType.getName();  //Facility Name.
                        }*/
                       //Extract the Facility Address Information.
                       System.out.println(" Coming here 3 " + addrType.getAddressType());
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          addrType.getAddressLine2(),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                    }
                }
                else //If the Preferred Address is not present.
                {
                    if ("F".equals(addrType.getAddressType()))
                    {
                       if (addrType.getEmail() != null) //Check whether Facility has email address.
                       {
                          facilityEmail =  addrType.getEmail();  //Facility Email.
                       }
                       /*if (addrType.getName() != null && !"".equals(addrType.getName()))
                       {
                          facName = addrType.getName(); //Facility Name.
                       }*/
                       //Extract the Facility Address Information.
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          addrType.getAddressLine2(),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                    }
                }                          
             }//End of for loop
             if (facilityEmail != null)
             {
                    
                    //Call the customization layer using XSLT to convert the XML data elements into
                    //HTML format
                    System.out.println(" Coming here 4 " );
                    String uniqueCode = Util.generateUniqueCode();
                    String fileName = uniqueCode+"_registration.html";
                    String success = null;          
                    try{
                          success = getTransformXMLManager().transformXMLintoHTML(message,fileName);
                     }catch (XMLTransformException xtfe) {success="ERROR";}
                     
                    if ("SUCCESS".equals(success))
                    {
                        try
                        {
                           sendMailWithAttachment(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),EmailContent.getMsgBodyForInitialFacAssignNotification(address,transaction.getDataArea().getRegistration().getRegNbr(),transaction.getDataArea().getRegistration().getPin()),fileName);
                        }
                        catch (SendFailedException sfe) 
                        {
                            sfe.printStackTrace();
                            //remove the file just created
                            try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                            }
                            boolean isValid = CheckValidMailingAddress(addressesType, "P");
                            String[] recipients = null;
                            if (isValid)
                            {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                            }
                            else
                            {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                            }
                            //String[] recipients = new String[] { FACILITY_RECIPIENT };
                            //send the mailing request to PPF.
                            fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                            isXMLsentToPPF = true;
                        }
                        catch(MessagingException me)
                        {
                            me.printStackTrace();
                            //remove the file just created
                            try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                            }
                            boolean isValid = CheckValidMailingAddress(addressesType, "P");
                            String[] recipients = null;
                            if (isValid)
                            {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                            }
                            else
                            {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                            }
                            //String[] recipients = new String[] { FACILITY_RECIPIENT };
                            //send the mailing request to PPF.
                            fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                            isXMLsentToPPF = true;
                        }
                        catch (Exception me)
                        {
                            me.printStackTrace();
                            //remove the file just created.
                            try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                            }
                            boolean isValid = CheckValidMailingAddress(addressesType, "P");
                            String[] recipients = null;
                            if (isValid)
                            {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                            }
                            else
                            {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                            }
                            //String[] recipients = new String[] { FACILITY_RECIPIENT };
                            //send the mailing request to PPF.
                            fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                            isXMLsentToPPF = true;
                        }
                        if (!isXMLsentToPPF) {
                            //If there is no exceptions in the email,to make it thread safe, remove the html file that 
                            //was created as part of transformation from the hard disk.
                             try {
                                      getTransformXMLManager().removeFile(fileName);
                                }catch (XMLTransformException xtfe ){
                                    try{
                                      getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processInitFacAssignmentNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                    }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                                }

                             //Log the email message to PPF records management after adding mail to address section in the XML.
                             //First check whether the Preferred Address is Valid. 
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              String[] recipients = null;
                              if (isValid)
                              {
                                    recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                    recipients = new String[] { FACILITY_RECIPIENT };
                              }
                              //String[] recipients = new String[] { FACILITY_RECIPIENT };
                              fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                        }
                                  
                    }
                    else if ("ERROR".equals(success))
                    {
                        System.out.println(mailingType.getMailingTypeName()+" : An error occurred while transforming XML-HTML and so sending the XML to PPF queue to send via paper");
                        //If the transformation didn't happen, send it to PPF to process via paper.
                        boolean isValid = CheckValidMailingAddress(addressesType, "P");
                        String[] recipients = null;
                        if (isValid)
                        {
                              recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                        }
                        else
                        {
                              recipients = new String[] { FACILITY_RECIPIENT };
                        }
                        //String[] recipients = new String[] { FACILITY_RECIPIENT };
                        fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                    }
             }
             else //Send via paper if email is null
             {
                 System.out.println(mailingType.getMailingTypeName()+" : The email is null and so sending the XML to PPF queue to send via paper");
                 boolean isValid = CheckValidMailingAddress(addressesType, "P");
                 String[] recipients = null;
                 if (isValid)
                 {
                      recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                 }
                 else
                 {
                      recipients = new String[] { FACILITY_RECIPIENT };
                 }
                 fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
          }
       }
       else if(StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
          System.out.println(mailingType.getMailingTypeName()+" : Media Code is letter and so sending the XML to PPF queue to send via paper");
          //Send the XML message to the MP_PPF queue for further processing by PPF
          String[] recipients = null;
          addressesType = transaction.getDataArea().getRegistration().getAddresses();
          boolean isValid = CheckValidMailingAddress(addressesType, "P");
          if (isValid)
          {
              recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
          }
          else
          {
              recipients = new String[] { FACILITY_RECIPIENT };
          }
          //String[] recipients = new String[] { FACILITY_RECIPIENT };
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }    
  }

  /**
   * MAILING-TYPE - 5
   * The API processRegModifiedByFDANotification() is used to send Registration Modified By FDA Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processRegModifiedByFDANotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
      throws MailingException,FatalException
  {
      boolean isXMLsentToPPF = false;
      //This email/letter has to go to US Agent(if foreign) and Facility
      TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
            //Get the reference of registrationMailingsType
            TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
                       
            String facilityLocation = null;
            String facilityEmail = null;
            String usAgentEmail  = null;
            String email = null;
            Address address = null;
                     
            //Check if the facility is foreign or domestic
            TransactionType.DataAreaType.RegistrationType registrationType = transaction.getDataArea().getRegistration();
            //Check for the facility addressType and the email id of the Facility.
            addressesType = transaction.getDataArea().getRegistration().getAddresses();
            TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
            ListIterator iter = addressesType.getAddress().listIterator();
            if ("I".equals(registrationType.getRegLocationType()))
            {
                  facilityLocation = "FOREIGN";
                  for(Iterator it=iter; it.hasNext(); ) 
                  {
                        // get the reference of Address object
                        addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                        if ("F".equals(addrType.getAddressType()))
                        {
                            if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                                 facilityEmail = addrType.getEmail();

                            //Extract the facility Address Information.
                            address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          addrType.getAddressLine2(),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                        }
                        else if ("P".equals(addrType.getAddressType()))
                        {
                            //Check for Preferred Email also in this case.
                           if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                           {
                              facilityEmail = addrType.getEmail();
                           }
                        }
                        else if ("U".equals(addrType.getAddressType()))
                        {
                             if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                                 usAgentEmail = addrType.getEmail();
                        }
                  }//End of for loop
            }
            else if ("D".equals(registrationType.getRegLocationType()))
            {
                 facilityLocation = "DOMESTIC";
                 for(Iterator it=iter; it.hasNext(); ) 
                  {
                        // get the reference of Address object
                        addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                        if ("F".equals(addrType.getAddressType()))
                        {
                            if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                                 facilityEmail = addrType.getEmail();

                            //Extract the facility Address Information.
                            address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                        }
                        else if ("P".equals(addrType.getAddressType())) 
                        {
                            //Check for Preferred Email also in this case.
                           if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                           {
                              facilityEmail = addrType.getEmail();
                           }
                        }
                  }//End of for loop
            }
                     
            //Check whether US Agent and Facility Email address are not null
            if ("FOREIGN".equals(facilityLocation) && facilityEmail != null && usAgentEmail != null)
            {
                  //Send the email to both facility and us agent.
                  email = facilityEmail+","+usAgentEmail;
            }
            else if ("FOREIGN".equals(facilityLocation) && facilityEmail == null && usAgentEmail != null)
            {
                  email = usAgentEmail;
            }
            else if ("FOREIGN".equals(facilityLocation) && facilityEmail != null && usAgentEmail == null)
            {
                  email = facilityEmail;
            }
            else if ("DOMESTIC".equals(facilityLocation) && facilityEmail != null)
            {
                  email = facilityEmail;
            }

            //Check whether US Agent and Facility Email address are not null
            if (email != null)
            {
                  try
                  {
                    if ("Registration Modified by FDA".equals(mailingType.getMailingTypeName()))
                    {
                       //Sending the email to the recipient with HTML TEXT as the body.
                       sendHTMLMail(email, ResourceManager.getInstance().getFromAddress(), mailingType.getMailingTypeName()+" Notification", EmailContent.getMsgBodyForRegnModifiedByFDA(address));
                    }else if ("Registration Cancelled".equals(mailingType.getMailingTypeName()))
                    {
                       if (facilityEmail != null && !"".equals(facilityEmail))
                       {
                          //Sending the facilityEmail to the recipient with HTML TEXT as the body.
                          //Facility get to see the registration number
                          sendHTMLMail(facilityEmail, ResourceManager.getInstance().getFromAddress(), mailingType.getMailingTypeName()+" Notification", EmailContent.getMsgBodyForRegnCancelled(address,registrationType.getRegNbr(),registrationType.getLastUpdateDate(),false));
                       }
                       if (usAgentEmail != null && !"".equals(usAgentEmail))
                       {
                         //Sending the usAgentEmail to the recipient with HTML TEXT as the body.
                         //Us Agent does not get to see the registration number.
                         sendHTMLMail(usAgentEmail, ResourceManager.getInstance().getFromAddress(), mailingType.getMailingTypeName()+" Notification", EmailContent.getMsgBodyForRegnCancelled(address,registrationType.getRegNbr(),registrationType.getLastUpdateDate(),true));
                       }
                    }                     
                  }
                  catch (SendFailedException sfe) 
                  {
                      sfe.printStackTrace();
                      String[] recipients = null;
                      if ("I".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                      {
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              if (isValid)
                              {
                                recipients = new String[] { USAGENT_RECIPIENT,PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                 recipients = new String[] { USAGENT_RECIPIENT,FACILITY_RECIPIENT };
                              }
                      }
                      else if ("D".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                      {
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              if (isValid)
                              {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                              }
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                  }
                  catch(MessagingException me)
                  {
                      me.printStackTrace();
                      String[] recipients = null;
                      if ("I".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                      {
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              if (isValid)
                              {
                                recipients = new String[] { USAGENT_RECIPIENT,PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                 recipients = new String[] { USAGENT_RECIPIENT,FACILITY_RECIPIENT };
                              }
                      }
                      else if ("D".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                      {
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              if (isValid)
                              {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                              }
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                  }
                  catch (Exception me)
                  {
                      me.printStackTrace();
                      String[] recipients = null;
                      if ("I".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                      {
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              if (isValid)
                              {
                                recipients = new String[] { USAGENT_RECIPIENT,PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                 recipients = new String[] { USAGENT_RECIPIENT,FACILITY_RECIPIENT };
                              }
                      }
                      else if ("D".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                      {
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              if (isValid)
                              {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                              }
                              else
                              {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                              }
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;                      
                  }
                  if (!isXMLsentToPPF){
                        //If there is no exception, send the log to PPF after adding mail to address section in XML.
                        System.out.println(mailingType.getMailingTypeName()+" : Sending the XML message to PPF for logging and Records Management purpose");
                        //Log the email message anway (if atleast an email is sent to even one address) to PPF records management
                         String[] recipients = null;
                         if ("I".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                         {
                                boolean isValid = CheckValidMailingAddress(addressesType, "P");
                                if (isValid)
                                {
                                  recipients = new String[] { USAGENT_RECIPIENT,PREFERRED_MAILING_RECIPIENT };
                                }
                                else
                                {
                                   recipients = new String[] { USAGENT_RECIPIENT,FACILITY_RECIPIENT };
                                }
                         }
                         else if ("D".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                         {
                                boolean isValid = CheckValidMailingAddress(addressesType, "P");
                                if (isValid)
                                {
                                    recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                                }
                                else
                                {
                                    recipients = new String[] { FACILITY_RECIPIENT };
                                }
                         }
                         fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                  }
            }
            else //email is null , so send to PPF to send via paper.
            {
                     System.out.println(mailingType.getMailingTypeName()+" : The email is null and so sending the XML to PPF queue to send via paper");
                     String[] recipients = null;
                     if ("I".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                     {
                            boolean isValid = CheckValidMailingAddress(addressesType, "P");
                            if (isValid)
                            {
                                recipients = new String[] { USAGENT_RECIPIENT,PREFERRED_MAILING_RECIPIENT };
                            }
                            else
                            {
                                 recipients = new String[] { USAGENT_RECIPIENT,FACILITY_RECIPIENT };
                            }
                     }
                     else if ("D".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
                     {
                             boolean isValid = CheckValidMailingAddress(addressesType, "P");
                             if (isValid)
                             {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                             }
                             else
                             {
                                  recipients = new String[] { FACILITY_RECIPIENT };
                             }
                     }
                     fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
            }
       }
       else if(StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
           System.out.println(mailingType.getMailingTypeName()+" : Media Code is letter and so sending the XML to PPF queue to send via paper");
           //Send the XML message to the MP_PPF queue for further processing by PPF
           String[] recipients = null;
           addressesType = transaction.getDataArea().getRegistration().getAddresses();
           if ("I".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
           {      
                             
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  if (isValid)
                  {
                      recipients = new String[] { USAGENT_RECIPIENT,PREFERRED_MAILING_RECIPIENT };
                  }
                  else
                  {
                       recipients = new String[] { USAGENT_RECIPIENT,FACILITY_RECIPIENT };
                  }
           }
           else if ("D".equals(transaction.getDataArea().getRegistration().getRegLocationType()))
           {
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  if (isValid)
                  {
                        recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                  }
                  else
                  {
                        recipients = new String[] { FACILITY_RECIPIENT };
                  }
           }
           fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }
  }

  /**
   * MAILING-TYPE - 6
   * The API processRegCancelledNotification() is used to send Registration Cancelled Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processRegCancelledNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
      throws MailingException, FatalException
  {
       processRegModifiedByFDANotification(transaction,mailingType,message);
  }

  /**
   * MAILING-TYPE - 7
   * The API processTransOfOwnerNotification() is used to send Transfer Of Ownership Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */  
  private void processTransOfOwnerNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
      throws MailingException,FatalException
  {
       boolean isXMLsentToPPF = false;
       String facilityEmail = null;
       Address address      = null;
      //If there is a Transfer of Ownership,an email/letter has to go to the new owner.
       //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
            //Get the reference of Registration  Type
             TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();

            //Get the reference of registrationMailingsType
            TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
                      
           //Check for the facility addressType and the email id of the Facility.
             TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = transaction.getDataArea().getRegistration().getAddresses();
             ListIterator iter = addressesType.getAddress().listIterator();
                       
             //Get the email recipient of this mailing type
             facilityEmail = getEmailAddress(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
             if ("null".equals(facilityEmail) || "".equals(facilityEmail))
                facilityEmail = null;
                       
             for(Iterator it=iter; it.hasNext(); ) 
             {
                  // get the reference of Address object
                  TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                           
                  if ("F".equals(addrType.getAddressType()))
                  {
                             if (facilityEmail == null) //Both Parent Company and Preferred emails are null
                             {
                                if (addrType.getEmail() != null)
                                  facilityEmail = addrType.getEmail(); //Get the Facility Email Address.
                             }
                            //Extract the facility Address
                            address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          addrType.getAddressLine2(),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                         break;
                  }
             }//End of for loop
                       
             if (facilityEmail != null)
             {
                  try
                  {

                        if ("Transfer of Ownership Notification".equals(mailingType.getMailingTypeName()))
                             sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),EmailContent.getMsgBodyForTransferOfOwn(ResourceManager.getInstance().getFromAddress(),address,getRegMailingId("FACILITY",regMailingsType)));
                        else if ("Facility Merger Notification".equals(mailingType.getMailingTypeName()))
                             sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),EmailContent.getMsgBodyForFacMerger(address,regType.getStatusReason()));
                  }
                  catch (SendFailedException sfe) 
                  {
                      sfe.printStackTrace();
                      String[] recipients = getRecipients(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                  }
                  catch (MessagingException me)
                  {
                      me.printStackTrace();
                      String[] recipients = getRecipients(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                  }
                  catch (Exception e)
                  {
                      e.printStackTrace();
                      String[] recipients = getRecipients(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                  }
                  if (!isXMLsentToPPF){
                     //if there is no exceptions in the email, Log the email message to PPF records management
                     String[] recipients = getRecipients(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
                     fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                  }
             }
             else //Send via paper if email is null
             {
                  System.out.println(mailingType.getMailingTypeName()+" : The email is null and so sending the XML to PPF queue to send via paper");
                  String[] recipients = getRecipients(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
                  fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
       }
       else if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
           System.out.println(mailingType.getMailingTypeName()+" : Media Code is letter and so sending the XML to PPF queue to send via paper");
           //Send the XML message to the MP_PPF queue for further processing by PPF
           String[] recipients = getRecipients(mailingType.getMailingTypeName(),transaction.getDataArea().getRegistration().getAddresses());
           fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }
  }

  /** MAILING-TYPE - 8
   * The API processFacMergerNotification() is used to send the Facility Merger Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   * */
  private void processFacMergerNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
     throws MailingException,FatalException
  {
      processTransOfOwnerNotification(transaction, mailingType, message);
  }

   /**
   * MAILING-TYPE - 9
   * The API facilityWithoutUSAgentNotification() is used to send facility with No U.S Agent Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void facilityWithoutUSAgentNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
     throws MailingException, FatalException
  {
      boolean isXMLsentToPPF = false;
      String facilityEmail  = null;
      TransactionType.DataAreaType.RegistrationType regType = null;
      TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
      Address address  = null;
      String facName = null;
      String getUSAgentName = null;
      
      //When u.s agent rejects the assignment from the facility,
      //Send out an email/letter to the  Preferred Address of the facility.
      // If preferred not there send it out to the facility
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
             //Get the reference of Registration  Type
             regType = transaction.getDataArea().getRegistration();
             //Check for the facility addressType and the email id of the Facility.
             addressesType = transaction.getDataArea().getRegistration().getAddresses();
             TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
             ListIterator iter = addressesType.getAddress().listIterator();
             
             boolean isPreferredValid = CheckValidMailingAddress(addressesType, "P");
             
             //System.out.println("the size is " + addressesType.getAddress().size() );
             for(Iterator it=iter; it.hasNext(); ) 
             {
                  // get the reference of Address object
                  addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                  if("U".equals(addrType.getAddressType() ) ) 
                  {
                    getUSAgentName = addrType.getName();
                  }
                  if ("F".equals(addrType.getAddressType()))
                  {
                       facilityEmail = null;
                       if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();

                       //Extract the facility Address
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                     facName = addrType.getName();
                  }
                  if ("P".equals(addrType.getAddressType()) && isPreferredValid )
                  {
                      facilityEmail = null;
                      if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();
                      //Extract the preferred facility Address and re-assign the address object with the Preferred address if one exist.
                      address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "P",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                  }
             }//End of for loop
             if (facilityEmail != null)
             {
                   try
                   {
                      //Sending the HTML text to the recipient.
                     // sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName()+" Notification",EmailContent.getMsgBodyForPinModification(regType.getRegNbr(),regType.getPin(),address, facName));
                      System.out.println("COMING HERE TO SEND THE EMAIL" + regType.getRegNbr() );
                      sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName(),EmailContent.getMsgBodyForFacWithoutUsAgent(regType.getRegNbr(),facName,getUSAgentName,address));

                   }
                   catch (SendFailedException sfe) 
                   {
                      sfe.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (MessagingException mex)
                   {
                      mex.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (Exception e)
                   {
                      e.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   if (!isXMLsentToPPF){
                         //if there is no exceptions in the email,log the email message to PPF records management
                          addressesType = transaction.getDataArea().getRegistration().getAddresses();
                          boolean isValid = CheckValidMailingAddress(addressesType, "P");
                          String[] recipients = null;
                          if (isValid)
                          {
                                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                          }
                          else
                          {                                         
                                recipients = new String[] { FACILITY_RECIPIENT };
                          }
                          fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                      }
             }
             else //email is null.
             {
                  addressesType = transaction.getDataArea().getRegistration().getAddresses();
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  String[] recipients = null;
                  if (isValid)
                  {
                        recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                  }
                  else
                  {                                         
                        recipients = new String[] { FACILITY_RECIPIENT };
                  }
                 fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
       }
       else if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
          //Send the XML message to the MP_PPF queue for further processing by PPF
          addressesType = transaction.getDataArea().getRegistration().getAddresses();
          boolean isValid = CheckValidMailingAddress(addressesType, "P");
          String[] recipients = null;
          if (isValid)
          {
                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
          }
          else
          {                                         
                recipients = new String[] { FACILITY_RECIPIENT };
          }
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }         

  }


  /** 
  /**
   * MAILING-TYPE - 11
   * The API processAgentReAssignNotification() is used to send the Agent Re-Assignment Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
  private void processAgentReAssignNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
      throws MailingException,FatalException
  {
      boolean isXMLsentToPPF = false;
      String email = null;
      String facilityEmail = null;
      String usAgentEmail = null;
      Address address     = null;
      ArrayList addressList = new ArrayList();

      TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
      //This mailing is sent out to Facility and US Agent as well.
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
             //Get the reference of Registration  Type
             TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();

              //Get the reference of registrationMailingsType
            TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
                      
            //Check for the facility addressType and the email id of the Facility.
             addressesType = transaction.getDataArea().getRegistration().getAddresses();
             ListIterator iter = addressesType.getAddress().listIterator();
             for(Iterator it=iter; it.hasNext(); ) 
             {
                  // get the reference of Address object
                  TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                  if ("F".equals(addrType.getAddressType()))
                  {
                         if (addrType.getEmail() != null)
                         { 
                            facilityEmail = addrType.getEmail();
                         }
                         //Extract the facility Address
                         address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          addrType.getAddressLine2(),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                          addressList.add(address);
                  }
                  else if ("P".equals(addrType.getAddressType())) 
                  {
                        //Check if Preferred Email address is filled in or not.
                        if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                           facilityEmail = addrType.getEmail();
                  }
                  else if ("U".equals(addrType.getAddressType()))
                  {
                          if (addrType.getEmail() != null)
                          { 
                                usAgentEmail = addrType.getEmail();
                          }
                          //Extract the US AGENT Address
                          address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "U",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          addrType.getAddressLine2(),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                          addressList.add(address);
                  }
                            
             }//End of for loop.
             if (facilityEmail != null && usAgentEmail != null)
               email = facilityEmail+"-FACILITY"+","+ usAgentEmail+"-USAGENT";
             if (facilityEmail != null && usAgentEmail == null)
             {
                 email = facilityEmail+"-FACILITY";
             }
             if (facilityEmail == null &&  usAgentEmail != null)
             {
                 email = usAgentEmail+"-USAGENT";
             }

             if (email != null)
             {
                     //System.out.println("Sending Email To The Address :"+addrType.getEmail());
                      StringTokenizer st = new StringTokenizer(email,",");

                      while (st.hasMoreTokens())
                      {
                          email = st.nextToken();
                          String recipientType = email.substring(email.indexOf("-")+1);
                          email = email.substring(0,email.indexOf("-"));
                                    
                          System.out.println("Extracting email one by one :"+email);
                          try
                          {
                              sendHTMLMail(email,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName()+" Notification",EmailContent.getMsgBodyForAgenReassign(ResourceManager.getInstance().getFromAddress(),addressList,getRegMailingId(recipientType,regMailingsType),regType.getRegNbr()));
                          }
                          catch (SendFailedException sfe)
                          {
                              sfe.printStackTrace();
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              String[] recipients = null;
                              if (isValid)
                              {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT, USAGENT_RECIPIENT };
                              }
                              else
                              {
                                  recipients = new String[] { FACILITY_RECIPIENT, USAGENT_RECIPIENT };
                              }
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          catch (MessagingException me)
                          {
                              me.printStackTrace();
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              String[] recipients = null;
                              if (isValid)
                              {
                                    recipients = new String[] { PREFERRED_MAILING_RECIPIENT, USAGENT_RECIPIENT };
                              }
                              else
                              {                                         
                                    recipients = new String[] { FACILITY_RECIPIENT, USAGENT_RECIPIENT };
                              }
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                          catch (Exception e)
                          {
                              e.printStackTrace();
                              boolean isValid = CheckValidMailingAddress(addressesType, "P");
                              String[] recipients = null;
                              if (isValid)
                              {
                                  recipients = new String[] { PREFERRED_MAILING_RECIPIENT, USAGENT_RECIPIENT };
                              }
                              else
                              {                                         
                                  recipients = new String[] { FACILITY_RECIPIENT, USAGENT_RECIPIENT };
                              }
                              //send the mailing request to PPF.
                              fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                              isXMLsentToPPF = true;
                          }
                      }
                      if (!isXMLsentToPPF){        
                         //if there is no exceptions in the email,log the email message to PPF records management
                          boolean isValid = CheckValidMailingAddress(addressesType, "P");
                          String[] recipients = null;
                          if (isValid)
                          {
                                recipients = new String[] { PREFERRED_MAILING_RECIPIENT, USAGENT_RECIPIENT };
                          }
                          else
                          {                                         
                                recipients = new String[] { FACILITY_RECIPIENT, USAGENT_RECIPIENT };
                          }
                          fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                      }
             }
             else 
             {
                  System.out.println(mailingType.getMailingTypeName()+ " : The email is null and so sending the XML to PPF queue to send via paper");
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  String[] recipients = null;
                  if (isValid)
                  {
                        recipients = new String[] { PREFERRED_MAILING_RECIPIENT, USAGENT_RECIPIENT };
                  }
                  else
                  {                                         
                        recipients = new String[] { FACILITY_RECIPIENT, USAGENT_RECIPIENT };
                  }
                 fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
       }
       else if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
          System.out.println(mailingType.getMailingTypeName()+" : Media Code is letter and so sending the XML to PPF queue to send via paper");
          //Send the XML message to the MP_PPF queue for further processing by PPF
          addressesType = transaction.getDataArea().getRegistration().getAddresses();
          boolean isValid = CheckValidMailingAddress(addressesType, "P");
          String[] recipients = null;
          if (isValid)
          {
                recipients = new String[] { PREFERRED_MAILING_RECIPIENT, USAGENT_RECIPIENT };
          }
          else
          {                                         
                recipients = new String[] { FACILITY_RECIPIENT, USAGENT_RECIPIENT };
          }
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }
  }

  /**
   * MAILING-TYPE - 12
   * The API processPinModificationNotification() is used to send the Pin Modification Notification.
   * @param transaction - The root element of the XML.
   * @param mailingType - The mailing type object of the XML.
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException.
   */
  private void processPinModificationNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
     throws MailingException, FatalException
  {
      boolean isXMLsentToPPF = false;
      String facilityEmail  = null;
      TransactionType.DataAreaType.RegistrationType regType = null;
      TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
      Address address  = null;
      String facName = null;
      
      //When a registration's Pin is modified, Send out an email/letter to the Facility or Preferred Address of the facility.
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
             //Get the reference of Registration  Type
             regType = transaction.getDataArea().getRegistration();
             //Check for the facility addressType and the email id of the Facility.
             addressesType = transaction.getDataArea().getRegistration().getAddresses();
             TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
             ListIterator iter = addressesType.getAddress().listIterator();

             boolean isPreferredValid = CheckValidMailingAddress(addressesType, "P");
             
             for(Iterator it=iter; it.hasNext(); ) 
             {
                  // get the reference of Address object
                  addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                  if ("F".equals(addrType.getAddressType()))
                  {
                       facilityEmail = null;
                       if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();

                       //Extract the facility Address
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                     facName = addrType.getName();
                  }
                  if ("P".equals(addrType.getAddressType()) && isPreferredValid )
                  {
                      facilityEmail = null;
                      if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();
                      //Extract the preferred facility Address and re-assign the address object with the Preferred address if one exist.
                      address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "P",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                  }
             }//End of for loop
             if (facilityEmail != null)
             {
                   try
                   {
                      //Sending the HTML text to the recipient.
                      sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName()+" Notification",EmailContent.getMsgBodyForPinModification(regType.getRegNbr(),regType.getPin(),address, facName));
                   }
                   catch (SendFailedException sfe) 
                   {
                      sfe.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (MessagingException mex)
                   {
                      mex.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (Exception e)
                   {
                      e.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   if (!isXMLsentToPPF){
                         //if there is no exceptions in the email,log the email message to PPF records management
                          addressesType = transaction.getDataArea().getRegistration().getAddresses();
                          boolean isValid = CheckValidMailingAddress(addressesType, "P");
                          String[] recipients = null;
                          if (isValid)
                          {
                                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                          }
                          else
                          {                                         
                                recipients = new String[] { FACILITY_RECIPIENT };
                          }
                          fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                      }
             }
             else //email is null.
             {
                  addressesType = transaction.getDataArea().getRegistration().getAddresses();
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  String[] recipients = null;
                  if (isValid)
                  {
                        recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                  }
                  else
                  {                                         
                        recipients = new String[] { FACILITY_RECIPIENT };
                  }
                 fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
       }
       else if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
          //Send the XML message to the MP_PPF queue for further processing by PPF
          addressesType = transaction.getDataArea().getRegistration().getAddresses();
          boolean isValid = CheckValidMailingAddress(addressesType, "P");
          String[] recipients = null;
          if (isValid)
          {
                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
          }
          else
          {                                         
                recipients = new String[] { FACILITY_RECIPIENT };
          }
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }         
  }


      /**
   * MAILING-TYPE - 15
   * The API InvalidRegistrationNotification() is used to send facility with No U.S Agent Notification.
   * @param transaction - The root element of the XML
   * @param mailingType - The mailing type object of the XML
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException
   */
    private void inValidRegistrationNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
     throws MailingException, FatalException
  {
      boolean isXMLsentToPPF = false;
      String facilityEmail  = null;
      TransactionType.DataAreaType.RegistrationType regType = null;
      TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = null;
      Address address  = null;
      String facName = null;
      
      //When a registration's Pin is modified, Send out an email/letter to the Facility or Preferred Address of the facility.
      //Get the media code
       if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("email"))
       {
             //Get the reference of Registration  Type
             regType = transaction.getDataArea().getRegistration();
             //Check for the facility addressType and the email id of the Facility.
             addressesType = transaction.getDataArea().getRegistration().getAddresses();
             TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
             ListIterator iter = addressesType.getAddress().listIterator();

             boolean isPreferredValid = CheckValidMailingAddress(addressesType, "P");
             
             for(Iterator it=iter; it.hasNext(); ) 
             {
                  // get the reference of Address object
                  addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                  if ("F".equals(addrType.getAddressType()))
                  {
                       facilityEmail = null;
                       if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();

                       //Extract the facility Address
                       address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "F",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                     facName = addrType.getName();
                  }
                  if ("P".equals(addrType.getAddressType()) && isPreferredValid )
                  {
                      facilityEmail = null;
                      if (addrType.getEmail() != null && !"".equals(addrType.getEmail()))
                          facilityEmail = addrType.getEmail().trim();
                      //Extract the preferred facility Address and re-assign the address object with the Preferred address if one exist.
                      address = MessageProcessorFactory.getAddressObject(addrType.getCountry(),
                                                                          "P",
                                                                          addrType.getName(),
                                                                          addrType.getAddressLine1(),
                                                                          (addrType.getAddressLine2() == null ? "" : addrType.getAddressLine2()),
                                                                          addrType.getCity(),
                                                                          addrType.getZipcode(),
                                                                          addrType.getStateProvince(),
                                                                          addrType.getTitle(),
                                                                          addrType.getBusPhoneCountryCode(),
                                                                          addrType.getBusPhoneAreaCode(),
                                                                          addrType.getBusPhoneNum(),
                                                                          addrType.getBusPhoneExtn(),
                                                                          addrType.getFaxCountryCode(),
                                                                          addrType.getFaxAreaCode(),
                                                                          addrType.getFaxNum(),
                                                                          (addrType.getEmail() == null ? "" : addrType.getEmail())
                                                                          );
                  }
             }//End of for loop
             if (facilityEmail != null)
             {
                   try
                   {
                      //Sending the HTML text to the recipient.
                      sendHTMLMail(facilityEmail,ResourceManager.getInstance().getFromAddress(),mailingType.getMailingTypeName()+" Notification",EmailContent.getMsgBodyForPinModification(regType.getRegNbr(),regType.getPin(),address, facName));
                   }
                   catch (SendFailedException sfe) 
                   {
                      sfe.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (MessagingException mex)
                   {
                      mex.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   catch (Exception e)
                   {
                      e.printStackTrace();
                      addressesType = transaction.getDataArea().getRegistration().getAddresses();
                      boolean isValid = CheckValidMailingAddress(addressesType, "P");
                      String[] recipients = null;
                      if (isValid)
                      {
                          recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                      }
                      else
                      {
                          recipients = new String[] { FACILITY_RECIPIENT };
                      }
                      //send the mailing request to PPF.
                      fillMailToAddressAndNotifyPPF(message,recipients, false, true,MPMailingConstants.PAPER_CODE);
                      isXMLsentToPPF = true;
                   }
                   if (!isXMLsentToPPF){
                         //if there is no exceptions in the email,log the email message to PPF records management
                          addressesType = transaction.getDataArea().getRegistration().getAddresses();
                          boolean isValid = CheckValidMailingAddress(addressesType, "P");
                          String[] recipients = null;
                          if (isValid)
                          {
                                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                          }
                          else
                          {                                         
                                recipients = new String[] { FACILITY_RECIPIENT };
                          }
                          fillMailToAddressAndNotifyPPF(message,recipients, true, false,MPMailingConstants.EMAIL_CODE);
                      }
             }
             else //email is null.
             {
                  addressesType = transaction.getDataArea().getRegistration().getAddresses();
                  boolean isValid = CheckValidMailingAddress(addressesType, "P");
                  String[] recipients = null;
                  if (isValid)
                  {
                        recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
                  }
                  else
                  {                                         
                        recipients = new String[] { FACILITY_RECIPIENT };
                  }
                 fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
             }
       }
       else if (StringUtil.toLowerCase(mailingType.getMediaCode()).equals("letter"))
       {
          //Send the XML message to the MP_PPF queue for further processing by PPF
          addressesType = transaction.getDataArea().getRegistration().getAddresses();
          boolean isValid = CheckValidMailingAddress(addressesType, "P");
          String[] recipients = null;
          if (isValid)
          {
                recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
          }
          else
          {                                         
                recipients = new String[] { FACILITY_RECIPIENT };
          }
          fillMailToAddressAndNotifyPPF(message,recipients, false, false,MPMailingConstants.PAPER_CODE);
       }         
  }



  /**
   * The API processRegPreviewNotification() is used to notify the customers about the registration information
   * as per customer's request.
   * @param transaction - The root elemenet of the XML.
   * @param mailingType - The mailing type object of the XML.
   * @param message - The XML message.
   * @throws MailingException
   * @throws FatalException.
   */
  private void processRegPreviewNotification(Transaction transaction, TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType, String message)
       throws MailingException, FatalException
  {
        boolean isXMLsentToPPF = false;
        //Get the reference of the Account Information
        TransactionType.DataAreaType.RegistrationType.AccountRegistrationLinkType accountType  = transaction.getDataArea().getRegistration().getAccountRegistrationLink();

        if (accountType.getAccountEmail() != null)
        {
                String uniqueCode = Util.generateUniqueCode();
                String fileName = uniqueCode+"_registration.html";
                String success = null;                
                try{
                      success = getTransformXMLManager().transformXMLintoHTML(message,fileName);                    
                }catch (XMLTransformException xtfe) {success="ERROR";}
                
                if ("SUCCESS".equals(success))
                {
                      try
                      {
                          sendMailWithAttachment(accountType.getAccountEmail(),ResourceManager.getInstance().getFromAddress(),"Facility Registration Information",EmailContent.getMsgBodyForRegistrationPreview(),fileName);
                      }
                      catch (SendFailedException sfe) 
                      {
                          sfe.printStackTrace();
                          //remove the file just created
                          try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processRegPreviewNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }
                          //send the mailing request to PPF.    
                          fillMailToSubAccountAddressAndNotifyPPF(message,false,true);
                          isXMLsentToPPF = true;
                      }
                      catch(MessagingException me)
                      {
                          me.printStackTrace();
                          //remove the file just created
                          try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processRegPreviewNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }
                          //send the mailing request to PPF.    
                          fillMailToSubAccountAddressAndNotifyPPF(message,false,true);
                          isXMLsentToPPF = true;
                      }
                      catch (Exception me)
                      {
                          me.printStackTrace();
                          //remove the file just created
                          try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processRegPreviewNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }
                          //send the mailing request to PPF.    
                          fillMailToSubAccountAddressAndNotifyPPF(message,false,true);
                          isXMLsentToPPF = true;
                      }
                      if (!isXMLsentToPPF){
                          //If there is no exceptions in the email,to make it thread safe, remove the html file that 
                          //was created as part of transformation from the hard disk.
                          try {
                                    getTransformXMLManager().removeFile(fileName);
                              }catch (XMLTransformException xtfe ){
                                  try{
                                    getAppAlertServicesObject().sendErrorAlert("ERROR: FFRMMailingXMLServicesImpl.processRegPreviewNotification() "+Util.getDate(Util.getTimestamp().toString(),3)+" "+ExceptionConstants.FILE_REMOVAL_EXCEPTION +":"+fileName,false);
                                  }catch (ApplicationErrorAlertException ae){/*No action is required here.*/}
                              }

                          //Log this message to PPF.
                          fillMailToSubAccountAddressAndNotifyPPF(message,true,false);
                      }
                }
                else if ("ERROR".equals(success))
                {
                    System.out.println(mailingType.getMailingTypeName()+" : An error occured while sending transforming XML-HTML and so sending the XML to MP_PPF queue to send via paper");
                   //If the transformation didn't happen, send it to PPF to process via paper.
                    fillMailToSubAccountAddressAndNotifyPPF(message,false,true);                                  
                }
        }
        else //If the email is null.
        {
             //Send to PPF.
              //System.out.println(mailingType.getMailingTypeName()+" : The email is null and so sending the XML to MP_PPF queue to send via paper");
              fillMailToSubAccountAddressAndNotifyPPF(message,false,false);
        }
  }
  
  /**
   * The API getRecipients() to collect the recipient list for some mailing types.
   * @param mailingTypeName - mailing type name
   * @param addressesType - Collection of address objects
   * @return email address.
   */
   private static String getEmailAddress(String mailingTypeName,TransactionType.DataAreaType.RegistrationType.AddressesType addressesType)
   {
        String email = null;
        String pc = null;
        String pm = null;
              
        if ("Transfer of Ownership Notification".equals(mailingTypeName) || "Facility Merger Notification".equals(mailingTypeName))
        {
            ListIterator iter = addressesType.getAddress().listIterator();
            for(Iterator it=iter; it.hasNext(); ) 
            {
                // get the reference of Address object
                TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                if ("P".equals(addrType.getAddressType())) //Try the Preferred Address
                {
                      if (!"".equals(addrType.getEmail()) || addrType.getEmail() != null)
                         pm = addrType.getEmail();
                }
                if ("C".equals(addrType.getAddressType()))
                {
                     if (!"".equals(addrType.getEmail()) || addrType.getEmail() != null)
                     {
                         pc = addrType.getEmail();
                         break;
                     }
                }
            }//end of for loop

            if (pc != null)
              email = pc;
            else if (pc == null & pm != null)
              email = pm;
            else if (pc == null && pm == null)
              email = "null";
        }

      return email;
   }
  /**
   * The API CheckValidMailingAddress() is used to verify whether a particular recipients address
   * is valid or not
   * @param addressesType - The address object from the XML message
   * @param addressTypeId - The Address Type Id passed.
   * @return boolean      - A True or False.
   */
   private static boolean CheckValidMailingAddress(TransactionType.DataAreaType.RegistrationType.AddressesType addressesType, String addressTypeId)
   {
         boolean isValid = false;
       
         ListIterator iter = addressesType.getAddress().listIterator();
         for(Iterator it=iter; it.hasNext(); ) 
         {
                // get the reference of Address object
                TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                if (addressTypeId.equals(addrType.getAddressType())) //Try the Preferred Address
                {
                     if ( addrType.getName() != null && addrType.getAddressLine1() != null && addrType.getCity() != null  && addrType.getStateProvince() != null && addrType.getCountry() != null)
                     {
                          if (!"".equals(addrType.getName()) && !"".equals(addrType.getAddressLine1()) && !"".equals(addrType.getCity()) && !"".equals(addrType.getStateProvince()) && !"".equals(addrType.getZipcode()) && !"".equals(addrType.getCountry()))
                          {
                               isValid = true;
                          }
                     }
                     break;
                }
         }
         return isValid;
   }
  /**
   * The API getRecipients() to collect the recipient list for some mailing types.
   * @param mailingTypeName - mailing type name
   * @param addressesType - Collection of address objects
   */
   private static String[] getRecipients(String mailingTypeName,TransactionType.DataAreaType.RegistrationType.AddressesType addressesType)
   {
        String[] recipients = null;
        String pc = null;
        String pm = null;
        //String fa = null;
       
        if ("Transfer of Ownership Notification".equals(mailingTypeName) || "Facility Merger Notification".equals(mailingTypeName))
        {
            ListIterator iter = addressesType.getAddress().listIterator();
            for(Iterator it=iter; it.hasNext(); ) 
            {
                // get the reference of Address object
                TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                if ("P".equals(addrType.getAddressType())) //Try the Preferred Address
                {
                      if (!"".equals(addrType.getName()) && !"".equals(addrType.getAddressLine1()) && !"".equals(addrType.getCity()) && !"".equals(addrType.getStateProvince()) && !"".equals(addrType.getCountry()))
                         pm = PREFERRED_MAILING_RECIPIENT;
                }
                if ("C".equals(addrType.getAddressType()))
                {
                     if (!"".equals(addrType.getName()) && !"".equals(addrType.getAddressLine1()) && !"".equals(addrType.getCity()) && !"".equals(addrType.getStateProvince()) &&  !"".equals(addrType.getCountry()))
                     {
                         pc = PARENT_COMPANY_RECIPIENT;
                         break;
                     }
                }
            }//end of for loop
            
            if (pc != null)
            {
                 recipients = new String[] { PARENT_COMPANY_RECIPIENT };
            }
            else if (pc == null && pm != null) //if Parent Company address is null, try the preferred mailing address
            {
                 recipients = new String[] { PREFERRED_MAILING_RECIPIENT };
            }
            else if (pc == null && pm == null) //If both Parent Company and Preferred are null, the recipient should be Facility.
            {
                 recipients = new String[] { FACILITY_RECIPIENT };
            }                
        }

      return recipients;
   }

   /**
   * The API  fillMailToSubAccountAddressAndNotifyPPF() is to fill the Mail To Sub Account Address element for Paper Processing Facility.
   * @param message - The original XML message
   * @param isAddLog - The Value that determine whether the message should add a log.
   * @param isAddExceptions - The value that determine whether the message should add exceptions.
   * @throws FatalException.
   * 
   */
   private void fillMailToSubAccountAddressAndNotifyPPF(String message, boolean isAddLog, boolean isAddExceptions)
     throws FatalException
   {
       ArrayList mailToAddressList = new ArrayList();

       try
       {
           // create a JAXBContext
           JAXBContext jaxbContext = JAXBContext.newInstance("gov.fda.furls.mp.ppf.jaxb",this.getClass().getClassLoader());

           // create an ObjectFactory instance.
           // if the JAXBContext had been created with mutiple pacakge names,
           // we would have to explicitly use the correct package name when
           // creating the ObjectFactory.            
           ObjectFactory objFactory = new ObjectFactory();
          
           // create an unmarshaller
           Unmarshaller um = jaxbContext.createUnmarshaller();

           Transaction transaction = (Transaction)um.unmarshal( new StreamSource( new StringReader( message.toString() ) ) );

           TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();
           //Check for the facility addressType and the email id of the Facility.
           TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = transaction.getDataArea().getRegistration().getAddresses();
           // declare the addrType
           TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
           ListIterator iter = addressesType.getAddress().listIterator();
           
           for(Iterator it=iter; it.hasNext(); ) 
           {
               // get the reference of Address object
               addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
               if (addrType.getAddressType().equals("SA"))
               {
                       mailToAddressList.add(addrType);
               }
           }
            TransactionType.DataAreaType.RegistrationType.MailToAddressesType.MailToAddressType mailToAddrType = null;
            if (mailToAddressList != null)
            {
                //Create and empty Registration Mailing Type object
                 TransactionType.DataAreaType.RegistrationType.MailToAddressesType mailToAddressesType  = objFactory.createTransactionTypeDataAreaTypeRegistrationTypeMailToAddressesType();

                  //Get a reference to the Mail To Address List
                  List mailAddressList =  mailToAddressesType.getMailToAddress(); 
                  for(Iterator it=mailToAddressList.iterator(); it.hasNext(); ) 
                  {
                       addrType =  (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)it.next();
                       
                       //Create and empty MailToAddress object
                       mailToAddrType  = objFactory.createTransactionTypeDataAreaTypeRegistrationTypeMailToAddressesTypeMailToAddressType();
                       mailToAddrType.setAddressType(addrType.getAddressType());                       
                       mailToAddrType.setName(addrType.getName());
                       mailToAddrType.setAddressLine1(addrType.getAddressLine1());
                       mailToAddrType.setAddressLine2(addrType.getAddressLine2());
                       mailToAddrType.setCity(addrType.getCity());
                       mailToAddrType.setStateProvince(addrType.getStateProvince());
                       mailToAddrType.setZipcode(addrType.getZipcode());
                       mailToAddrType.setCountry(addrType.getCountry());
                       mailToAddrType.setIsoCountryCode(addrType.getIsoCountryCode());
                       mailToAddrType.setTitle(addrType.getTitle());
                       mailToAddrType.setBusPhoneCountryCode(addrType.getBusPhoneCountryCode());
                       mailToAddrType.setBusPhoneAreaCode(addrType.getBusPhoneAreaCode());
                       mailToAddrType.setBusPhoneNum(addrType.getBusPhoneNum());
                       mailToAddrType.setBusPhoneExtn(addrType.getBusPhoneExtn());
                       mailToAddrType.setFaxCountryCode(addrType.getFaxCountryCode());
                       mailToAddrType.setFaxNum(addrType.getFaxNum());
                       mailToAddrType.setHomePhoneCountryCode(addrType.getHomePhoneCountryCode());
                       mailToAddrType.setHomePhoneAreaCode(addrType.getHomePhoneAreaCode());
                       mailToAddrType.setHomePhoneNum(addrType.getHomePhoneNum());
                       mailToAddrType.setEmail(addrType.getEmail());

                       mailAddressList.add(mailToAddrType);
                  }
                   //Set the required Mail To Address Type to the Registration Type.
                  regType.setMailToAddresses(mailToAddressesType);
            }
            System.out.println("Adding the Mail To Address element to the original XML message to PPF queue :"); 
             
             if (isAddLog)
             {
                  TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();

                 if ("Mailing Request".equals(controlAreaType.getVerb()) || "Send Email".equals(controlAreaType.getVerb()) || "Email Request From Registration Preview".equals(controlAreaType.getVerb()))
                 {
                    //Change the verb to Mailing Request Email Log
                    controlAreaType.setVerb("Mailing Request Email Log");
                 }
                 System.out.println("Adding the Mail To Address element and sending the log to the original XML message to PPF queue"); 
             }
             if (isAddExceptions)
             {
                //Get the Data Area Type
                TransactionType.DataAreaType dataAreaType = transaction.getDataArea();

                //Get the reference of the Exception type
                TransactionType.DataAreaType.ExceptionsType exceptionsType = transaction.getDataArea().getExceptions();
                  
                TransactionType.DataAreaType.ExceptionsType.ExceptionType exceptionType = null;
                //Get the reference of the list of Exception Type
                ListIterator iter1 = exceptionsType.getException().listIterator();
               
                for(Iterator it=iter1; it.hasNext(); ) 
                {
                      exceptionType = (TransactionType.DataAreaType.ExceptionsType.ExceptionType)it.next();
                      exceptionType.setCode(ExceptionConstants.EMAIL_ERROR_CODE);
                      exceptionType.setDescription(ExceptionConstants.EMAIL_ERROR_DESC);
                }

                System.out.println("Adding the Mail To Address element and Exception Message to the original XML message to PPF queue"); 
             }
             //create a Marshaller and marshal to the StreamResult
              Marshaller m = jaxbContext.createMarshaller();
              m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
              StringWriter sw = new StringWriter();
              StreamResult result = new StreamResult(sw);
              m.marshal(transaction, result );

              System.out.println(sw.toString());
              
               //Send the XML to the message Queue.
              notifyPaperProcess(sw.toString());
       }
       catch (FatalException fe)
       {
          if (!isAddLog) //If it not a log, then this message is a mailing request. So the trans'n should be rollbacked.
              throw new FatalException("FFRMMailingXMLServicesImpl.fillMailToSubAccountAddressAndNotifyPPF() "+fe.getMessage());
          else //If it is not a mailing request and then the exception is not severe.
             System.out.println("FFRMMailingXMLServicesImpl.fillMailToSubAccountAddressAndNotifyPPF()..Unable to notify the PPF about the mailing request log");
       }
       catch( JAXBException je )
       {
          je.printStackTrace();
          if (!isAddLog) //If it is not a log, then this message is a mailing request. So the trans'n should be rollbacked.
              throw new FatalException("FFRMMailingXMLServicesImpl.fillMailToSubAccountAddressAndNotifyPPF() "+ExceptionConstants.JAXB_EXCEPTION);
          else //If it is not a mailing request and then the exception is not severe.
             System.out.println("FFRMMailingXMLServicesImpl.fillMailToSubAccountAddressAndNotifyPPF()..A JAXBException Exception occurred and unable to notify the PPF about the mailing request log");
       }
       catch(Exception e)
       {
          if (!isAddLog) //If it is not a log, then this message is a mailing request. So the trans'n should roll back.
              throw new FatalException("FFRMMailingXMLServicesImpl.fillMailToSubAccountAddressAndNotifyPPF() "+ExceptionConstants.JAXB_EXCEPTION);
          else //If it is not a mailing request and then the exception is not severe.
             System.out.println("FFRMMailingXMLServicesImpl.fillMailToSubAccountAddressAndNotifyPPF()..A JAXBException Exception occurred and unable to notify the PPF about the mailing request log");
       }
   }
   
  /**
   * The API  mailToAddress is to fill the fillMailToAddressAndNotifyPPF element for Paper Processing Facility.
   * @param message - The original XML message
   * @param recipients - The String Array contains the recipients.
   * @param isAddLog   - This determines whether the "Log" should be added to the verb.
   * @param isAddExceptions - This determines whether the exception block has to be filled in.
   * @throws MailingException
   * @throws FatalException
   */
   private void fillMailToAddressAndNotifyPPF(String message,String[] recipients, boolean isAddLog, boolean isAddExceptions,String mediaCode)
      throws MailingException, FatalException
   {
       ArrayList mailToAddressList = new ArrayList();
       String facilityReceiptCode ="";
       String usAgentReceiptCode ="";
       /*
        * Added to get the variables for inserting into the mailing log file
        */
       String regnbr = null;
       String mailing_type = null;
       String mailing_type_name = null;
       String date = null;
       
       
       try
       {
           // create a JAXBContext
           JAXBContext jaxbContext = JAXBContext.newInstance("gov.fda.furls.mp.ppf.jaxb",this.getClass().getClassLoader());

          // create an ObjectFactory instance.
          // if the JAXBContext had been created with mutiple pacakge names,
          // we would have to explicitly use the correct package name when
          // creating the ObjectFactory.            
          ObjectFactory objFactory = new ObjectFactory();
          
           // create an unmarshaller
           Unmarshaller um = jaxbContext.createUnmarshaller();

           Transaction transaction = (Transaction)um.unmarshal( new StreamSource( new StringReader( message.toString() ) ) );

           // added for XSL-FO mailing type 9 and 12
           TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();
           boolean rate_flag = false;
           if ("FFRM".equals(controlAreaType.getInitiator()))
           {
               if ("Mailing Request".equals(controlAreaType.getVerb()))
               {
                     TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType = transaction.getDataArea().getRegistration().getMailingTypes();
               
                     if ("Initial Agent Return Receipt Pending".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 1
                         rate_flag = true;
                         
                     }
                     else if ("Initial Facility Return Receipt Pending".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 2
                        
                     }
                     else if ("Initial Agent Assignment Notification".equals(mailingType.getMailingTypeName()))
                     {
                          //Mailing Type - 3
                          
                     }
                     else if ("Initial Facility Assignment Notification".equals(mailingType.getMailingTypeName()))
                     {
                          //Mailing Type - 4
                          
                     }
                     else if ("Registration Modified by FDA".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 5
                         
                     }
                     else if ("Registration Cancelled".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 6
                         
                     }
                     else if ("Transfer of Ownership Notification".equals(mailingType.getMailingTypeName()))  
                     {
                        //Mailing Type - 7
                        
                     }
                     else if ("Facility Merger Notification".equals(mailingType.getMailingTypeName()))
                     {
                       //Mailing Type - 8
                      
                     }
                     else if("Facility without US Agent".equals(mailingType.getMailingTypeName()) ) 
                     {
                       //Mailing Type - 9
                       rate_flag = true;
                     }
                     else if ("Agent Re-Assignment".equals(mailingType.getMailingTypeName()))
                     {
                        //Mailing Type - 11
                        
                     }
                     else if ("PIN Modification".equals(mailingType.getMailingTypeName()))
                     {
                         //Mailing Type - 12
                         rate_flag = true;
                     }
                     else if ("Send Registration Preview".equals(mailingType.getMailingTypeName())) //This API is for Registration Preview
                     {
                        //Not a mailing type defined in the database. This API can be used to send the entire registration
                        //info to the customer at their request. Mainly to support online registration preview.
                       
                     }
               }
           }

           //Get the Registration Type Object
           TransactionType.DataAreaType.RegistrationType regType = transaction.getDataArea().getRegistration();
           /*
            * Get the Registration no from the Registration Type Object.
            */
            MailLogVO maillogvo = new MailLogVO();
            
            regnbr = regType.getRegNbr();
            
            System.out.println("The Registration Number is " +  regnbr );
           // Get the Registration Mailing Object
           TransactionType.DataAreaType.RegistrationType.MailingTypesType mailingType = transaction.getDataArea().getRegistration().getMailingTypes();
           date = mailingType.getMailingDate();
           mailing_type = mailingType.getMailingTypeId();
           mailing_type_name = mailingType.getMailingTypeName();
         //Get the Registration Mailing Type
           TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType = transaction.getDataArea().getRegistration().getMailingTypes().getRegistrationMailings();
           
           //Check for the facility addressType and the email id of the Facility.
           TransactionType.DataAreaType.RegistrationType.AddressesType addressesType = transaction.getDataArea().getRegistration().getAddresses();
           // declare the addrType
           TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType addrType = null;
           ListIterator iter = addressesType.getAddress().listIterator();
           
            System.out.println("The recipients length :"+recipients.length);
            for (int i=0; i < recipients.length; i++)
            {
                if ("F".equals(recipients[i]) || "C".equals(recipients[i]) || "P".equals(recipients[i]))
                {
                   facilityReceiptCode = getReceiptCode("FACILITY", regMailingsType);
                }
                else if ("U".equals(recipients[i]))
                {
                   usAgentReceiptCode = getReceiptCode("USAGENT", regMailingsType);
                }                
            }
            System.out.println("facilityReceiptCode"+ facilityReceiptCode);
             System.out.println("usAgentReceiptCode"+ usAgentReceiptCode);
            for(Iterator it=iter; it.hasNext(); ) 
            {
                // get the reference of Address object
                addrType = (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)iter.next();
                for (int i=0; i < recipients.length; i++)
                {
                    if (addrType.getAddressType().equals(recipients[i]))
                    {
                         System.out.println("Recipient is :"+recipients[i]);
                         mailToAddressList.add(addrType);
                         //break;
                    }
                }
             }
            TransactionType.DataAreaType.RegistrationType.MailToAddressesType.MailToAddressType mailToAddrType = null;
            if (mailToAddressList != null)
            {
                //Create and empty Registration Mailing Typ object
                 TransactionType.DataAreaType.RegistrationType.MailToAddressesType mailToAddressesType  = objFactory.createTransactionTypeDataAreaTypeRegistrationTypeMailToAddressesType();

                  //Get a reference to the Mail To Address List
                  List mailAddressList =  mailToAddressesType.getMailToAddress(); 
                  for(Iterator it=mailToAddressList.iterator(); it.hasNext(); ) 
                  {
                       addrType =  (TransactionType.DataAreaType.RegistrationType.AddressesType.AddressType)it.next();
                       
                       //Create and empty MailToAddress object
                       mailToAddrType  = objFactory.createTransactionTypeDataAreaTypeRegistrationTypeMailToAddressesTypeMailToAddressType();
                       mailToAddrType.setAddressType(addrType.getAddressType());                       
                       mailToAddrType.setName(addrType.getName());
                       mailToAddrType.setAddressLine1(addrType.getAddressLine1());
                       mailToAddrType.setAddressLine2(addrType.getAddressLine2());
                       mailToAddrType.setCity(addrType.getCity());
                       mailToAddrType.setStateProvince(addrType.getStateProvince());
                       if (addrType.getZipcode() != null)
                       mailToAddrType.setZipcode(addrType.getZipcode());
                       mailToAddrType.setCountry(addrType.getCountry());
                       mailToAddrType.setIsoCountryCode(addrType.getIsoCountryCode());
                       mailToAddrType.setTitle(addrType.getTitle());
                       mailToAddrType.setBusPhoneCountryCode(addrType.getBusPhoneCountryCode());
                       mailToAddrType.setBusPhoneAreaCode(addrType.getBusPhoneAreaCode());
                       mailToAddrType.setBusPhoneNum(addrType.getBusPhoneNum());
                       mailToAddrType.setBusPhoneExtn(addrType.getBusPhoneExtn());
                       mailToAddrType.setFaxCountryCode(addrType.getFaxCountryCode());
                       mailToAddrType.setFaxNum(addrType.getFaxNum());
                       mailToAddrType.setHomePhoneCountryCode(addrType.getHomePhoneCountryCode());
                       mailToAddrType.setHomePhoneAreaCode(addrType.getHomePhoneAreaCode());
                       mailToAddrType.setHomePhoneNum(addrType.getHomePhoneNum());
                       mailToAddrType.setEmail(addrType.getEmail());

                       System.out.println("the rate flag is " + rate_flag ); 
                       if(rate_flag)

                       {
                         CountryRate rate = new CountryRate();
                         System.out.println("the country is " + addrType.getIsoCountryCode() );
                         mailToAddrType.setRate(rate.getRate(addrType.getIsoCountryCode() ));
                         
                       }
                         System.out.println("the rate is " + mailToAddrType.getRate() );
                         System.out.println("usAgentReceiptCode before setting"+ usAgentReceiptCode);
                         System.out.println("facilityReceiptCode before setting"+ facilityReceiptCode);
                         System.out.println("addrType.getAddressType() before setting"+ addrType.getAddressType());
  
                       if ("F".equals(addrType.getAddressType()) || "C".equals(addrType.getAddressType()) || "P".equals(addrType.getAddressType()))














                           mailToAddrType.setReceiptCode(facilityReceiptCode);  
                       else if ("U".equals(addrType.getAddressType()))
                           mailToAddrType.setReceiptCode(usAgentReceiptCode);  
                       else    
                           mailToAddrType.setReceiptCode("");  
                           
                       mailAddressList.add(mailToAddrType);
                  }
                   //Set the required Mail To Address Type to the Registration Type.
                  regType.setMailToAddresses(mailToAddressesType);
            }
             System.out.println("Adding the Mail To Address element to the original XML message to PPF queue :"); 
             
             if (isAddLog)
             {
                 // TransactionType.ControlAreaType controlAreaType = transaction.getControlArea();

                 if ("Mailing Request".equals(controlAreaType.getVerb()) || "Send Email".equals(controlAreaType.getVerb()))
                 {
                    //Change the verb to Mailing Request Email Log
                    controlAreaType.setVerb("Mailing Request Email Log");
                 }
                 System.out.println("Adding the Mail To Address element and sending the log to the original XML message to PPF queue"); 
             }
             if (isAddExceptions)
             {
                //Get the Data Area Type
                TransactionType.DataAreaType dataAreaType = transaction.getDataArea();

                //Get the reference of the Exception type
                TransactionType.DataAreaType.ExceptionsType exceptionsType = transaction.getDataArea().getExceptions();
                  
                TransactionType.DataAreaType.ExceptionsType.ExceptionType exceptionType = null;
                //Get the reference of the list of Exception Type
                if (exceptionsType != null)
                {
                   ListIterator iter1 = exceptionsType.getException().listIterator();               
                   for(Iterator it=iter1; it.hasNext(); ) 
                   {
                        exceptionType = (TransactionType.DataAreaType.ExceptionsType.ExceptionType)it.next();
                        exceptionType.setCode(ExceptionConstants.EMAIL_ERROR_CODE);
                        exceptionType.setDescription(ExceptionConstants.EMAIL_ERROR_DESC);
                   }
                   System.out.println("Adding the Mail To Address element and Exception Message to the original XML message to PPF queue"); 
                }
             }
             //create a Marshaller and marshal to the StreamResult
              Marshaller m = jaxbContext.createMarshaller();
              //m.setProperty(Marshaller.JAXB_ENCODING,"iso-8859-1");
              m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
              StringWriter sw = new StringWriter();
              StreamResult result = new StreamResult(sw);
              m.marshal(transaction, result );

              System.out.println(sw.toString());
              if(MPMailingConstants.MAILING_TYPE_9.equals(mailing_type) || MPMailingConstants.MAILING_TYPE_12.equals(mailing_type ) ||  MPMailingConstants.MAILING_TYPE_1.equals(mailing_type ) ) {
              //Send the XML to the message Queue to print the PDF.
                  if("Mailing Request".equals(controlAreaType.getVerb()) ) {
                  notifyPaperProcessNewMT(sw.toString());
                  }
                  //add the values to the Mail Log VO
                  maillogvo.setRegnbr(regnbr);
                  maillogvo.setMailingtypeid(mailing_type);
                  maillogvo.setMailingtypename(mailing_type_name);
                  maillogvo.setCreateddate(date);
                  maillogvo.setControlverb(controlAreaType.getVerb() );
                  maillogvo.setXmlobject(sw.toString() );
                  System.out.println("COMING HERE BEFORE INSERTING INTO MAILING LOG TABLE ");
                  logXMLFile(maillogvo);
              } else {
                   notifyPaperProcess(sw.toString());
              }
           /*  
            * Add logic to Update the Mailing Table with the media code.
            * 
            * */
              try 
              {
                //String mailingdate =  
               // String receiptcode = mailingType.getr
                 // List ls =  regMailingsType.getRegistrationMailing();
                   if ("F".equals(addrType.getAddressType()) || "C".equals(addrType.getAddressType()) || "P".equals(addrType.getAddressType()) )
                        //   mailToAddrType.setReceiptCode(facilityReceiptCode); 
                             maillogvo.setReceiptcode(facilityReceiptCode);
                       else if ("U".equals(addrType.getAddressType()))
                        //   mailToAddrType.setReceiptCode(usAgentReceiptCode);
                             maillogvo.setReceiptcode(usAgentReceiptCode);
                       else    
                        //   mailToAddrType.setReceiptCode(""); 
                             maillogvo.setReceiptcode("");
                  maillogvo.setRegnbr(regnbr);
                  maillogvo.setMailingtypeid(mailing_type);
                  maillogvo.setMediacode(mediaCode);
                  updateRegistrationMailing(maillogvo);
              }catch(Exception e) 
              {
                /* Error report */
                System.out.println("Error in updating the mailings table ");
              }
              
       }
       catch (FatalException fe)
       {
          if (!isAddLog) //If it is a Mailing Request, the exception is severe and the trans'n should be rolled back.
             throw new FatalException("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF() "+fe.getMessage());
          else //If it is a mailing log , then the exception is not severe.
             System.out.println("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF()..Unable to notify PPF about the mailing request log");
       }
       catch( JAXBException je )
       {
          je.printStackTrace();
          if (!isAddLog) //If it is a Mailing Request, the exception is severe and the trans'n should be rolled back.
             throw new FatalException("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF() "+ExceptionConstants.JAXB_EXCEPTION);
          else //If it is a mailing log , then the exception is not severe.
             System.out.println("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF()..Unable to notify PPF about the mailing request log");
       }
       catch (MailingException me)
       {
          //This exception is not severe.
          me.printStackTrace();
          throw new MailingException("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF()..A Mailing Exception occurred");
       }
       catch(Exception e)
       {
           if (!isAddLog) //If it is not a log, then this message is a mailing request. So the trans'n should  roll back.
              throw new FatalException("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF() "+ExceptionConstants.JAXB_EXCEPTION);
          else //If it is not a mailing request, then the exception is not severe.
             System.out.println("FFRMMailingXMLServicesImpl.fillMailToAddressAndNotifyPPF()..An Exception occurred and unable to notify the PPF about the mailing request log");
       }
   }
  
  /**
   * The API getRegMailingId is used to extract the registrationMailingId for the recipientType
   * @param recipientType - The recipientType e.g., FACILITY or USAGENT or SUBACCOUNT-<firstname> <lastname>
   * @param regMailingsType - The Registration Mailing Object.
   * @throws MailingException
   */
   private static String getRegMailingId(String recipientType, TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType)
     throws MailingException
   {
       String regMailingId ="";
       try
       {
            if (regMailingsType != null)
            {
              ListIterator itreg  = regMailingsType.getRegistrationMailing().listIterator();

              for (Iterator it=itreg; it.hasNext(); ) 
              {
                  //Get the RegistrationMailingType
                  TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType.RegistrationMailingType regMailingType = (TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType.RegistrationMailingType)itreg.next();
                  if (recipientType.equals(regMailingType.getMailingRecipientType()))
                  {
                      regMailingId            = regMailingType.getRegMailingPrimaryKey();
                      break;
                  }
              }
            }
       }
       catch( Exception e )
       {
         e.printStackTrace();
         throw new MailingException("FFRMMailingXMLServicesImpl.getRegMailingId()..An Exception occurred :"+e.getMessage());
       }
       return regMailingId;      
   }

   /**
   * The API getReceiptCode is used to extract the receiptCode for the recipientType
   * This API is exclusively implemented for PPF folks to give the receipt code for each MailToAddress
   * recipient.
   * @param recipientType - The recipientType e.g., FACILITY or USAGENT or SUBACCOUNT-emailAddress
   * @param regMailingsType - The Registration Mailing Object
   * @throws MailingException
   */
   private static String getReceiptCode(String recipientType, TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType regMailingsType)
     throws MailingException
   {
       String receiptCode ="";
       try
       {
            if (regMailingsType != null)
            {
              ListIterator itreg  = regMailingsType.getRegistrationMailing().listIterator();

              for (Iterator it=itreg; it.hasNext(); ) 
              {
                  //Get the RegistrationMailingType
                  TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType.RegistrationMailingType regMailingType = (TransactionType.DataAreaType.RegistrationType.MailingTypesType.RegistrationMailingsType.RegistrationMailingType)itreg.next();
                  if (recipientType.equals(regMailingType.getMailingRecipientType()))
                  {
                      receiptCode            = regMailingType.getReceiptCode();
                      break;
                  }
              }
            }
       }
       catch( Exception e )
       {
         e.printStackTrace();
         throw new MailingException("FFRMMailingXMLServicesImpl.getRegMailingId()..An Exception occurred :"+e.getMessage());
       }
       return receiptCode;       
   }
    public static void main(String[] args) throws IOException
  {
     try {
     FFRMMailingXMLServicesImpl ffms = new FFRMMailingXMLServicesImpl();
     String ss = ffms.XMLReader("C:\\Development\\PaperProcessor-02142005\\FOF\\MailingType12.xml");
     System.out.println("THE XML IS " + ss);
     ffms.processMessage(ss);
     }catch(Exception e) 
     {
       e.printStackTrace();
       System.out.println("coming here in exception in main method");
     }
  }
  public String XMLReader(String filename)
      throws FileNotFoundException, IOException
  {   

        String line = "";
        StringBuffer content = new StringBuffer();
        
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        //content.append("'");
        while ((line = reader.readLine()) != null) {
            content.append(getParsedValue(line));
            content.append("\n");
        }
        //content.append("'");
        reader.close();

        if (content != null)
        {
         // org.apache.log4j.BasicConfigurator.configure();
         // logger.info("INSIDE THE XML READER FROM FILE");
         // logger.warn("COMING HERE FOR WARNING");
         // logger.error("COMING HERE FOR ERRRORRR");
         // System.out.println(content.toString());
          //notifyFFRM(content.toString());
         // sendMessage(content.toString() );
        }
   return content.toString();     
  }
  public static String getParsedValue(String value)
	{
        if (value.length() == 0)
        {
            return new String("");
        }
        else 
        {
	        StringBuffer sb = new StringBuffer();
	        int i1 = 0;
	        for (int i2 = value.indexOf('\'', i1); i2 != -1; i2 = value.indexOf('\'', i1))
	        {
		            sb.append(value.substring(i1, i2)).append("''");
		            i1 = i2+1;
	        }
	        sb.append(value.substring(i1));
	            return new String(sb.toString());
        }
    }
}