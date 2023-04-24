package com.tcs.ain.mp.helper;

/* $Revision: 1.1.1.1 $
 * Copyright � 2003 Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 */

/**
 * StringUtil - Static string utilities
 * Lifecycle: all static methods, no members so no initialization or lifecycle concerns
 * @version 2.00 1 April 2003
 * @author Padma Killi
 */

import java.util.*;

public class StringUtil
{
    public static String capitalizeWords(String original)
    {
        int nmax = original.length();
        char [] buf = new char[nmax];
        original.getChars(0, nmax, buf, 0);

        for (int n = -1; n < nmax - 1; ++n)
        {
            if (n == -1 || buf[n] == ' ')
            {
                char c = buf[n+1];
                if (c >= 'a' && c <= 'z')
                {
                    buf[n+1] = (char)(c + ('A' - 'a'));
                }
            }
        }
        return new String(buf);
    }

    public static String capitalizeFirstWord(String original)
    {
    	if (original == null || original.length() == 0)
    	{
    		return original;
    	}
    	else if (original.length() == 1)
    	{
    		return original.toUpperCase();
    	}

    	return Character.toUpperCase(original.charAt(0)) + original.substring(1);
    }

	public static List splitLines(String string)
	{
		List result = new ArrayList(10);
		for (StringTokenizer t = new StringTokenizer(string, "\n"); t.hasMoreTokens();)
		{
			result.add(t.nextToken());
		}

		return result;
	}

	/**
	* Replaces the first instance of 'oldSubString' in 'string' with 'newSubString'
	*/
	public static String replaceSubstring(String string, String oldSubString, String newSubString)
	{
	    StringBuffer sb;
	    int pos;
	    String res;

	    sb  = new StringBuffer();
	    res = string;
	    pos =  string.indexOf(oldSubString);
	    if ( pos >= 0)
	    {
	        sb.append(string.substring(0, pos))
	        .append(newSubString)
	        .append(string.substring(pos + oldSubString.length(), string.length()))
	        ;
	        res = sb.toString();
	    }

	    return res;
	}

	/**
	 * Compare two possibly-null strings for equality
	 */
	public static boolean equals(String s1, String s2)
	{
		if (s1 == null && s2 == null)
		{
			return true;
		}
		if (s1 == null || s2 == null)
		{
			return false;
		}

		return s1.equals(s2);
	}

	/**
	 * Compare two possibly-null strings for equality, ignoring case
	 */
	public static boolean equalsIgnoreCase(String s1, String s2)
	{
		if (s1 == null && s2 == null)
		{
			return true;
		}
		if (s1 == null || s2 == null)
		{
			return false;
		}

		return s1.equalsIgnoreCase(s2);
	}

	/**
	 * If date has fractional seconds, trim them off
	 */
    public static String truncateDate(String longDate)
    {
    	if (longDate == null)
    	{
    		return "";
    	}
		else if (longDate.length() > 19)
		{
			return longDate.substring(0, 19);
		}
		else
		{
			return longDate;
		}
    }

    public static String [] trimAndRemoveBlankEntries(String [] original)
    {
    	List list = new ArrayList(original.length);
    	for (int n = 0; n < original.length; ++n)
    	{
    		String value = original[n];
    		if (value != null)
    		{
    			String trimmed = value.trim();
    			if (trimmed.length() > 0)
    			{
    				list.add(trimmed);
    			}
    		}
    	}

		return (String [])list.toArray(new String[list.size()]);
    }

	public static StringBuffer appendQuotedValue(StringBuffer sb, String string)
	{
		sb.append('\'');
		//System.out.println("Before escapes, string is <" + sb.toString() + ">");
		int i1 = 0;
		for (int i2 = string.indexOf('\'', i1); i2 != -1; i2 = string.indexOf('\'', i1))
		{
			sb.append(string.substring(i1, i2))
			    .append("''");
			i1 = i2+1;
		}
		
		sb.append(string.substring(i1));		
		sb.append('\'');
		//System.out.println("After escapes, string is <" + sb.toString() + ">");
		return sb;
	}	
	
