package ru.torment.client.tileset;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "tile", propOrder = {"properties"} )
public class Tile
{
	@XmlAttribute(name = "id")
	protected int id;
	@XmlAttribute(name = "terrain")
	protected String terrain;
	@XmlAttribute(name = "type")
	protected String type;

	@XmlElementWrapper( name = "properties" )
	@XmlElement( name = "property" )
	protected List<Property> properties = new ArrayList<Property>();

	public int    getId()      { return id;      }
	public String getTerrain() { return terrain; }
	public String getType()    { return type;    }
	public List<Property> getProperties() { return properties; }

	public void setId(      int    id      ) { this.id      = id;      }
	public void setTerrain( String terrain ) { this.terrain = terrain; }
	public void setType(    String type    ) { this.type    = type;    }
	public void setProperties( List<Property> properties ) { this.properties = properties; }

	@Override
	public String toString()
	{
		String outProp = "";
		for ( Property property : properties )
		{
			outProp += "\n\t\t" + property;
		}
		return id + " " + terrain + " " + type + outProp;
	}
}
