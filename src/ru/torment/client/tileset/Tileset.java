package ru.torment.client.tileset;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "tileset" )
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "tileset", propOrder = {"image", "terrains", "tiles"} )
public class Tileset
{
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "tilewidth")
	protected int tilewidth;
	@XmlAttribute(name = "tileheight")
	protected int tileheight;
	@XmlAttribute(name = "tilecount")
	protected int tilecount;
	@XmlAttribute(name = "columns")
	protected int columns;

	@XmlElement(required = true)
	protected Image image;

	@XmlElementWrapper( name = "terraintypes" )
	@XmlElement( name = "terrain" )
	protected List<Terrain> terrains = new ArrayList<Terrain>();

	@XmlElement( name = "tile" )
	protected List<Tile> tiles = new ArrayList<Tile>();

	public String getName()       { return name;       }
	public int    getTilewidth()  { return tilewidth;  }
	public int    getTileheight() { return tileheight; }
	public int    getTilecount()  { return tilecount;  }
	public int    getColumns()    { return columns;    }
	public Image  getImage()      { return image;      }
	public List<Terrain> getTerraintypes() { return terrains; }
	public List<Tile>    getTiles()        { return tiles;    }

	public void setName(       String name       ) { this.name       = name;       }
	public void setTilewidth(  int    tilewidth  ) { this.tilewidth  = tilewidth;  }
	public void setTileheight( int    tileheight ) { this.tileheight = tileheight; }
	public void setTilecount(  int    tilecount  ) { this.tilecount  = tilecount;  }
	public void setColumns(    int    columns    ) { this.columns    = columns;    }
	public void setImage(      Image  image      ) { this.image      = image;      }
	public void setTerraintypes( List<Terrain> terrains ) { this.terrains = terrains; }
	public void setTiles(        List<Tile>    tiles    ) { this.tiles    = tiles;    }

	@Override
	public String toString()
	{
		String outTerr = "\n\t----------------------------------";
		for ( Terrain terrain : terrains )
		{
			outTerr += "\n\t" + terrain;
		}
		String outTile = "\n\t----------------------------------";
		for ( Tile tile : tiles )
		{
			outTile += "\n\t" + tile;
		}
		return name + " " + tilewidth + " " + tileheight + " " + tilecount + " " + columns + "\n" + image + outTerr + outTile;
	}
}
