package ru.torment.client.map;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "map")
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"tilesets", "layers", "objectgroups"} )
public class Map
{
	@XmlAttribute(name = "version")
	protected Float version;
	@XmlAttribute(name = "tiledversion")
	protected String tiledversion;
	@XmlAttribute(name = "orientation")
	protected String orientation;
	@XmlAttribute(name = "renderorder")
	protected String renderorder;
	@XmlAttribute(name = "width")
	protected int width;
	@XmlAttribute(name = "height")
	protected int height;
	@XmlAttribute(name = "tilewidth")
	protected int tilewidth;
	@XmlAttribute(name = "tileheight")
	protected int tileheight;
	@XmlAttribute(name = "nextobjectid")
	protected int nextobjectid;

	@XmlElement( name = "tileset", required = true)
	protected List<Tileset>     tilesets     = new ArrayList<Tileset>();
	@XmlElement( name = "layer", required = true)
	protected List<Layer>       layers       = new ArrayList<Layer>();
	@XmlElement( name = "objectgroup", required = true)
	protected List<Objectgroup> objectgroups = new ArrayList<Objectgroup>();

	// Getters
	public Float  getVersion()      { return version;      }
	public String getTiledversion() { return tiledversion; }
	public String getOrientation()  { return orientation;  }
	public String getRenderorder()  { return renderorder;  }
	public int    getWidth()        { return width;        }
	public int    getHeight()       { return height;       }
	public int    getTilewidth()    { return tilewidth;    }
	public int    getTileheight()   { return tileheight;   }
	public int    getNextobjectid() { return nextobjectid; }

	public List<Tileset>     getTilesets()     { return this.tilesets;     }
	public List<Layer>       getLayers()       { return this.layers;       }
	public List<Objectgroup> getObjectgroups() { return this.objectgroups; }

	// Setters
	public void setVersion(      Float  version      ) { this.version      = version;      }
	public void setTiledversion( String tiledversion ) { this.tiledversion = tiledversion; }
	public void setOrientation(  String orientation  ) { this.orientation  = orientation;  }
	public void setRenderorder(  String renderorder  ) { this.renderorder  = renderorder;  }
	public void setWidth(        int    width        ) { this.width        = width;        }
	public void setHeight(       int    height       ) { this.height       = height;       }
	public void setTilewidth(    int    tilewidth    ) { this.tilewidth    = tilewidth;    }
	public void setTileheight(   int    tileheight   ) { this.tileheight   = tileheight;   }
	public void setNextobjectid( int    nextobjectid ) { this.nextobjectid = nextobjectid; }

	public void setTilesets(     List<Tileset>     tilesets     ) { this.tilesets     = tilesets;     }
	public void setLayers(       List<Layer>       layers       ) { this.layers       = layers;       }
	public void setObjectgroups( List<Objectgroup> objectgroups ) { this.objectgroups = objectgroups; }

	@Override
	public String toString()
	{
		String outTilesets = "\n\t------------------------------------";
		for ( Tileset tileset : tilesets )
		{
			outTilesets += "\n\t" + tileset;
		}
		String outLayers = "\n\t------------------------------------";
		for ( Layer layer : layers )
		{
			outLayers += "\n\t" + layer;
		}
		String outObjectgroups = "\n\t------------------------------------";
		for ( Objectgroup objectgroup : objectgroups )
		{
			outObjectgroups += "\n\t" + objectgroup;
		}
		return version + " " + tiledversion + " " + orientation + " " + renderorder + " " + width + " " + height + " " + tilewidth + " " + tileheight + " " + nextobjectid + outTilesets + outLayers + outObjectgroups;
	}
}
