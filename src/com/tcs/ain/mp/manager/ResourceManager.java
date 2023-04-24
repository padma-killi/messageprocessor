package com.tcs.ain.mp.manager;

import com.tcs.ain.mp.common.ResourceException;
import java.util.*;

/**
 * The ResourceManager is a singleton class which is used to provide the resources for other objects like
 * TransformXMLManager to locate the XSL file path , SMTP server name etc
 * @version 2.0 06 Sep 2003
 * @author :Padma Killi
 * Modified Date   :12 April 2009
 * 
 * @see ResourceException
 */
public final class ResourceManager 
{

  private static ResourceManager _this;

  //Static block to instantiate the singleton in a thread safe way.
  //Static initializer
  static 
  {
    _this = new ResourceManager();
  }

  private ResourceManager()
  {
        try
        {
            init();
        }
        catch(ResourceException re)
        {
          re.printStackTrace();
        }
  }
  /*Method to get the instance of ResourceManager(Singleton)*/
    public static ResourceManager getInstance()
    {
         return _this;
    }	
  /**
  * Initialization. Specifically, locate and load the properties file.
  * @throws ResourceException
  */        
  private void init() throws ResourceException
  {
        // Determine if the class has not been initialised, and
        // so needs initialisation for objects to be stable
        if (_initialised == false)
        {
            // Try to locate properties via classpath
            ResourceBundle resourceBundle = null;

            // Get the resouce bundle
            try
            {
                resourceBundle = PropertyResourceBundle.getBundle(PROPERTIES_FILE);
            }
            catch (MissingResourceException e)
            {
                e.printStackTrace();
                System.out.println("Unable to locate the resource bundle");
            }
             // Check for bundle
            if (resourceBundle != null)
            {
                
                // Get properties from bundle
                setConfigSettings(resourceBundle);
                
            }
            // Indicate that the initialisation has been performed correctly
          _initialised = true;
        }
        
  }

  /**
   * The API setConfigSettings will set the necessary settings.
   * @param resourceBundle - This is the Resource Bundle.
   * @throws ResourceException
   */
  private void setConfigSettings(ResourceBundle resourceBundle) throws ResourceException
  {
       //Load XSL file path
        try
        {
            this.setXslFilePath(resourceBundle.getString(XSL_FILE_PATH));
        }
        catch (MissingResourceException e)
        {
            throw new ResourceException();
        }
        //Load SMTP server name
        try
        {
          this.setSmtpServerName(resourceBundle.getString(SMTP_SERVER_NAME));
        }
         catch (MissingResourceException e)
        {
            throw new ResourceException();
        }

        //Load the html write path
        try
        {
          this.setHtmlWritePath(resourceBundle.getString(HTML_WRITE_PATH));
        }
         catch (MissingResourceException e)
        {
            throw new ResourceException();
        }
        //Load the From Address
        try
        {
          this.setFromAddress(resourceBundle.getString(EMAIL_FROM_ADDRESS));
        }
         catch (MissingResourceException e)
        {
            throw new ResourceException();
        }

        //Load the Acct Mgmt From Address
        try
        {
          this.setAcctMgmtFromAddress(resourceBundle.getString(ACCT_MGMT_FROM_ADDRESS));
        }
         catch (MissingResourceException e)
        {
            throw new ResourceException();
        }

        //Load the Acct Mgmt Email Subject
        try
        {
          this.setAcctMgmtEmailSubject(resourceBundle.getString(ACCT_MGMT_EMAIL_SUBJECT));
        }
         catch (MissingResourceException e)
        {
            throw new ResourceException();
        }
        
        //Load the FDA_HELP_DESK_NO
        try
        {
           this.setFdaHelpDeskNo(resourceBundle.getString(FDA_HELP_DESK_NO));
        }
         catch (MissingResourceException e)
        {
            throw new ResourceException();
        }
        //Load the FDA_HELP_DESK_URL
        try
        {
           this.setFdaHelpDeskUrl(resourceBundle.getString(FDA_HELP_DESK_URL));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        }
        //Load the FURLS_URL
        try
        {
           this.setFurlsUrl(resourceBundle.getString(FURLS_URL));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        }

        //Load the PPF_HELP_DESK_EMAIL
        try
        {
          this.setPpfHelpDeskEmail(resourceBundle.getString(PPF_HELP_DESK_EMAIL));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        }
        
        //Load the PPF_HELP_DESK_EMAIL_SUBJECT
        try
        {
           this.setPpfHelpDeskEmailSubject(resourceBundle.getString(PPF_HELP_DESK_EMAIL_SUBJECT));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        }        

        //Load the APP_ERROR_ALERT_SWITCH
        try
        {
           this.setAppErrorAlertSwitch(resourceBundle.getString(APP_ERROR_ALERT_SWITCH));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        }   

        //Load the APP_ERROR_ALERT_TO
        try
        {
           this.setAppErrorAlertTo(resourceBundle.getString(APP_ERROR_ALERT_TO));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        }
        
        //Load the APP_ERROR_ALERT_SUBJECT
        try
        {
           this.setAppErrorAlertSubject(resourceBundle.getString(APP_ERROR_ALERT_SUBJECT));
        }
        catch(MissingResourceException e)
        {
           throw new ResourceException();
        } 
        // Load the Initial Factory credentials - ADDED ON Nov 3rd 2009
        try 
        {
          this.setInitialFactory(resourceBundle.getString(JAVA_NAMING_FACTORY_INITIAL) );
        }catch(MissingResourceException e) 
        {
          throw new ResourceException();
        }
        try 
        {
          this.setProviderUrl(resourceBundle.getString(JAVA_NAMING_PROVIDER_URL) );
        }catch(MissingResourceException e) 
        {
          throw new ResourceException();
        }
        try 
        {
          this.setSecurityprincipal(resourceBundle.getString(JAVA_NAMING_SECURITY_PRINCIPAL) );
        }catch(MissingResourceException e) 
        {
          throw new ResourceException();
        }
        try 
        {
          this.setSecuritycredentials(resourceBundle.getString(JAVA_NAMING_SECURITY_CREDENTIAL) );
        }catch(MissingResourceException e) 
        {
          throw new ResourceException();
        }
  }

