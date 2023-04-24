package com.tcs.ain.mp.dao.impl;

import com.interface21.dao.DataAccessException;
import com.interface21.jdbc.object.SqlQuery;
import com.tcs.ain.mp.common.TransactionFailedException;
import com.tcs.ain.mp.dao.ISendMailingDao;
import com.tcs.ain.mp.helper.DataSourceHelper;
import com.tcs.ain.mp.dao.helper.TransactionInsertQuery;
import com.tcs.ain.mp.dao.helper.TransactionUpdateQuery;
import com.tcs.ain.mp.dao.helper.Constants;

import javax.sql.DataSource;

public class SendMailingDaoImpl implements ISendMailingDao
{
      /**
       * The API insertFMLSTransaction() is used to insert into FMLS Integration Transaction Log.
       * @param obj - The TransactionVo Object.
       * @throws TransactionFailedException.
       * @since 1.0
       */
      public void insertMailingLog(Object obj) throws TransactionFailedException
      {
        TransactionInsertQuery mailingTransactionInsertQuery  = null;
        DataSource ds = DataSourceHelper.getDataSource1();
        mailingTransactionInsertQuery = new TransactionInsertQuery(ds,Constants.MAILING_LOG_INSERT);
        mailingTransactionInsertQuery.saveObject(obj, Constants.MAILING_LOG_INSERT);
        System.out.println("******* Done Inserting mailing log record ******** ");
      }  

      public void updateRegMailing(Object obj) throws TransactionFailedException 
      {
        TransactionUpdateQuery mailingTransactionUpdateQuery = null;
        DataSource ds = DataSourceHelper.getDataSource1();
        mailingTransactionUpdateQuery = new TransactionUpdateQuery(ds,Constants.REG_MAILING_UPDATE_MEDIACODE);
        mailingTransactionUpdateQuery.saveObject(obj,Constants.REG_MAILING_UPDATE_MEDIACODE);
      }

}