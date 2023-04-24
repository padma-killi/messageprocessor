package com.tcs.ain.mp.manager;

import com.tcs.ain.mp.common.XMLTransformException;

/**
 * ITransformXML - A java interface that is used to transform XML into HTML documents
 * @author : Padma Killi
 * @version 2.00 09 Sep 2003
 * Modified on :12 April 2009
 * 
 * @see XMLTransformException
 */

public interface ITransformXML 
{
   public String transformXMLintoHTML(String xmlMessage,String fileToWrite);
   public boolean removeFile(String fileName) throws XMLTransformException;
}