  public String getXslFilePath()
  {
    return (this.xslFilePath);
  }
  public void setXslFilePath(String xslFilePath)
  {
    this.xslFilePath = xslFilePath;
  }
  public String getSmtpServerName()
  {
    return (this.smtpServerName);
  }
  public void setSmtpServerName(String smtpServerName)
  {
    this.smtpServerName  = smtpServerName;
  }
  public String getHtmlWritePath()
  {
    return (this.htmlWritePath);
  }
  public void setHtmlWritePath(String htmlWritePath)
  {
     this.htmlWritePath = htmlWritePath;
  }
  public String getFromAddress()
  {
     return (this.fromAddress);
  }
  public void setFromAddress(String fromAddress)
  {
    this.fromAddress = fromAddress;
  }
  public String getAcctMgmtEmailSubject()
  {
    return (this.acctMgmtEmailSubject);
  }
  public void setAcctMgmtEmailSubject(String acctMgmtEmailSubject)
  {
    this.acctMgmtEmailSubject = acctMgmtEmailSubject;
  }
  public String getAcctMgmtFromAddress()
  {
    return (this.acctMgmtFromAddress);
  }
  public void setAcctMgmtFromAddress(String acctMgmtFromAddress)
  {
    this.acctMgmtFromAddress = acctMgmtFromAddress;
  }
  public String getFdaHelpDeskNo()
  {
    return (this.fdaHelpDeskNo);
  }
  public void setFdaHelpDeskNo(String fdaHelpDeskNo)
  {
    this.fdaHelpDeskNo = fdaHelpDeskNo;
  }
  public String getFdaHelpDeskUrl()
  {
    return (this.fdaHelpDeskUrl);
  }
  public void setFdaHelpDeskUrl(String fdaHelpDeskUrl)
  {
      this.fdaHelpDeskUrl = fdaHelpDeskUrl;
  }
  public String getFurlsUrl()
  {
    return (this.furlsUrl);
  }
  public void setFurlsUrl(String furlsUrl)
  {
    this.furlsUrl = furlsUrl;
  }
  public String getPpfHelpDeskEmail()
  {
    return (this.ppfHelpDeskEmail);
  }
  public void setPpfHelpDeskEmail(String ppfHelpDeskEmail)
  {
    this.ppfHelpDeskEmail = ppfHelpDeskEmail;
  }
  public String getPpfHelpDeskEmailSubject()
  {
     return (this.ppfHelpDeskEmailSubject);
  }
  public void setPpfHelpDeskEmailSubject(String ppfHelpDeskEmailSubject)
  {
     this.ppfHelpDeskEmailSubject = ppfHelpDeskEmailSubject;
  }
  public String getAppErrorAlertSwitch()
  {
     return (this.appErrorAlertSwitch);  
  }
  public void setAppErrorAlertSwitch(String appErrorAlertSwitch)
  {
    this.appErrorAlertSwitch = appErrorAlertSwitch;
  }
  public String getAppErrorAlertTo()
  {
    return (this.appErrorAlertTo);
  }
  public void setAppErrorAlertTo(String appErrorAlertTo)
  {
    this.appErrorAlertTo = appErrorAlertTo;
  }
  public String getAppErrorAlertSubject()
  {
    return (this.appErrorAlertSubject);
  }
  public void setAppErrorAlertSubject(String appErrorAlertSubject)
  {
    this.appErrorAlertSubject = appErrorAlertSubject;
  }
   public String getInitialFactory()
  {
    return initialFactory;
  }

