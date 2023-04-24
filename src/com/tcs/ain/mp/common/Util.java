package com.tcs.ain.mp.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

/* $Revision: 1.2 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * Util -  A java class that has all the utility API's needed for message processor component.

 * @version 2.00 09 June 2003
 * @author Padma Killi
*/

public final class Util 
{
  public Util(){}

  private static final int REG_DIGITS = 9;
  private static final int WEIGHT_START = 15;
  
  /**
  * Get the current timestamp
  */
  public static String getTimestamp()
  {
     java.util.Date dt = new java.util.Date();
     Timestamp ts = new Timestamp(dt.getTime());
     return (ts.toString());
  }

  /**
  * Get the date in String format
  */
  public static String getDate( String str, int format )
  {
        // format = 1 or '< 1' or '> 6' --> MM/DD/YYYY
        // format = 2 --> DD/MM/YYYY
        // format = 3 --> MM/DD/YYYY 12:00 AM
        // format = 4 --> MM/DD/YYYY 24:00
        // format = 5 --> MM/DD/YY
        // format = 6 --> DD/MM/YY
        String separator = "/" ;
        String retStr = "" ;
        String monthStr = "" ;
        String dateStr = "" ;
        String yearStr = "" ;
        String timeStr = "" ;

        HashMap monthMap = new HashMap();
        monthMap.put("01", "January");
        monthMap.put("02", "February");
        monthMap.put("03", "March");
        monthMap.put("04", "April");
        monthMap.put("05", "May");
        monthMap.put("06", "June");
        monthMap.put("07", "July");
        monthMap.put("08", "August");
        monthMap.put("09", "September");
        monthMap.put("10", "October");
        monthMap.put("11", "November");
        monthMap.put("12", "December");
        
        try
        {
            if( str == null || str.equals("") )
            {
                return retStr ;
            }
            else
            {
                String s = str.replace('-', ' ' ) ;
                StringTokenizer st = new StringTokenizer( s ) ;
                yearStr  = st.nextToken() ;
                monthStr = st.nextToken() ;
                dateStr  = st.nextToken() ;
                timeStr  = st.nextToken() ;
                timeStr  = timeStr.substring(0,8) ;
                if( format == 1 || format < 1 || format > 7 )
                {
                    retStr = monthStr + separator + dateStr + separator + yearStr ;
                }
                else if( format == 2 )
                {
                    retStr = dateStr + separator + monthStr + separator + yearStr ;
                }
                else if( format == 3 )
                {
                    StringTokenizer stt = new StringTokenizer( timeStr, ":" ) ;
                    String ampmStr = "" ;
                    int hour = Integer.parseInt( stt.nextToken() ) ;
                    int min  = Integer.parseInt( stt.nextToken() ) ;
                    int sec  = Integer.parseInt( stt.nextToken() ) ;
                    if( hour > 12 )
                    {
                        hour = hour - 12 ;
                        if( hour == 12 )
                        {
                            if( min > 0 )
                                ampmStr = "AM" ;
                            else
                                ampmStr = "PM" ;
                        }
                        else
                            ampmStr = "PM" ;
                    }
                    else if ( hour < 12 )
                    {
                        ampmStr = "AM" ;
                    }
                    else
                    {
                        if( min > 0 )
                            ampmStr = "PM" ;
                        else
                            ampmStr = "AM" ;
                    }
                
                    retStr = monthStr + separator + dateStr + separator + yearStr 
                                     + " " + ( hour < 10 ? "0"+hour : ""+hour ) + ":" 
                                           + ( min < 10 ? "0"+min : ""+min ) + ":" 
                                           + ( sec < 10 ? "0"+sec : ""+sec ) + " " 
                                           + ampmStr ;
                }
                else if( format == 4 )
                {
                    retStr = monthStr + separator + dateStr + separator + yearStr 
                                     + " " + timeStr ;
                }
                else if( format == 5 )
                {
                    retStr = monthStr + separator + dateStr + separator + yearStr.substring(0,2) ;
                }
                else if( format == 6 )
                {
                    retStr = dateStr + separator + monthStr + separator + yearStr.substring(0,2) ;
                }
                else if( format == 7 )
                {
                  //get the month from the hashmap corresponding to the parsed monthStr
                  String month = (String)monthMap.get(monthStr);
                  retStr  = month +" "+ dateStr +", "+yearStr;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace() ;
            return str ;
        }
        return retStr ;
  }

   /**
   * This method is used to generate an unique alpha-numeric code 
   * @return String
   */

   public static String generateUniqueCode() {
        //generate the weights and store them in an array
        int[] weights = new int[REG_DIGITS+2];
        //int[] regNbr = new int[REG_DIGITS+2]; //not required
        int[] chksum = new int[REG_DIGITS+2];
        int sum = 0;
        int nbr = 0;
        String nbrstr = new String();

        for(int i = 0;i <=(9-1); i++) {
            //generate numbers for each location, weights are in the sequence, n+3,n+5,n+7, so on
            weights[i] = (WEIGHT_START + (2*i + 3));

            //System.out.println("weights " + weights[i]);
            
            //first digit of the  number is always 1
            if(i == 0) {
                //regNbr[i] = 1;
                //System.out.println("regNbr " + regNbr[i]);
                //registration number always begins with a '1'
                nbr = 1;
                //calculate chksum
                chksum[i] = weights[i]*nbr;
                //System.out.println("chksum " + chksum[i]);
                sum += chksum[i];
                //System.out.println("sum " + sum);

                //convert nbr to string
                Integer tempint = new Integer(nbr);
                nbrstr = tempint.toString();
                //System.out.println("nbrstr " + nbrstr);
            }
            else {
                //generate a random number between 0-9
                nbr = (int)((Math.random()*10)%(10));
                //System.out.println("nbr " + nbr);

                //calculate chksum
                chksum[i] = weights[i]*nbr;
                //System.out.println("chksum " + chksum[i]);
                sum += chksum[i];
                //System.out.println("sum " + sum);

                //convert nbr to string
                Integer tempint = new Integer(nbr);
                nbrstr = nbrstr.concat(tempint.toString());
                //System.out.println("nbrstr " + nbrstr);
            }
        } //end for
        //generate the actual number

        //isolate the last 2 digits
        Integer tempsum = new Integer(sum);
        String str = tempsum.toString();
        //System.out.println("str " + str + "str.length() " + str.length());
        Character temp = new Character(str.charAt(str.length()-2));
        nbrstr = nbrstr.concat(temp.toString());
        //System.out.println("nbrstr " + nbrstr);
        //System.out.println("str " + str + "str.length() " + (str.length()));
        Character temp1 = new Character(str.charAt(str.length()-1));
        nbrstr = nbrstr.concat(temp1.toString());
        //System.out.println("nbrstr " + nbrstr);

        //generate Long
        Long number = new Long(nbrstr);
       // System.out.println("Generated No: " + number);
        return (number.toString());
    }//end generate no
    
   /**
    * This API returns a date with the no. of days being added to the current date.
    * @param days - The no. of days to be added.
    * @return String - The formatted date.
    */
    public static String getFutureDate(int days)
    {
      // Create a Calendar object:
      Calendar dteDate = Calendar.getInstance();
      //System.out.println("Current Date :"+dteDate.getTime());
      //add no of days and display
      dteDate.add(dteDate.DAY_OF_YEAR ,days);
      java.util.Date dteDay = dteDate.getTime();

    	SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
      String strDay = formatter.format(dteDay);

      return (strDay);
    }
    /**
     * This API checks to see whether an year is leapyear or not
     * @param year
     */
    public static boolean isLeapYear(int year)
    {
        int y = year;
        boolean isLeapYear;
      
        //Divisible by 4 but not 100
        isLeapYear = (y % 4 == 0) && (y % 100 != 0);
      
        //or divisible by 400
        isLeapYear = isLeapYear || (y % 400 == 0);
      
        return(isLeapYear);
    }
}