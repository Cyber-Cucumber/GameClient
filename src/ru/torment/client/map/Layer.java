package ru.torment.client.map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"data"} )
public class Layer
{
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "width")
	protected int width;
	@XmlAttribute(name = "height")
	protected int height;
	@XmlElement(required = true)
	protected Data data;

	// Getters
	public String getName()   { return name;   }
	public int    getWidth()  { return width;  }
	public int    getHeight() { return height; }
	public Data   getData()   { return data;   }

	// Setters
	public void setName(   String name   ) { this.name   = name;   }
	public void setWidth(  int    width  ) { this.width  = width;  }
	public void setHeight( int    height ) { this.height = height; }
	public void setData(   Data   data   ) { this.data   = data;   }

	@Override
	public String toString()
	{
		return name + " " + width + " " + height + "\n\t\t" + data;
	}
}