  public void setInitialFactory(String newInitialFactory)
  {
    initialFactory = newInitialFactory;
  }

  public String getProviderUrl()
  {
    return providerUrl;
  }

  public void setProviderUrl(String newProviderUrl)
  {
    providerUrl = newProviderUrl;
  }

  public String getSecurityprincipal()
  {
    return securityprincipal;
  }

  public void setSecurityprincipal(String newSecurityprincipal)
  {
    securityprincipal = newSecurityprincipal;
  }

  public String getSecuritycredentials()
  {
    return securitycredentials;
  }

  public void setSecuritycredentials(String newSecuritycredentials)
  {
    securitycredentials = newSecuritycredentials;
  }
  
  private String xslFilePath;
  private String smtpServerName;
  private String htmlWritePath;
  private String fromAddress;
  private String acctMgmtEmailSubject;
  private String acctMgmtFromAddress;
  private String fdaHelpDeskNo;
  private String fdaHelpDeskUrl="http://www.cfsan.fda.gov/~furls/helpf.html";
  private String furlsUrl="http://www.access.fda.gov/";
  private String ppfHelpDeskEmail;
  private String ppfHelpDeskEmailSubject;
  private String appErrorAlertSwitch;
  private String appErrorAlertTo;
  private String appErrorAlertSubject;

  //Added the new private variables to access initial context factory
  private String initialFactory;
  private String providerUrl;
  private String securityprincipal;
  private String securitycredentials;
  
  
 
  private  boolean _initialised  = false;
  
  private  static final String PROPERTIES_FILE            ="ppf-interface";
  //private  final String PROPERTIES_SUFFIX          =".properties";  

  //Property Constants
  private static final String XSL_FILE_PATH               = "xsl_file_path";
  private static final String HTML_WRITE_PATH             = "html_write_path";
  private static final String SMTP_SERVER_NAME            = "smtp_server_name";
  private static final String EMAIL_FROM_ADDRESS          = "email_from_address";
  private static final String ACCT_MGMT_EMAIL_SUBJECT     = "acct_mgmt_email_subject";
  private static final String ACCT_MGMT_FROM_ADDRESS      = "acct_mgmt_email_from_address";
  private static final String FDA_HELP_DESK_NO            = "fda_help_desk_number";
  private static final String FDA_HELP_DESK_URL           = "fda_help_desk_url";
  private static final String FURLS_URL                   = "furls_url";
  private static final String PPF_HELP_DESK_EMAIL         = "ppf_help_desk_email";
  private static final String PPF_HELP_DESK_EMAIL_SUBJECT = "ppf_help_desk_email_subject";
  private static final String APP_ERROR_ALERT_SWITCH      = "application_error_alert_switch";
  private static final String APP_ERROR_ALERT_TO          = "application_error_alert_to"; //to address.
  private static final String APP_ERROR_ALERT_SUBJECT     = "application_error_alert_subject";

  // ADDED on 1st Nov.2009
   /*----------------------------- JNDI PROPERTIES ---------------------------------------*/
  private final String JAVA_NAMING_FACTORY_INITIAL     = "java.naming.factory.initial";
  private final String JAVA_NAMING_PROVIDER_URL        = "java.naming.provider.url";
  private final String JAVA_NAMING_SECURITY_PRINCIPAL  = "java.naming.security.principal";
  private final String JAVA_NAMING_SECURITY_CREDENTIAL = "java.naming.security.credentials";
 
  
}
