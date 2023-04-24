package com.tcs.ain.mp.dao.helper;

import com.interface21.jdbc.core.SqlParameter;
import com.interface21.jdbc.object.SqlUpdate;
import java.io.BufferedWriter;
import java.sql.Connection;
import java.util.Date;
import java.text.DateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.naming.Context;
import java.sql.Types;
import javax.sql.DataSource;
import java.io.IOException;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import com.tcs.ain.mp.common.IErrorMessages;
import com.tcs.ain.mp.common.TransactionFailedException;
import com.tcs.ain.mp.helper.MailLogVO;


/* $Revision: 1.2 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * TransactionInsertQuery -  This class houses the logic to insert into FURLS schema.
 *
 * @version 1.00 01 December 2009
 * @author Padma Killi
 */

public class TransactionInsertQuery extends SqlUpdate implements Constants,Queries
{
  //protected final Log logger = LogFactory.getLog(getClass().getName());
  public TransactionInsertQuery(DataSource ds, int whichObject){
         setDataSource(ds);
         setParameters(whichObject);
         compile();
  }
  /**
   * The API setParameters() is called from this class's constructor to set the parameters
   * for the "WHERE" condition of the SQL.
   * @param whichObject - This will help identify which update tranaction query to be used.
   * @since 1.0
   */
  public void setParameters(int whichObject) {
            if ( MAILING_LOG_INSERT == whichObject ){
                setSql(MAILING_LOG_INSERT_QUERY);
                declareParameter(new SqlParameter(Types.NUMERIC));  //reg_nbr
                declareParameter(new SqlParameter(Types.VARCHAR));  //mailing id
                declareParameter(new SqlParameter(Types.VARCHAR));  //mailing name
                declareParameter(new SqlParameter(Types.DATE));    //date
                declareParameter(new SqlParameter(Types.CLOB));     //transaction_xml
                declareParameter(new SqlParameter(Types.NUMERIC));  //sequence id
                declareParameter(new SqlParameter(Types.VARCHAR));  //mailing verb
            }
  }
   /**
   *
   * @param o Lookup Object
   * @param whichObject  Constant indicating which Lookup Object
   * @param ds  DataSource Object
   * @since 1.0
   */
  public void saveObject(Object o,int whichObject) throws TransactionFailedException {

     if ( MAILING_LOG_INSERT == whichObject ){
            MailLogVO mailVO = (MailLogVO) o ;

            System.out.println("saveObject() - Reg Nbr :"+mailVO.getRegnbr());
            System.out.println("saveObject() - Mailing Type :"+mailVO.getMailingtypeid());
            System.out.println("saveObject() - Mailing type name :"+mailVO.getMailingtypename());
            System.out.println("saveObject() - Created Date :"+mailVO.getCreateddate());
            System.out.println("saveObject() - Is Transaction XML null :"+(mailVO.getXmlobject()==null));
            System.out.println("saveObject() - ID :" );
            System.out.println("saveObject() - Mailing verb : " + mailVO.getControlverb() );

            insertIntoMailingLog(mailVO);
        }
  }

   /**
   * The API insertIntoMailingLog() is used to insert the new maling transaction info into
   * Mailings_Log table.
   * @param mailVO - The Transaction Object.
   * @throws DataAccessException.
   * @since 1.0
   */
  private void insertIntoMailingLog(MailLogVO maillogvo) throws TransactionFailedException
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

         id = getNextId("mailings_log");
         
          DataSource dataSource = getDataSource();
          conn = getDataSource().getConnection();
        //  conn.setAutoCommit(false);
          stmt = conn.createStatement();
//         conn = DataSourceHelper.getConnection();
//         stmt = conn.createStatement();
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
        // conn.commit(); 
         System.out.println("INSERTED INTO THE MAILING LOG FILE");

      }
       catch (IOException ie)
       {
          ie.printStackTrace();
          System.out.println( "ERROR IN INSERTING TO MAILING LOG DUE TO IO EXCEPTION :"+DateFormat.getDateTimeInstance().format(new Date())+" - "+ IErrorMessages.IO_EXCEPTION_INSERT_MAILING_LOG+" - Reg Nbr :"+maillogvo.getRegnbr()+" - Mailing Type :"+maillogvo.getMailingtypeid() );
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

  public  Long getNextId(String tablename) throws TransactionFailedException
  {
         String query="";
         Long id = null;//new Long(100000L);
         Connection conn = null;
         PreparedStatement pst  = null;
         ResultSet rs = null;

         if ("mailings_log".equals(tablename))
         {
           query="SELECT MAILINGS_LOG_ID_SEQ.nextval FROM dual";
         }

        try
        {
          DataSource dataSource = getDataSource();
          conn = getDataSource().getConnection();
          //stmt = conn.createStatement();
          //conn = DataSourceHelper.getConnection();      
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