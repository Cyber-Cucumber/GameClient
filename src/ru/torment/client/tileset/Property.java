package ru.torment.client.tileset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property")
public class Property
{
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "type")
	protected String type;
	@XmlAttribute(name = "value")
	protected String value;

	public String getName()  { return name;  }
	public String getType()  { return type;  }
	public String getValue() { return value; }

	public void setName(  String name  ) { this.name  = name;  }
	public void setType(  String type  ) { this.type  = type;  }
	public void setValue( String value ) { this.value = value; }

	@Override
	public String toString()
	{
		return name + " " + type + " " + value;
	}
}
