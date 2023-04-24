package com.tcs.ain.mp.helper;
import java.io.*;

/* $Revision: 1.2 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * FileHandler -  A java class that handles the file reading.

 * @version 2.00 09 June 2003
 * @author Padma Killi
*/

public class FileHandler {

    /**
     * Read a text file into a String.
     */
    public static String readFile(String filename)
        throws FileNotFoundException, IOException {

        String line = "";
        StringBuffer content = new StringBuffer();
        
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        content.append("");
        while ((line = reader.readLine()) != null) {
            content.append(getParsedValue(line));
            content.append("\n");
        }
        content.append("");
        reader.close();
        return content.toString();
    }
	
    public static void writeFile(String data,String filename)
        throws IOException {

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
        writer.print(data);
        writer.close();
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