	public static String [] copySetIntoArray(HashSet set)
	{
		String [] results = new String[set.size()];
		int nn = 0;
		for (Iterator i = set.iterator(); i.hasNext();)
		{
			results[nn++] = (String)i.next();
		}
		
		return results;
	}
	
  /** 
   * This method runs about 20 times faster than java.lang.String.toLowerCase,
   * and doesn't waste any storage when the result is equal to the input.
   *
   * Warning: Don't use this method when your default locale is Turkey
   * (seriously -- Turkey has special rules for lowercasing that this method ignores)
   *
   * java.lang.String.toLowerCase is slow because 
   *  (a) it uses a StringBuffer (which has synchronized methods), 
   *  (b) it initializes the StringBuffer to the default size, and 
   *  (c) it gets the default locale every time to test for name equal to "tr".
   * @author Peter Norvig, www.norvig.com 
   **/
	public static String toLowerCase(String str) 
	{
		int len = str.length();
		int different = -1;
		
		// See if there is a char that is different in lowercase
		for (int i = len-1; i >= 0; i--) 
		{
			char ch = str.charAt(i);
			if (Character.toLowerCase(ch) != ch) 
			{
				different = i;
				break;
			}
		}
		
		// If the string has no different char, then return the string as is,
		// otherwise create a lowercase version in a char array.
		if (different == -1)
		{
			return str;
		}
		else 
		{
			char[] chars = new char[len];
			str.getChars(0, len, chars, 0);
			
			// (Note we start at different, not at len.)
			for (int j = different; j >= 0; j--) 
			{
				chars[j] = Character.toLowerCase(chars[j]);
			}
			return new String(chars);
		}
	}
	
	public static final String escapeSpecials(String str)
	{
		String dispValue = str;
		
		for (int amploc = dispValue.indexOf('&'); amploc != -1; amploc = dispValue.indexOf('&', amploc+1))
		{
			dispValue = dispValue.substring(0, amploc) + "&amp;" + dispValue.substring(amploc+1);
//			System.out.println("Original string '" + str + "' is displayed as '" + dispValue + "'");
		}
		
		for (int quotloc = dispValue.indexOf('\''); quotloc != -1; quotloc = dispValue.indexOf('\'', quotloc+1))
		{
			dispValue = dispValue.substring(0, quotloc) + "&quot;" + dispValue.substring(quotloc+1);
		}
		
		for (int ltloc = dispValue.indexOf('<'); ltloc != -1; ltloc = dispValue.indexOf('<', ltloc+1))
		{
			dispValue = dispValue.substring(0, ltloc) + "&lt;" + dispValue.substring(ltloc+1);
		}
		
		for (int gtloc = dispValue.indexOf('>'); gtloc != -1; gtloc = dispValue.indexOf('>', gtloc+1))
		{
			dispValue = dispValue.substring(0, gtloc) + "&gt;" + dispValue.substring(gtloc+1);
		}
		return dispValue;
    }
    
    static public final String[] vectorToArray(Vector vector)
    {
    	String [] results = new String[vector.size()];
    	vector.copyInto(results);
    	return results;
    }    
        
    static public final String [] listToArray(List list)
    {
    	return (String [])list.toArray(new String[list.size()]);
    }
    
    static public String arrayToQuotedCommaSeparated(String [] values)
    {
    	StringBuffer sb = new StringBuffer();
    	for (int nv = 0; nv < values.length; ++nv)
    	{
    		if (nv == 0)
    		{
    			sb.append("'");
    		}
    		else
    		{
    			sb.append("','");
    		}
    		sb.append(values[nv]);
    	}
    	sb.append("'");
    	return sb.toString();
    }
    
