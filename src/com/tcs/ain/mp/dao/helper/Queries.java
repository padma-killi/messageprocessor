package com.tcs.ain.mp.dao.helper;

/* $Revision: 1.1 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * Queries -  A java interface that defines all the SQL queries.
 * 
 * @version 1.00 01 Dec 2009
 * @author Padma Killi
 * @since 1.0
 */
 
public interface Queries 
{

    public static final String MAILING_LOG_INSERT_QUERY="INSERT INTO Mailings_Log(reg_nbr,mailing_id,mailing_type_name,created_date,xml_data,id,mailing_verb) VALUES(?,?,?,?,?,?,?)";
    public static final String REG_MAILING_UPDATE_MEDIACODE_QUERY="update registration_mailing set media_sendby =? where reg_nbr=? and mailing_type_id=? and receipt_code=?";
}
  