package com.tcs.ain.mp.helper;

import java.io.Serializable;

/* $Revision: 1.3 $
 * Copyright � 2003  Global Net Services Inc
 * All Rights Reserved
 *
 * This is unpublished proprietary source code.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
*/

/**
 * Address -  A  java object that holds the address information
 * 
 * @version 2.00 8 April 2009
 * @author Padma Killi
 * 
 * @see java.io.Serializable
*/

public class Address implements Serializable
{
  private String countryName;
	private String addressTypeId;
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String zipCode;
  private String stateOrProvince;
	private String title;
  private String busPhCountryCode;
  private String busPhAreaCode;
  private String busPhNo;
  private String busPhExtn;
  private String faxPhCountryCode;
  private String faxPhAreaCode;
  private String faxPhNo;
  private String email;
  
  

	public Address(){}

	//accessor methods
  public String getCountryName()
  {
    return (this.countryName);
  }
  
	public String getAddressTypeId()
  {
		return (this.addressTypeId);
	}

	public String getName()
  {
		return (this.name);
	}

	public String getAddressLine1()
  {
		return (this.addressLine1);
	}

	public String getAddressLine2()
  {
		return (this.addressLine2);
	}

	public String getCity()
  {
		return (this.city);
	}

	public String getZipCode()
  {
		return (this.zipCode);
	}

  public String getStateOrProvince()
  {
    return (this.stateOrProvince);
  }
	public String getTitle()
  {
		return (this.title);
	}
  public String getBusPhCountryCode()
  {
    return (this.busPhCountryCode);
  }
  public String getBusPhAreaCode()
  {
    return (this.busPhAreaCode);
  }
  public String getBusPhNo()
  {
    return (this.busPhNo);
  }
  public String getBusPhExtn()
  {
    return (this.busPhExtn);
  }
  public String getFaxPhCountryCode()
  {
    return (this.faxPhCountryCode);
  }
  public String getFaxPhAreaCode()
  {
    return (this.faxPhAreaCode);
  }
  public String getFaxPhNo()
  {
    return (this.faxPhNo);
  }
  public String getEmail()
  {
    return (this.email);
  }
	//mutator methods
  public void setCountryName(String countryName)
  {
    this.countryName = countryName;
  }
 
	public void setAddressTypeId(String newAddressType)
  {
		this.addressTypeId = newAddressType;
	}
	
	public void setName(String name)
  {
		this.name = name;
	}

	public void setAddressLine1(String addressLine1)
  {
		this.addressLine1 = addressLine1;
	}

	public void setAddressLine2(String addressLine2)
  {
		this.addressLine2 = addressLine2;
	}

	public void setCity(String city)
  {
		this.city = city;
	}

	public void setZipCode(String zipCode)
  {
		this.zipCode = zipCode;
	}
	
  public void setStateOrProvince(String stateOrProvince)
  {
    this.stateOrProvince = stateOrProvince;
  }

	public void setTitile(String title)
  {
    this.title = title;
  }
  public void setBusPhCountryCode(String busPhCountryCode)
  {
    this.busPhCountryCode = busPhCountryCode;
  }
  public void setBusPhAreaCode(String busPhAreaCode)
  {
    this.busPhAreaCode = busPhAreaCode;
  }
  public void setBusPhNo(String busPhNo)
  {
    this.busPhNo = busPhNo;
  }
  public void setBusPhExtn(String busPhExtn)
  {
    this.busPhExtn = busPhExtn;
  }
  public void setFaxPhCountryCode(String faxPhCountryCode)
  {
    this.faxPhCountryCode = faxPhCountryCode;
  }
  public void setFaxPhAreaCode(String faxPhAreaCode)
  {
    this.faxPhAreaCode = faxPhAreaCode;
  }
  public void setFaxPhNo(String faxPhNo)
  {
    this.faxPhNo = faxPhNo;
  }
  public void setEmail(String email)
  {
    this.email  = email;
  }
  
}