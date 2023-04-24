package com.tcs.ain.mp.common;

import com.tcs.ain.mp.manager.ResourceManager;
import com.tcs.ain.mp.helper.Address;

import java.util.ArrayList;
import java.util.Iterator;

/* $Revision: 1.9 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * EmailContent -  A  final java class that has all the email mailing related content
 * 
 * @version 2.00 8 April 2009
 * @author Padma Killi
*/


public final class EmailContent 
{
  public EmailContent(){}
  /**
    * The API getEmailMessageBody is used to get the Email Message Body  for MT-1 & MT-2.
    * @param ToAddress - Mail To Address
    * @param regNo     - The Registration No of the facility
    * @param mailingTypeName - The mailing type name.
    * @param regMailingPrimaryKey - The primary key in the registration mailing table for a particular facility.
    * @param facName - The company name.
    * @param regNo - The registration number.
    * @param receiptCode - The receipt code for the mailing.
    * @return String - The email content.
    */
    public static String getEmailMessageBody(String ToAddress,String mailingTypeName, String regMailingPrimaryKey,String facName,String regNo,String receiptCode)
    {
       StringBuffer sb = new StringBuffer();
        
      if ("Initial Agent Return Receipt Pending".equals(mailingTypeName)) //MT-1
      {
       
        sb.append("This notice has been sent to you because you have been named the US Agent for the")
          .append("<br>")
          .append("facility listed in the attached registration.")
          .append("<br><br>")
          .append("Please review the registration in the attached document and respond to this e-mail using the following links:")
          .append("<br><br>")
          .append("Press")
          .append("<a href=")
          .append("mailto:")
          .append(ToAddress)
          .append("?subject=agree")
          .append(",")
          .append(regMailingPrimaryKey)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>AGREE</b> </a>")
          .append("if you agree that you are the US Agent for this facility.")
          .append("<br><br>")
          .append("Press")
          .append("<a href=")
          .append("mailto:")
          .append(ToAddress)
          .append("?subject=disagree")
          .append(",")
          .append(regMailingPrimaryKey)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>DISAGREE</b> </a>")
          .append("if you are not the US Agent for this facility.")
          .append("<br><br>")
          .append("If you need further assistance, please go to:<br>")
          .append("<a href=")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append(">")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append("</a><br><br>")
          .append("Thank You.")
          .append("<br>");
      }
      else if ("Initial Facility Return Receipt Pending".equals(mailingTypeName)) //MT-2
      {
       
        sb.append("This notice has been sent to you because the facility listed in the attached document has")
          .append("<br>")
          .append("been registered with the FDA.")
          .append("<br><br>")
          .append("Please review the registration in the attached document and respond to this e-mail using the following links:")
          .append("<br><br>")
          .append("Press")
          .append("<a href=")
          .append("mailto:")
          .append(ToAddress)
          .append("?subject=agree")
          .append(",")
          .append(regMailingPrimaryKey)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>AGREE</b> </a>")
          .append("if you agree with the information provided in the registration for this facility.")
          .append("<br><br>")
          .append("Press")
          .append("<a href=")
          .append("mailto:")
          .append(ToAddress)
          .append("?subject=disagree")
          .append(",")
          .append(regMailingPrimaryKey)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>DISAGREE</b> </a>")
          .append("if you do not agree with the information provided in the registration for this facility.")
          .append("<br><br>")
          .append("If you need further assistance, please go to:<br>")
          .append("<a href=")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append(">")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append("</a><br><br>")
          .append("Thank You.")
          .append("<br>");
      }
       return (sb.toString());
    }
    /**
     * The API getMsgBodyForInitialFacAssignNotification() is used to get the email body for 
     * Initial Facility Assignment Notification (MT-4).
     * @param address - The address object that has facility information.
     * @param regNo  - The registration number of the facility.
     * @param pin    - The pin of the registration.
     * @return String - The email content.
     */
    public static String getMsgBodyForInitialFacAssignNotification(Address address, String regNo, String pin)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head><title>Initial Facility Assignment Notification</title>")
             .append("<style> table.content {border : 1px solid #164676;font-family : Verdana, Arial, Helvetica, Sans-Serif;font-size : 8pt; } td.a{ border: thin double #164676 }</style>")
             .append("</head><body>")
             .append("<table align=left><tr><td><b><u>DEPARTMENT OF HEALTH AND HUMAN SERVICES</u></b></tr></td>")
             .append("</table><br><br><table><tr><td>Food and Drug Administration</td></tr><tr><td>5600 Fishers Lane, HFS-681</td></tr><tr><td>Rockville, MD 20857<tr></td></table><br><br>")
             .append("<table border=0 cellpadding=0 class=content><tr><td class=a colspan=2 align=left><b>Date: </b>"+Util.getDate(Util.getTimestamp().toString(),7)+"</td></tr></table><br><br>")
             .append("<table width=640><tr><td>The U.S. Food and Drug Administration (FDA) is hereby providing you with a confirmation")
             .append(" copy of the information FDA received regarding registration of your facility with the FDA as required by 21 CFR Part 1, Subpart H, and the Public Health ")
             .append("Security and Bioterrorism Preparedness and Response Act of 2002. The FDA Registration of Food Facilities database shows:</td></tr></table><br><br>")
             .append("<table class=content width=640><tr><td class=a><b>Food Facility Name:</b></td><td align=left class=a colspan=6>"+address.getName()+"</td></tr>")
             .append("<tr><td class=a><b>Food Facility Registration Number:</b></td><td align=left class=a colspan=6>"+regNo+"</td></tr>")
             .append("<tr><td class=a><b>PIN:</b></td><td align=left class=a colspan=6>"+pin+"</td></tr></table><br><br>")
             .append("<table class=content width=640><tr><td class=a><b>Street Address Line 1:</b></td><td align=left class=a colspan=6>"+address.getAddressLine1()+"</td></tr>")
             .append("<tr><td class=a><b>Street Address Line 2:</b></td><td align=left class=a colspan=6>"+address.getAddressLine2()+"</td></tr>")
             .append("<tr><td class=a><b>City:</b></td><td align=left class=a>"+address.getCity()+"</td><td class=a><b>State/Province:</b></td><td class=a>"+address.getStateOrProvince()+"</td></tr>")
             .append("<tr><td class=a><b>ZIP/Postal Code:</b></td><td align=left class=a colspan=6>"+address.getZipCode()+"</td></tr>")
             .append("<tr><td class=a><b>Country:</b></td><td align=left class=a colspan=6>"+address.getCountryName()+"</td></tr></table><br>")
             .append("You may want to review the accuracy of the registration in the attached paper document.<b><u>If the<br>")
             .append("information is correct, no action is necessary.</u></b><br><br>")
             .append("If the information is <u>incorrect</u>, you must update your registration within 60 days of receipt of this letter.<br>")
             .append("You can update your registration electronically via the FDA Industry Systems web site at <a href="+ResourceManager.getInstance().getFurlsUrl()+">"+ResourceManager.getInstance().getFurlsUrl()+"</a>.<br>")
             .append("If you have not used the FDA Industry Systems previously, you first will need to create an account.<br> There are tutorials at the above web address that will explain how to do so.")
             .append("If you did not create this<br> registration online, you will need to link the registration to your account so you can view, update or cancel it.<br><br>")
             .append("Alternatively, you may submit an update by mail or fax to:<br><br>")
             .append("U.S. Food and Drug Administration<br>5600 Fishers Lane, HFS-681<br>Rockville, MD USA 20857<br>Fax: 301-210-0247<br><br>")
             .append("You should include a copy of this letter with your mailed or fax response along with your request.<br>Please use Form 3537")
             .append("  if submitting a paper update to your registration, and be sure to check in<br> Section 1 of the form the items applicable to the update.<br><br>")
             .append("You may contact the FDA Industry Systems Help Desk via telephone at: "+ResourceManager.getInstance().getFdaHelpDeskNo()+" if you have questions.<br><br>")
             .append("Thank You.<br><br>FDA Unified Registration and Listing and Prior Notice System Helpdesk</body></html>");
       
