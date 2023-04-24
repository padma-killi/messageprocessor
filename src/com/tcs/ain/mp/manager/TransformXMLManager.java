package com.tcs.ain.mp.manager;

import com.tcs.ain.mp.common.XMLTransformException;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.io.*;

import com.tcs.ain.mp.helper.FileHandler;

/**
 * TransformXMLManager implements ITransformXML. This class is mainly used to transform
 * XML documents into an HTML documents and vice versa.
 * 
 * @version 2.0 09 Sep 2003
 * @author :Padma Killi
 * Modified Date:12 April 2009
 * 
 * @see ITransformXML
 */
public class TransformXMLManager implements ITransformXML
{
  private static final String XSL_FILE_NAME              ="ppf-interface.xsl";
  //private final String HTML_FILE_NAME             ="registration.html";
  
  public TransformXMLManager()
  {
  }

  /**
   * The API transformXMLintoHTML is used to transform XML into HTML.
   * 
   * The API XSLCustomization(String message) is used to customize the XML message into HTML using
   * XSLT transformation API's before we send the email.
   * The following example shows how to use JAXB to marshal a document for transformation by XSLT. 
   * @param xmlMessage - XML message.
   */
  public String transformXMLintoHTML(String xmlMessage,String fileToWrite)
  {
      FileReader fr     = null;
      BufferedReader br = null;
      StringReader sr   = null;
      FileOutputStream fo = null;
      String success ="SUCCESS";
      
      try
      {
          // set up XSLT transformation
          TransformerFactory tf = TransformerFactory.newInstance();

          //br = new BufferedReader(new FileReader("C:/Development/PPF/src/gov/fda/furls/mp/manager/ppf-interface.xsl"));
          fr = new FileReader(ResourceManager.getInstance().getXslFilePath() + XSL_FILE_NAME);          
         // br = new BufferedReader(new FileReader(ResourceManager.getInstance().getXslFilePath() + XSL_FILE_NAME));
          if (fr != null)
          {
              br = new BufferedReader(fr);
              Transformer t = tf.newTransformer(new StreamSource(br));

              // run transformation
             // t.transform( new StreamSource( new StringReader( xmlMessage.toString() ) ),new StreamResult(System.out));
              sr = new StringReader( xmlMessage.toString());
              fo = new FileOutputStream(ResourceManager.getInstance().getHtmlWritePath()+ fileToWrite);
             // t.transform( new StreamSource( new StringReader( xmlMessage.toString() ) ),new StreamResult(new FileOutputStream(ResourceManager.getInstance().getHtmlWritePath()+ HTML_FILE_NAME)));
             t.transform( new StreamSource( sr ),new StreamResult(fo));

             System.out.println("XML - HTML Transformation Successful..and filename created is :"+fileToWrite);
          }
          else
          {
             success = "ERROR";
             System.out.println("Error: TransformXMLManager.transformXMLintoHTML() ..Unable to locate the file");
          }
          
      }
      catch(TransformerConfigurationException tce)
      {
         success = "ERROR";
         tce.printStackTrace();
         System.out.println("transformXMLintoHTML() error in Transformer Configuration");
      }
      catch (TransformerException te)
      {
        success = "ERROR";
        System.out.println("transformXMLintoHTML() error in transforming XML ");
        te.printStackTrace();
      }
      catch (FileNotFoundException fne)
      {
        success = "ERROR";
        fne.printStackTrace();
        System.out.println("transformXMLintoHTML() error in finding the  XSL file (ppf-interface.xsl)");
      }
      finally
      {
           try 
           {
              fr.close();
              br.close();
              sr.close();
              fo.close();
           }
           catch(Exception e)
           {
              e.printStackTrace();
              System.out.println("transformXMLintoHTML() unable to close the BufferedReader");
           }
      }

      return success;
  }

  /**
   * The API removeFile is used to remove the file that was created as part of the transformation.
   * @param fileName - The file to delete.
   * @throws XMLTransformException
   */
   public boolean removeFile(String fileName)
     throws XMLTransformException
   {
      //Get the HTML path
      String path = ResourceManager.getInstance().getHtmlWritePath();
      boolean isdeleted = false;
      
     // File file = new File(path+"registration.html");
      File file = new File(path+fileName);
     
    
      if (file.exists())
      {   
          isdeleted =   file.delete();
          System.out.println( "Is File Exist deleted :" + isdeleted);
      }
      //System.out.print("The Length of the file :"+file+" is :"+file.length());
     
      return isdeleted;
   }

   
   /**
    * The main API to test the functionlity of this class.
    */
   public static void main(String[] args)
   {
        try {
           TransformXMLManager tmanager = new TransformXMLManager();
           String message = FileHandler.readFile("C:/Development/PPF/src/gov/fda/furls/mp/manager/ppf-interface3.xml");
           tmanager.transformXMLintoHTML(message,"registration.html");
        }catch(Exception e) {e.printStackTrace();}
    }
}
