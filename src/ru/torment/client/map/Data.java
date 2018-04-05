package ru.torment.client.map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"value"} )
public class Data
{
	@XmlAttribute(name = "encoding")
	protected String encoding;
	@XmlValue
	protected String value;

	// Getters
	public String getEncoding() { return encoding; }
	public String getValue()    { return value;    }

	// Setters
	public void setEncoding( String encoding ) { this.encoding = encoding; }
	public void setValue(    String value    ) { this.value    = value;    }

	@Override
	public String toString()
	{
		return encoding + value;
	}
}
