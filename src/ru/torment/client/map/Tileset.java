package ru.torment.client.map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "" )
public class Tileset
{
	@XmlAttribute(name = "firstgid")
	protected int firstgid;
	@XmlAttribute(name = "source")
	protected String source;

	// Getters
	public int    getFirstgid() { return firstgid; }
	public String getSource()   { return source;   }

	// Setters
	public void setFirstgid( int    firstgid ) { this.firstgid = firstgid; }
	public void setSource(   String source   ) { this.source   = source;   }

	@Override
	public String toString()
	{
		return firstgid + " " + source;
	}
}
