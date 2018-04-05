package ru.torment.client.tileset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "terrain")
public class Terrain
{
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "tile")
	protected int tile;

	public String getName() { return name; }
	public int    getTile() { return tile; }

	public void setName( String name ) { this.name = name; }
	public void setTile( int    tile ) { this.tile = tile; }

	@Override
	public String toString()
	{
		return name + " " + tile;
	}
}
