package com.tcs.ain.mp.helper;

public class MailLogVO 
{
  private String regnbr;
  private String mailingtypeid;
  private String mailingtypename;
  private String createddate;
  private String xmlobject;
  private String controlverb;
  private String mediacode;
  private String receiptcode;

  public MailLogVO()
  {
  }

  public String getRegnbr()
  {
    return regnbr;
  }

  public void setRegnbr(String newRegnbr)
  {
    regnbr = newRegnbr;
  }

  public String getMailingtypeid()
  {
    return mailingtypeid;
  }

  public void setMailingtypeid(String newMailingtypeid)
  {
    mailingtypeid = newMailingtypeid;
  }

  public String getMailingtypename()
  {
    return mailingtypename;
  }

  public void setMailingtypename(String newMailingtypename)
  {
    mailingtypename = newMailingtypename;
  }

  public String getCreateddate()
  {
    return createddate;
  }

  public void setCreateddate(String newCreateddate)
  {
    createddate = newCreateddate;
  }

  public String getXmlobject()
  {
    return xmlobject;
  }

  public void setXmlobject(String newXmlobject)
  {
    xmlobject = newXmlobject;
  }

  public String getControlverb()
  {
    return controlverb;
  }

  public void setControlverb(String newControlverb)
  {
    controlverb = newControlverb;
  }

  public String getMediacode()
  {
    return mediacode;
  }

  public void setMediacode(String newMediacode)
  {
    mediacode = newMediacode;
  }

  public String getReceiptcode()
  {
    return receiptcode;
  }

  public void setReceiptcode(String newReceiptcode)
  {
    receiptcode = newReceiptcode;
  }
}