    /**
     * Converts a millisecond number to days+h:mm:s
     */
    public static String toDHMS(long millis)
    {
    	StringBuffer sb = new StringBuffer();
    	
    	if (millis < 0)
    	{
    		sb.append("minus ");
    		millis = -millis;
    	}
    	
    	long secs = millis/1000;
        long days	= secs/60/60/24;
        secs -=		 (days*60*60*24);
        long hours	= secs/60/60;
        secs -= 	(hours*60*60);
    	long mins	= secs/60;
    	secs -= 	 (mins*60);
    	
    	if (days > 0)
    	{
    		sb.append(days)
    		  .append('+');
    	}
    	
   		sb.append(hours)
   		  .append(':');
   		  
   		if (mins <= 9)
   		{
   			sb.append('0');
   		}   		
   		sb.append(mins)
   		  .append(':');
   		
   		if (secs <= 9)
   		{
   			sb.append('0');
   		}   		
   		sb.append(secs);
    
    	return sb.toString();
    }   
    
    /**
     * Converts a millisecond number to days+h:mm:s
     */
    public static String toDHMConcise(long millis)
    {
    	StringBuffer sb = new StringBuffer();
    	
    	if (millis < 0)
    	{
    		sb.append("minus ");
    		millis = -millis;
    	}
    	
    	long secs = millis/1000;
        long days	= secs/60/60/24;
        secs -=		 (days*60*60*24);
        long hours	= secs/60/60;
        secs -= 	(hours*60*60);
    	long mins	= secs/60;
    	
    	if (days > 0)
    	{
    		sb.append(days)
    		  .append('+');
    	}
    	
   		sb.append(hours)
   		  .append(':');
   		  
   		if (mins <= 9)
   		{
   			sb.append('0');
   		}   		
   		sb.append(mins);
   		
    	return sb.toString();
    }   
    
    /**
     * Converts a millisecond number to "n days, n hours, n minutes
     */
    public static String toDHM(long millis)
    {
        StringBuffer sb = new StringBuffer();
        
        if (millis < 0)
        {
            millis = - millis;
            sb.append("minus ");
        }
        
        long mins = millis/1000/60;        
        long days	= mins/60/24;
        mins -=	(days*60*24);
        long hours	= mins/60;
        mins -= (hours*60);
        
        if (days > 0)
        {
        	sb.append(days)
        	  .append(" days, ");
        }
        
        if (hours > 0)
        {
        	sb.append(hours)
        	  .append(" hours, ");
        }
        
        sb.append(mins)
          .append(" minutes");

        return sb.toString();
    }    
    
    public static String removeNewlines(String original)
    {
        if (original == null)
        {
            return original;
        }
        return original.replace('\r', ' ').replace('\n', ' ');
    }
    
    public static String makeNonBlank(String original)
    {
        if (original.length() == 0)
        {
            return "&nbsp;";
        }
        
        return original;
    }
    
	public static void appendSortedSet(StringBuffer sb, Set set, String itemSeparator)
	{
		String [] values = new String[set.size()];
		int nvmax = 0;
		for (Iterator it = set.iterator(); it.hasNext();)
		{
			values[nvmax++] = (String)it.next();
		}
		Arrays.sort(values);
		for (int n = 0; n < values.length; ++n)
		{
			if (n > 0)
			{
				sb.append(itemSeparator);
			}
			sb.append(values[n]);
		}
	}


    /**
    *Method to parse through single quotes and double quotes
    */
	public  static String getParsedValue(String value)
	{
        String trimmedvalue = value.trim();
        
        if (trimmedvalue.length() == 0)
        {
            return new String("null");
        }
        else 
        {
           
	        StringBuffer sb = new StringBuffer();
	        int i1 = 0;
	        for (int i2 = trimmedvalue.indexOf('\'', i1); i2 != -1; i2 = trimmedvalue.indexOf('\'', i1))
	        {
		            sb.append(trimmedvalue.substring(i1, i2)).append("''");
		            i1 = i2+1;
	        }
	        sb.append(trimmedvalue.substring(i1));
	            return new String(sb.toString());

        }
        //return new String("");
    }
}