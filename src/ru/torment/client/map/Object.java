package ru.torment.client.map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "")
public class Object
{
	@XmlAttribute(name = "id")
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "x")
	protected Float x;
	@XmlAttribute(name = "y")
	protected Float y;
	@XmlAttribute(name = "width")
	protected Float width;
	@XmlAttribute(name = "height")
	protected Float height;
	@XmlAttribute(name = "visible")
	protected int visible;

	// Getters
	public int    getId()      { return id;      }
	public String getName()    { return name;    }
	public Float  getX()       { return x;       }
	public Float  getY()       { return y;       }
	public Float  getWidth()   { return width;   }
	public Float  getHeight()  { return height;  }
	public int    getVisible() { return visible; }

	// Setters
	public void setId(      int    id      ) { this.id      = id;      }
	public void setName(    String name    ) { this.name    = name;    }
	public void setX(       Float  x       ) { this.x       = x;       }
	public void setY(       Float  y       ) { this.y       = y;       }
	public void setWidth(   Float  width   ) { this.width   = width;   }
	public void setHeight(  Float  height  ) { this.height  = height;  }
	public void setVisible( int    visible ) { this.visible = visible; }

	@Override
	public String toString()
	{
		return id + " " + name + " " + x + " " + y + " " + width + " " + height + " " + visible;
	}
}
