package com.tcs.ain.mp.logging.impl;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.util.Date;
import java.text.DateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.naming.Context;
import java.io.IOException;


import com.tcs.ain.mp.logging.IMailLog;
import com.tcs.ain.mp.common.TransactionFailedException;
import com.tcs.ain.mp.common.IErrorMessages;
import com.tcs.ain.mp.helper.MailLogVO;
import com.tcs.ain.mp.helper.DataSourceHelper;

public class MailLoggingServices implements IMailLog
{
  public MailLoggingServices() 
  {
  }

  public void insertIntoMailLogger(MailLogVO maillogvo) 
  {

        Connection conn = null;
     PreparedStatement pstmt  = null;
     Statement stmt = null;
     ResultSet rset = null;
     Context ctx  = null;
     BufferedWriter out = null;
     Long id = null;
     try {
         System.out.println("Starting to insert in Mailings_log table ");

         id = getNextId2("mailings_log");
         
         conn = DataSourceHelper.getConnection1();
         stmt = conn.createStatement();
         StringBuffer sb = new StringBuffer();
         String regno = maillogvo.getRegnbr();
         Long lg = new Long(regno);
         sb.append("insert into mailings_log(reg_nbr,mailing_id,mailing_type_name,created_date,xml_data,id,mailing_verb) values");
         sb.append(" ( ");
         sb.append(regno);
         sb.append(" , ");
         sb.append(" '");
         sb.append(maillogvo.getMailingtypeid());
         sb.append("'");
         sb.append(" , ");
         sb.append("'");
         sb.append(maillogvo.getMailingtypename());
         sb.append("'");
         sb.append(" , ");
  //       sb.append("'");
         sb.append("SYSDATE");
    //     sb.append("'");
         sb.append(" , ");
         sb.append("EMPTY_CLOB()");
         sb.append(" , ");
         sb.append(id); 
         sb.append(" , ");
         sb.append("'");
         sb.append(maillogvo.getControlverb() );
         sb.append("'");
         sb.append(")");

         int rows = stmt.executeUpdate(sb.toString());
         System.out.println("THE SQL IS " + sb.toString() );

         String query = "SELECT xml_data FROM mailings_log where id = " + id;
         rset = stmt.executeQuery(query);

         while(rset.next())
         {
              oracle.sql.CLOB clob = (oracle.sql.CLOB)rset.getClob("XML_DATA");
              out = new BufferedWriter(clob.getCharacterOutputStream());
              out.write(maillogvo.getXmlobject() );
              out.close();
          }
         System.out.println("INSERTED INTO THE MAILING LOG FILE");

      }
       catch (IOException ie)
       {
          ie.printStackTrace();
          System.out.println( "ERROR IN INSERTING TO MAILING LOG DUE TO IO EXCEPTION :"+DateFormat.getDateTimeInstance().format(new Date())+" - "+IErrorMessages.IO_EXCEPTION_INSERT_MAILING_LOG+" - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid() );
       }
       catch (SQLException sqle)
       {
            sqle.printStackTrace();
            System.out.println( "ERROR IN INSERTING TO MAILING LOG DUE TO SQL EXCEPTION :"+DateFormat.getDateTimeInstance().format(new Date())+" - "+IErrorMessages.SQL_EXCEPTION_INSERT_MAILING_LOG+" - - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid() );
       }
       catch (Exception e)
       {
            e.printStackTrace();
            System.out.println( "ERROR IN INSERTING TO MAILING LOG DUE TO  EXCEPTION :"+DateFormat.getDateTimeInstance().format(new Date())+" - "+IErrorMessages.EXCEPTION_INSERT_MAILING_LOG+" - - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid() );
       }
         finally {
              try { if (out != null) {out.close();System.out.println("insertIntoMailingLog(). BufferedWriter is closed");}}
                 catch (IOException ie){
                  ie.printStackTrace();
                  System.out.println("ERROR - insertIntoMailingLog() - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid());
                 }
              try { if (rset != null) {rset.close();System.out.println("insertIntoMailingLog(). Resultset is closed");}}
                catch (SQLException se){
                 se.printStackTrace();
                 System.out.println("ERROR - insertIntoMailingLog() - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid());
                }
              try { if (stmt != null) {stmt.close(); System.out.println("insertIntoMailingLog(). Statement is closed");}}
                catch (SQLException se) {
                  se.printStackTrace();
                  System.out.println("ERROR - insertIntoMailingLog() - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid());
                }
              try { if (conn != null) {conn.close(); System.out.println("insertIntoMailingLog(). Connection is closed"); }}
                catch (SQLException se) {
                  se.printStackTrace();
                  System.out.println("ERROR - insertIntoMailingLog() - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid());
                }
            }

  }
   public static Long getNextId2(String tablename) throws TransactionFailedException
  {
         String query="";
         Long id = null;//new Long(100000L);
         Connection conn = null;
         PreparedStatement pst  = null;
         ResultSet rs = null;

         if ("mailings_log".equals(tablename))
         {
           query="SELECT MAILING_LOG_ID_SEQ.nextval FROM dual";
         }

        try
        {
          conn = DataSourceHelper.getConnection1();      
          System.out.println(query);
          pst = conn.prepareStatement(query);
          rs = pst.executeQuery();
          if (rs.next())
       	  {
         		id = getLong((rs.getLong(1)));
       	  }
        }
        catch (SQLException e)
        {
          e.printStackTrace();
          System.err.println("SQLHelper.getNextId().."+e.getMessage());
        }
        finally
        {
          try { if (rs != null) rs.close(); }
          catch (SQLException e) { }
          try { if (pst != null) pst.close(); }
          catch (SQLException e) { }
          try { if (conn != null) conn.close(); }
          catch (SQLException e) { }
        }
      System.out.println("The Id generated is :"+id+" and the tablename is :"+tablename);
      return id;
      
  }
   private static Long getLong( long longVal )
  {
        if( new Long(longVal) == null ) return new Long(-1L);
        else return new Long(longVal) ;
  }
}