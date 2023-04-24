package com.tcs.ain.mp.dao.helper;

import com.interface21.jdbc.core.SqlParameter;
import com.interface21.jdbc.object.SqlUpdate;
import javax.sql.DataSource;

import com.tcs.ain.mp.helper.MailLogVO;
//import gov.fda.furls.dbp.dao.vo.TransactionVO;
import java.sql.Types;

/* $Revision: 1.1 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * TransactionUpdateQuery -  This class houses the logic to update FMLS Integration Schema from FURLS.
 * 
 * @version 1.00 20 May 2009
 * @author Padma Killi
*/


public class TransactionUpdateQuery extends SqlUpdate implements Constants,Queries{
  /**
   * TransactionUpdateQuery Constructor.
   * @param ds - The DataSource object
   * @param whichObject - This will help identify which update tranaction query to be used.
   * @since 1.0
   */
  public TransactionUpdateQuery(DataSource ds,int whichObject) {
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

           if ( REG_MAILING_UPDATE_MEDIACODE == whichObject ) {
                setSql(REG_MAILING_UPDATE_MEDIACODE_QUERY);
                declareParameter(new SqlParameter(Types.VARCHAR));
                declareParameter(new SqlParameter(Types.NUMERIC));
                declareParameter(new SqlParameter(Types.NUMERIC));
                declareParameter(new SqlParameter(Types.VARCHAR));
            }
  }
  /**
   *
   * @param o Lookup Object
   * @param whichObject - A Constant indicating which Lookup Object
   * @param ds - A DataSource Object
   * @since 1.0
   */
  public void saveObject(Object o,int whichObject)  {
        if ( REG_MAILING_UPDATE_MEDIACODE == whichObject ){
            MailLogVO mailVO = (MailLogVO) o ;
            String mediacode1 = mailVO.getMediacode();
            String regnbr1 = mailVO.getRegnbr();
            String mailingid1 = mailVO.getMailingtypeid();
            String receiptcode1 = mailVO.getReceiptcode();
            String mediacode = null;
            String regnbr = null;
            String mailingid = null;
            String receiptcode = null;
            if (mediacode1 != null) 
            {
              mediacode = mediacode1.trim();
            }
            if(regnbr1 != null) 
            {
              regnbr = regnbr1.trim();
            }
            if(mailingid1 != null) 
            {
              mailingid = mailingid1.trim();
            }
            if(receiptcode1 != null) 
            {
              receiptcode = receiptcode1.trim();
            }
            System.out.println("The media code is " + mailVO.getMediacode() );
            System.out.println("The regnbr is " + mailVO.getRegnbr() );
            System.out.println("The mailing id is " + mailVO.getMailingtypeid() );
            System.out.println("The receipt code is " + mailVO.getReceiptcode() );
            update( new Object[] {mediacode,regnbr,mailingid,receiptcode } );
        }
       
  }        
}


