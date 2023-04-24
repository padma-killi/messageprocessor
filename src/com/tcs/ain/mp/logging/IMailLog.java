package com.tcs.ain.mp.logging;

import com.tcs.ain.mp.helper.MailLogVO;
import gov.fda.furls.mp.helper.*;
public interface IMailLog 
{


  public void insertIntoMailLogger(MailLogVO maillogvo );
  
}