      return (sb.toString());
    }

    /**
     * The API getMsgBodyForInitialAgentAssignNotification() is used to get the email body for 
     * Initial Agent Assignment Notification (MT-3).
     * @param address - The address object that has facility information.
     * @param regNo  - The registration number of the facility.
     * @param pin    - The pin of the registration.
     * @return String - The email content.
     */
    public static String getMsgBodyForInitialAgentAssignNotification(Address address, String companyName, String companyAddress, String usAgentReceiptCode, String regNo)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head><title>Initial Agent Assignment Notification</title>")
             .append("<style> table.content {border : 1px solid #164676;font-family : Verdana, Arial, Helvetica, Sans-Serif;font-size : 8pt; } td.a{ border: thin double #164676 }</style>")
             .append("</head><body>")
             .append("<table align=left><tr><td><b><u>DEPARTMENT OF HEALTH AND HUMAN SERVICES</u></b></tr></td>")
             .append("</table><br><br><table><tr><td>Food and Drug Administration</td></tr><tr><td>5600 Fishers Lane, HFS-681</td></tr><tr><td>Rockville, MD 20857<tr></td></table><br><br>")
             .append("<table border=0 cellpadding=0 class=content><tr><td class=a colspan=2 align=left><b>Date: </b>"+Util.getDate(Util.getTimestamp().toString(),7)+"</td></tr></table><br><br>")
             .append("<table width=640><tr><td>The U.S. Food and Drug Administration (FDA) is hereby providing you with a confirmation ")
             .append("copy of the information FDA received listing you as the U.S. Agent for the facility identified below.")
             .append("The registration of the facility was submitted to FDA as required by 21 CFR Part 1, Subpart H, and ")
             .append("the Public Health Security and Bioterrorism Preparedness and Response Act of 2002.The FDA Registration database information shows:</td></tr></table><br><br>")
             .append("<table class=content width=640><tr><td class=a><b>Food Facility Name:</b></td><td align=left class=a colspan=6>"+companyName+"</td></tr>")
             .append("<tr><td class=a><b>Food Facility Address:</b></td><td align=left class=a colspan=6>"+companyAddress+"</td></tr>")
             .append("<tr><td class=a><b>Receipt Code:</b></td><td align=left class=a colspan=6>"+usAgentReceiptCode+"</td></tr></table><br><br>")
             .append("<table class=content width=640><tr><td class=a><b>Name of U.S. Agent:</b></td><td align=left class=a colspan=6>"+address.getName()+"</td></tr>")
             .append("<tr><td class=a><b>Title of U.S. Agent:</b></td><td align=left class=a colspan=6>"+address.getTitle()+"</td></tr>")
             .append("<tr><td class=a><b>Street Address Line 1:</b></td><td align=left class=a colspan=6>"+address.getAddressLine1()+"</td></tr>")
             .append("<tr><td class=a><b>Street Address Line 2:</b></td><td align=left class=a colspan=6>"+address.getAddressLine2()+"</td></tr>")
             .append("<tr><td class=a><b>City:</b></td><td align=left class=a>"+address.getCity()+"</td><td class=a><b>State:</b></td><td class=a>"+address.getStateOrProvince()+"</td></tr>")
             .append("<tr><td class=a><b>ZIP Code:</b></td><td align=left class=a colspan=6>"+address.getZipCode()+"</td></tr>")
             .append("<tr><td class=a><b>Country:</b></td><td align=left class=a>"+address.getCountryName()+"</td><td class=a><b>E-mail Address:</b></td><td class=a>"+address.getEmail()+"</td></tr></table><br>")
             .append("<b><u>If you are correctly listed as the U.S. Agent and the above information is correct, no action is necessary.</u></b><br><br>")
             .append("<b><u>If you are the U.S. Agent for this facility, but your contact information is incorrect</u></b>, you or another individual ")
             .append("authorized by the facility must update this information within 60 days of receipt of this<br> letter using Form 3537. You must check the items being updated in Section 1 of the form. If you have an<br> existing electronic account (established when the facility is registered electronically) that is linked to this registration, you may update your ")
             .append("registration electronically via <a href="+ResourceManager.getInstance().getFurlsUrl()+">"+ResourceManager.getInstance().getFurlsUrl()+"</a>.<br><br>")
             .append("Alternatively, you or the person who registered the facility may submit an update by mail or fax to:<br><br>")
             .append("U.S. Food and Drug Administration<br>5600 Fishers Lane, HFS-681<br>Rockville, MD USA 20857<br>Fax: 301-210-0247<br><br>")
             .append("<b><u>If you are not the U.S. Agent for this facility,</u></b> please click on the following link to notify us that you disagree with this designation: <a href="+ResourceManager.getInstance().getFurlsUrl()+">"+ResourceManager.getInstance().getFurlsUrl()+"</a>.<br> ")
             .append("Once we recieve your notification, we will inform the facility that you disagree with its U.S. Agent <br> designation and request that the facility's ")
             .append("registration be amended to include the name of a person who <br> has affirmatively agreed to serve as the facility's U.S. Agent.<br><br>")
             .append("You may contact the FDA Industry Systems Help Desk via telephone at "+ResourceManager.getInstance().getFdaHelpDeskNo()+" if you have any questions about this process.<br><br>")
             .append("Thank You.<br><br>")
             .append("FDA Unified Registration and Listing and Prior Notice System Helpdesk.</body></html>");
       
      return (sb.toString());
    }
    /**
    * The API getMsgBodyForRegnModifiedByFDA is used to get the Email Message Body for MT-5.
    * This API is used to get the message body for Registration Modified By FDA.
    * NOTE: As of 07/14/2009, CFSAN decides to send this notification as one of the bulk mails.
    * @param address          - The address object.
    */
    public static String getMsgBodyForRegnModifiedByFDA(Address address)
    {
        StringBuffer sb = new StringBuffer();

        sb.append("The registration for the following facility has been modified by FDA to correct information found<br>")
          .append("inaccurate.<br><br>")
          .append("<b>FACILITY NAME :</b> "+address.getName()+"<br>")
          .append("<b>FACILITY STREET ADDRESS, Line 1:</b> "+address.getAddressLine1()+"<br>")
          .append("<b>FACILITY STREET ADDRESS, Line 2:</b> "+address.getAddressLine2()+"<br>")
          .append("<b>CITY:</b> "+address.getCity()+"<br>")
          .append("<b>STATE/PROVINCE/TERRITORY:</b> "+address.getStateOrProvince()+"<br>")
          .append("<b>ZIP CODE (POSTAL CODE):</b> "+address.getZipCode()+"<br>")
          .append("<b>COUNTRY:</b> "+address.getCountryName()+"<br>")
          .append("<b>PHONE NUMBER (Include Area/Country Code):</b> "+address.getBusPhCountryCode()+" "+address.getBusPhAreaCode()+" "+address.getBusPhNo()+" "+address.getBusPhExtn()+"<br>")
          .append("<b>FAX NUMBER (OPTIONAL; Include Area/Country Code):</b> "+address.getFaxPhCountryCode()+" "+address.getFaxPhAreaCode()+" "+address.getFaxPhNo()+"<br><br>")
          .append("If you need further assistance, please go to:<br>")
          .append("<a href=")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append(">")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append("</a><br><br>")
          .append("Thank You");

          return (sb.toString());
    }

    /**
    * The API getMsgBodyForFacMerger is used to get the Email Message Body for MT-8.
    * This API is used to get the message body for Registration Cancelled/Facility Merger
    * @param address          - The address object.
    * @param statusReason     - The Status Reason for Cancellation.
    */
    public static String getMsgBodyForFacMerger(Address address,String statusReason)
    {
        StringBuffer sb = new StringBuffer();

        sb.append("The registration for the following facility has been cancelled.<br><br>")
          .append("<b>FACILITY NAME :</b> "+address.getName()+"<br>")
          .append("<b>FACILITY STREET ADDRESS, Line 1:</b> "+address.getAddressLine1()+"<br>")
          .append("<b>FACILITY STREET ADDRESS, Line 2:</b> "+address.getAddressLine2()+"<br>")
          .append("<b>CITY:</b> "+address.getCity()+"<br>")
          .append("<b>STATE/PROVINCE/TERRITORY:</b> "+address.getStateOrProvince()+"<br>")
          .append("<b>ZIP CODE (POSTAL CODE):</b> "+address.getZipCode()+"<br>")
          .append("<b>COUNTRY:</b> "+address.getCountryName()+"<br>")
          .append("<b>PHONE NUMBER (Include Area/Country Code):</b>"+address.getBusPhCountryCode()+" "+address.getBusPhAreaCode()+" "+address.getBusPhNo()+" "+address.getBusPhExtn()+"<br>")
          .append("<b>FAX NUMBER (OPTIONAL; Include Area/Country Code):</b> "+address.getFaxPhCountryCode()+" "+address.getFaxPhAreaCode()+" "+address.getFaxPhNo()+"<br><br>")
          .append("The reason provided for the cancellation is : "+statusReason+"<br><br>")
          .append("Cancelled registrations cannot be re-activated. If this registration was cancelled in error, a <br>")
          .append("new registration is required.<br><br>")
          .append("If you need further assistance, please go to :<br>")
          .append("<a href=")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append(">")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append("</a><br><br>")
          .append("Thank You");

          return (sb.toString());
    }

    /**
     * The API getMsgBodyForRegnCancelled() is used to get the email body for Registration Cancelled 
     * Notification (MT-6).
     * @param address - The address object that has the facility information.
     * @param regNo   - The registration number of the facility.
     * @param cancelDate - The registration cancelled date.
     * @param isUsAgent - A boolean that checks whether mailing is for the usAgent or not.
     * @return String - The message body.
     */
     public static String getMsgBodyForRegnCancelled(Address address, String regNo, String cancelDate, boolean isUsAgent)
     {
        StringBuffer sb = new StringBuffer();
        if (isUsAgent) {regNo="";}
        
             sb.append("<html><head><title>Regsitration Cancelled Notification</title>")
             .append("<style> table.content {border : 1px solid #164676;font-family : Verdana, Arial, Helvetica, Sans-Serif;font-size : 8pt; } td.a{ border: thin double #164676 }</style>")
             .append("</head><body>")
             .append("<table align=left><tr><td><b><u>DEPARTMENT OF HEALTH AND HUMAN SERVICES</u></b></tr></td>")
             .append("</table><br><br><table><tr><td>Food and Drug Administration</td></tr><tr><td>5600 Fishers Lane, HFS-681</td></tr><tr><td>Rockville, MD 20857<tr></td></table><br><br>")
             .append("<table border=0 cellpadding=0 class=content><tr><td class=a colspan=2 align=left><b>Date: </b>"+Util.getDate(Util.getTimestamp().toString(),7)+"</td></tr></table><br><br>")
             .append("<table width=640><tr><td>Dear Sir/Madam,</td></tr><tr><td>The U.S. Food and Drug Administration (FDA) is hereby providing you with a confirmation ")
             .append("copy of the cancellation of registration that FDA received regarding the facility listed below in accordance with 21 CFR Part 1, Subpart H, and the Public Health Security and Bioterrorism Preparedness and Response Act of 2002.</td></tr></table><br><br>")
             .append("<table class=content width=640><tr><td class=a><b>Date of Cancellation:</b></td><td align=left class=a colspan=6>"+cancelDate+"</td></tr>")
             .append("<tr><td class=a><b>Food Facility Registration Number:</b></td><td align=left class=a colspan=6>"+regNo+"</td></tr>")
             .append("<tr><td class=a><b>Food Facility Name:</b></td><td align=left class=a colspan=6>"+address.getName()+"</td></tr>")
             .append("<tr><td class=a><b>Street Address Line 1:</b></td><td align=left class=a colspan=6>"+address.getAddressLine1()+"</td></tr>") 
             .append("<tr><td class=a><b>Street Address Line 2:</b></td><td align=left class=a colspan=6>"+address.getAddressLine2()+"</td></tr>")
             .append("<tr><td class=a><b>City:</b></td><td align=left class=a>"+address.getCity()+"</td><td class=a><b>State/Province:</b></td><td class=a>"+address.getStateOrProvince()+"</td></tr>")
             .append("<tr><td class=a><b>ZIP/Postal Code:</b></td><td align=left class=a colspan=6>"+address.getZipCode()+"</td></tr>")
             .append("<tr><td class=a><b>Country:</b></td><td align=left class=a colspan=6>"+address.getCountryName()+"</td></tr></table><br>")
             .append("As requested, we have cancelled the facility's registration. <br><br>")
             .append("If you have any further questions or comments please contact us at the FDA Industry Systems Helpdesk at 1-800-216-7331<br>")
             .append("(301-575-0156 outside the United States) or email us at FURLS@FDA.GOV.<br><br>")
             .append("Thank You.<br><br>")
             .append("FDA Unified Registration and Listing and Prior Notice System Helpdesk</body></html>");
     
       
       return (sb.toString());
     }
    /**
    * The API getMsgBodyForTransferOfOwn is used to get the Email Message Body of MT-7.
    * This API is used to get the message body for Transfer Of Ownership
    * @param toAddress        - Mail To Address
    * @param address          - The address object.
    * @param regMailingId     - The unique Registration Mailing Record Id.
    */
    public static String getMsgBodyForTransferOfOwn(String toAddress,Address address,String regMailingId)
    {
        StringBuffer sb = new StringBuffer();

        sb.append("A new registration for the following facility has been created. The submitter stated that<br>")
          .append("this facility has transferred ownership.<br><br>")
          
          .append("<b>FACILITY NAME :</b> "+address.getName()+"<br>")
          .append("<b>FACILITY STREET ADDRESS, Line 1:</b> "+address.getAddressLine1()+"<br>")
          .append("<b>FACILITY STREET ADDRESS, Line 2:</b> "+address.getAddressLine2()+"<br>")
          .append("<b>CITY:</b> "+address.getCity()+"<br>")
          .append("<b>STATE/PROVINCE/TERRITORY:</b> "+address.getStateOrProvince()+"<br>")
          .append("<b>ZIP CODE (POSTAL CODE):</b> "+address.getZipCode()+"<br>")
          .append("<b>COUNTRY:</b> "+address.getCountryName()+"<br>")
          .append("<b>PHONE NUMBER (Include Area/Country Code):</b> "+address.getBusPhCountryCode()+" "+address.getBusPhAreaCode()+" "+address.getBusPhNo()+" "+address.getBusPhExtn()+"<br>")
          .append("<b>FAX NUMBER (OPTIONAL; Include Area/Country Code):</b> "+address.getFaxPhCountryCode()+" "+address.getFaxPhAreaCode()+" "+address.getFaxPhNo()+"<br><br>")
          .append("A transfer of ownership requires that the previous owner be notified, and that the<br>")
          .append("previous owner agrees that the facility has a new owner.<br><br>")
          .append("Please select the following <br>")
          .append("<a href=")
          .append("mailto:")
          .append(toAddress)
          .append("?subject=agree")
          .append(",")
          .append(regMailingId)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>AGREE</b> </a>")
          .append("link if you agree that the facility has a new owner.<br><br>")
          .append("Select the following <br>")
          .append("<a href=")
          .append("mailto:")
          .append(toAddress)
          .append("?subject=disagree")
          .append(",")
          .append(regMailingId)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>DISAGREE</b> </a>")
          .append("link if the facility has not changed ownership.<br><br>")
          .append("If you need further assistance, please go to:<br>")
          .append("<a href=")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append(">")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append("</a><br><br>")
          .append("Thank You");

          return (sb.toString());
    }
     /**
    * The API getMsgBodyForAgenReassign is used to get the Email Message Body for MT-11.
    * This API is used to get the message body for Agent Re-assignment
    * NOTE: 07/15/2009 - This notification has to go away and instead, MT-3 has 
    * to be sent. Changes required in FFRM to initiate MT-3 instead of MT-11.
    * @param toAddress        - Mail To Address.
    * @param address          - The address object.
    * @param regMailingId     - The unique Registration Mailing Record Id.
    * @param regNo            - The registration number of the facility.
    */
    public static String getMsgBodyForAgenReassign(String toAddress,ArrayList addressList,String regMailingId,String regNo)
    {
        StringBuffer sb = new StringBuffer();
         sb.append("This e-mail has been sent to notify you that the US Agent for the following facility has<br>");
         sb.append("been reassigned.<br><br>");

         sb.append("<b>REGISTRATION NUMBER :</b>"+regNo+"<br><br>");
          
          for(Iterator it=addressList.iterator(); it.hasNext(); ) 
          {
              Address address = (Address)it.next();
              if ("F".equals(address.getAddressTypeId()))
              {
                  sb.append("Facility Name and Address:<br><br>");
                  sb.append("<b>FACILITY NAME:</b> "+address.getName()+"<br>");
                  sb.append("<b>FACILITY STREET ADDRESS, Line 1:</b> "+address.getAddressLine1()+"<br>");
                  sb.append("<b>FACILITY STREET ADDRESS, Line 2:</b> "+address.getAddressLine2()+"<br>");
                  sb.append("<b>CITY:</b> "+address.getCity()+"<br>");
                  sb.append("<b>STATE/PROVINCE/TERRITORY:</b> "+address.getStateOrProvince()+"<br>");
                  sb.append("<b>ZIP CODE (POSTAL CODE):</b> "+address.getZipCode()+"<br>");
                  sb.append("<b>COUNTRY:</b> "+address.getCountryName()+"<br>");
                  sb.append("<b>PHONE NUMBER (Include Area/Country Code):</b> "+address.getBusPhCountryCode()+" "+address.getBusPhAreaCode()+" "+address.getBusPhNo()+" "+address.getBusPhExtn()+"<br>");
                  sb.append("<b>FAX NUMBER (OPTIONAL; Include Area/Country Code):</b> "+address.getFaxPhCountryCode()+" "+address.getFaxPhAreaCode()+" "+address.getFaxPhNo()+"<br><br>");
              }
              else if ("U".equals(address.getAddressTypeId()))
              {
                  sb.append("The new agent is :<br><br>");
                  sb.append("<b>US AGENT NAME:</b> "+address.getName()+"<br>");
                  sb.append("<b>US AGENT STREET ADDRESS, Line 1:</b> "+address.getAddressLine1()+"<br>");
                  sb.append("<b>US AGENT STREET ADDRESS, Line 2:</b> "+address.getAddressLine2()+"<br>");
                  sb.append("<b>CITY:</b> "+address.getCity()+"<br>");
                  sb.append("<b>STATE/PROVINCE/TERRITORY:</b> "+address.getStateOrProvince()+"<br>");
                  sb.append("<b>ZIP CODE (POSTAL CODE):</b> "+address.getZipCode()+"<br>");
                  sb.append("<b>COUNTRY:</b> "+address.getCountryName()+"<br>");
                  sb.append("<b>PHONE NUMBER (Include Area/Country Code):</b> "+address.getBusPhCountryCode()+" "+address.getBusPhAreaCode()+" "+address.getBusPhNo()+" "+address.getBusPhExtn()+"<br>");
                  sb.append("<b>FAX NUMBER (OPTIONAL; Include Area/Country Code):</b> "+address.getFaxPhCountryCode()+" "+address.getFaxPhAreaCode()+" "+address.getFaxPhNo()+"<br><br>");
              }
            
          }
   
          sb.append("Please select the following <br>");
          sb.append("<a href=");
          sb.append("mailto:");
          sb.append(toAddress);
          sb.append("?subject=agree");
          sb.append(",");
          sb.append(regMailingId);
          sb.append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.");
          sb.append("> <b>AGREE</b> </a>");
          sb.append("link if you agree with the US Agent re-assignment.<br><br>");
          sb.append("Select the following <br>");
          sb.append("<a href=");
          sb.append("mailto:");
          sb.append(toAddress);
          sb.append("?subject=disagree");
          sb.append(",");
          sb.append(regMailingId);
          sb.append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.");
          sb.append("> <b>DISAGREE</b> </a>");
          sb.append("link if you disagree with the US Agent re-assignment.<br><br>");
          sb.append("If you need further assistance, please go to:<br>");
          sb.append("<a href=");
          sb.append(ResourceManager.getInstance().getFdaHelpDeskUrl());
          sb.append(">");
          sb.append(ResourceManager.getInstance().getFdaHelpDeskUrl());
          sb.append("</a><br><br>");
          sb.append("Thank You");

          return (sb.toString());
    }

    /**
     * The API getMsgBodyForFacWithoutUsAgent() is used to the Email Message Body for MT-9
     * @param regNo - The food facility registration number
     * @param companyName - The food facility name
     * @param inValidUsAgentName - The in valid US Agent name
     * @param address - The Mail To Address object
     * @return String - The Email Message Body.
     */
    public static String getMsgBodyForFacWithoutUsAgent(String regNo,String companyName, String inValidUsAgentName, Address address)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head><title>Facility without US Agent Notification</title>")
          .append("<style> table.content {border : 1px solid #164676;font-family : Verdana, Arial, Helvetica, Sans-Serif;font-size : 8pt; } td.a{ border: thin double #164676 }</style>")
          .append("</head><body>")
          .append("<table align=left><tr><td><b><u>DEPARTMENT OF HEALTH AND HUMAN SERVICES</u></b></tr></td>")
          .append("</table><br><br><table><tr><td>Food and Drug Administration</td></tr><tr><td>5600 Fishers Lane, HFS-681</td></tr><tr><td>Rockville, MD 20857<tr></td></table><br><br>")

          .append("<table class=content width=640><tr><td class=a colspan=6><b>Date:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+Util.getDate(Util.getTimestamp().toString(),7)+"</td></tr><br><br>")
          .append("<tr><td class=a colspan=6><b>Name:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getName()+"</td></tr>")
          .append("<tr><td class=a colspan=6><b>Street Address Line 1:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getAddressLine1()+"</td></tr>") 
          .append("<tr><td class=a colspan=6><b>Street Address Line 2:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getAddressLine2()+"</td></tr>")
          .append("<tr><td class=a colspan=6><b>City:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getCity()+"</td></tr>")
          .append("<tr><td class=a colspan=6><b>State/Province:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getStateOrProvince()+"</td></tr>")
          .append("<tr><td class=a colspan=6><b>ZIP Code:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getZipCode()+"</td></tr>")
          .append("<tr><td class=a colspan=6><b>Country:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+address.getCountryName()+"</td></tr></table><br>")
          
          .append("<table class=content width=640><tr><td class=a><b>Subject:</b>&nbsp;&nbsp;&nbsp;&nbsp;Invalid U.S. Agent</td></tr>")
          .append("<tr><td class=a colspan=6><b>Name of U.S. Agent:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+inValidUsAgentName+"</td></tr></table>")
          .append("<br><br>")
          .append("<table class=content width=640><tr><td class=a colspan=6><b>Food Facility Name:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+companyName+"</td></tr>")
          .append("<tr><td class=a colspan=6><b>Food Facility Registration Number:</b>&nbsp;&nbsp;&nbsp;&nbsp;"+regNo+"</td></tr></table>")
          .append("<br>")
          .append("The U.S. Food and Drug Administration has sent you this notice to inform you that the above-named U.S. Agent<br>")
          .append("listed on your facility's registration has not agreed to be your U.S. Agent. As specified in 21 CFR � 1.234(a),<br>")
          .append("the owner, operator, or agent in charge of the facility must submit an update to your facility's registration<br>")
          .append("that includes contact information for a valid U.S. Agent. This update must be submitted within 60 calendar days<br>")
          .append("of receipt of this notice. To update registration information at any time, access the online FDA Industry Systems<br>")
          .append("at <a href="+ResourceManager.getInstance().getFurlsUrl()+">"+ResourceManager.getInstance().getFurlsUrl()+"</a>, or you may use DHHS/FDA Form No. 3537. To expedite<br>")
          .append("updates, FDA recommends using the online system.<br><br>")

          .append("If you wish to return the Form No. 3537:<br><br>")
          .append("<table class=content width=640><tr><td class=a><b>By Postal Mail:</b></td><td align=left class=a colspan=6>U.S. Department of Health and Human Services <br>Food and Drug Administration <br>5600 Fishers Lane, HFS-681 <br>Rockville, MD 20857</td></tr>")
          .append("<tr><td class=a><b>By Fax:</b></td><td align=left class=a colspan=6>301-210-0247</td></tr></table><br><br>")

          .append("IF YOU NEED FURTHER ASSISTANCE<br>")

          .append("Please see <a href=http://www.cfsan.fda.gov/~furls/ovffreg.html>http://www.cfsan.fda.gov/~furls/ovffreg.html</a> or <a href=http://www.cfsan.fda.gov/~furls/helpf.html>http://www.cfsan.fda.gov/~furls/helpf.html</a> or contact<br>")
          .append("the FDA Industry Systems Help Desk via telephone at: "+ResourceManager.getInstance().getFdaHelpDeskNo()+".</body></html>");

          return (sb.toString());
    }

     /**
    * The API getMsgBodyForPinModification is used to get the Email Message Body for MT-12.
    * This API is used to get the message body for PIN Modification
    * @param regNo          - The Registration Number of the facility.
    * @param pin            - The modified PIN
    */
    public static String getMsgBodyForPinModification(String regNo, String pin, Address address, String companyName)
    {
        StringBuffer sb = new StringBuffer();

        sb.append("<html><head><title>PIN Modofication Notification</title>")
          .append("<style> table.content {border : 1px solid #164676;font-family : Verdana, Arial, Helvetica, Sans-Serif;font-size : 8pt; } td.a{ border: thin double #164676 }</style>")
          .append("</head><body>")
          .append("<table align=left><tr><td><b><u>DEPARTMENT OF HEALTH AND HUMAN SERVICES</u></b></tr></td>")
          .append("</table><br><br><table><tr><td>Food and Drug Administration</td></tr><tr><td>5600 Fishers Lane, HFS-681</td></tr><tr><td>Rockville, MD 20857<tr></td></table><br><br>")
          .append("<table class=content width=640><tr><td class=a><b>Date:</b></td><td align=left class=a colspan=6>"+Util.getDate(Util.getTimestamp().toString(),7)+"</td></tr>")
          .append("<tr><td class=a><b>Name:</b></td><td align=left class=a colspan=6>"+address.getName()+"</td></tr>")
          .append("<tr><td class=a><b>Street Address Line 1:</b></td><td align=left class=a colspan=6>"+address.getAddressLine1()+"</td></tr>") 
          .append("<tr><td class=a><b>Street Address Line 2:</b></td><td align=left class=a colspan=6>"+address.getAddressLine2()+"</td></tr>")
          .append("<tr><td class=a><b>City:</b></td><td align=left class=a>"+address.getCity()+"</td><td class=a><b>State/Province:</b></td><td class=a>"+address.getStateOrProvince()+"</td></tr>")
          .append("<tr><td class=a><b>ZIP Code:</b></td><td align=left class=a colspan=6>"+address.getZipCode()+"</td></tr>")
          .append("<tr><td class=a><b>Country:</b></td><td align=left class=a colspan=6>"+address.getCountryName()+"</td></tr></table><br>")
          .append("<table class=content width=640><tr><td class=a><b>Subject:</b></td><td align=left class=a colspan=6>PIN Modification</td></tr></table><br>")
          .append("<table class=content width=640><tr><td class=a><b>Food Facility Name:</b></td><td align=left class=a colspan=6>"+companyName+"</td></tr>")
          .append("<tr><td class=a><b>Food Facility Registration Number:</b></td><td align=left class=a colspan=6>"+regNo+"</td></tr>")
          .append("<tr><td class=a><b>New PIN:</b></td><td align=left class=a colspan=6>"+pin+"</td></tr></table><br><br>")
          .append("The U.S. Food and Drug Administration has sent you this notice to confirm the modification of the above-named<br>")
          .append("facility's Personal Identification Number(PIN). No response is necessary on your part, unless you believe an<br>")
          .append("individual not authorized by the owner, operator, or agent in charge of the facility modified the PIN. FDA has<br>")
          .append("not released the new PIN to any other individual. Therfore, it is your responsibility to provide the PIN only to<br>")
          .append("those individuals you wish to have access to your registration information. Be sure to guard the PIN and registration<br>")
          .append("number closely, as they are both required to access this registration information on-line. If you have questions, or<br>")
          .append("if you believe the PIN was modified in error, you may contact the FDA:<br><br>")
          .append("<table class=content width=640><tr><td class=a><b>By Telephone:</b></td><td align=left class=a colspan=6>FDA Industry Systems Help Desk:<br> "+ResourceManager.getInstance().getFdaHelpDeskNo()+"</td></tr>")
          .append("<tr><td class=a><b>By Postal Mail:</b></td><td align=left class=a colspan=6>U.S. Department of Health and Human Services <br>Food and Drug Administration <br>5600 Fishers Lane, HFS-681 <br>Rockville, MD 20857</td></tr>")
          .append("<tr><td class=a><b>By Fax:</b></td><td align=left class=a colspan=6>301-210-0247</td></tr></table><br><br>")
          .append("IF YOU NEED FURTHER ASSISTANCE<br>")
          .append("To update registration information at any time, access the online FDA Industry Systems at  <a href="+ResourceManager.getInstance().getFurlsUrl()+">"+ResourceManager.getInstance().getFurlsUrl()+"</a>, or<br>")
          .append("you may use DHHS/FDA Form No. 3537. To expedite updates, FDA recommends using the online system. For more help,<br>")
          .append("please see <a href=http://www.cfsan.fda.gov/~furls/ovffreg.html>http://www.cfsan.fda.gov/~furls/ovffreg.html</a> or <a href=http://www.cfsan.fda.gov/~furls/helpf.html>http://www.cfsan.fda.gov/~furls/helpf.html</a>.</body></html>");
        
          return (sb.toString());
    }
    /**
     * The API getMsgBodyForRegistrationPreview() is used to get the email message body.
     */
     public static String getMsgBodyForRegistrationPreview()
     {
//         StringBuffer sb = new StringBuffer();
//         sb.append("Please find the attached Facility Registration Information you requested");

         return "Please find the attached Facility Registration Information you requested";
     }

     /**
      * The API getMsgBodyForInvalidRegistration is used to get the Email Message Body for MT-1.
      * This API is used to get the message body for Invalid Registration
      * @param regNo          - The Registration Number of the facility.
      * This new method getMsgBodyForInvalidRegistration() added by Naidu Sanapala
      */
   // public static String getMsgBodyForInvalidRegistration()

     public static String getMsgBodyForInvalidRegistration(String regNo)
    {
        StringBuffer sb = new StringBuffer();

        sb.append("<html><head><title>Invalid Registration Notification</title>")
          .append("<style> table.content {border : 1px solid #164676;font-family : Verdana, Arial, Helvetica, Sans-Serif;font-size : 8pt; } td.a{ border: thin double #164676 }</style>")
          .append("</head><body>")
          .append("<table align=left><tr><td><b><u>DEPARTMENT OF HEALTH AND HUMAN SERVICES</u></b></tr></td>")
          .append("</table><br><br><table><tr><td>Food and Drug Administration</td></tr><tr><td>5600 Fishers Lane, HFS-681</td></tr><tr><td>Rockville, MD 20857<tr></td></table><br><br>")
          .append("<table class=content width=640><tr><td class=a><b>Date:</b></td><td align=left class=a colspan=6>"+Util.getDate(Util.getTimestamp().toString(),7)+"</td></tr>")
          .append("<tr><td class=a><b>Your Food Facility Registration Number:</b></td><td align=left class=a colspan=6>"+regNo+"</td></tr>")
          .append(" <br>The U.S. Food and Drug Administration has sent you to notify that <br><b>YOUR REGISTRATION IS INVALID</B>, Please contact FDA<br><br>")
          .append("<table class=content width=640><tr><td class=a><b>By Telephone:</b></td><td align=left class=a colspan=6>FDA Industry Systems Help Desk:<br> "+ResourceManager.getInstance().getFdaHelpDeskNo()+"</td></tr>")
          .append("<tr><td class=a><b>By Postal Mail:</b></td><td align=left class=a colspan=6>U.S. Department of Health and Human Services <br>Food and Drug Administration <br>5600 Fishers Lane, HFS-681 <br>Rockville, MD 20857</td></tr>")
          .append("<tr><td class=a><b>By Fax:</b></td><td align=left class=a colspan=6>301-210-0247</td></tr></table><br><br>")
          .append("IF YOU NEED FURTHER ASSISTANCE<br>")
          .append("To update registration information at any time, access the online FDA Industry Systems at  <a href="+ResourceManager.getInstance().getFurlsUrl()+">"+ResourceManager.getInstance().getFurlsUrl()+"</a>, or<br>")
          .append("you may use DHHS/FDA Form No. 3537. To expedite updates, FDA recommends using the online system. For more help,<br>")
          .append("please see <a href=http://www.cfsan.fda.gov/~furls/ovffreg.html>http://www.cfsan.fda.gov/~furls/ovffreg.html</a> or <a href=http://www.cfsan.fda.gov/~furls/helpf.html>http://www.cfsan.fda.gov/~furls/helpf.html</a>.</body></html>");
        
          return (sb.toString());
    }
}