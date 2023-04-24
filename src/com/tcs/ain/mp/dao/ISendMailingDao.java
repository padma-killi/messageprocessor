package com.tcs.ain.mp.dao;
import com.tcs.ain.mp.common.TransactionFailedException;

/* $Revision: 1.1 $
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * iSendMailingDao -  An interface that has the various API's for FFRM mailings
 * 
 * @version 1.00 01 Dec 2009
 * @author Padma Killi
 * @see
*/

public interface ISendMailingDao 
{
    /**
     * The API insertMailingLog() is used to insert record into Mailing_logs.
     *
     * @param obj - The TransactionVO object.
     * @throws TransactionFailedException
     * @since 1.0
     */
    public void insertMailingLog(Object obj) throws TransactionFailedException;
     public void updateRegMailing(Object obj) throws TransactionFailedException;
}
