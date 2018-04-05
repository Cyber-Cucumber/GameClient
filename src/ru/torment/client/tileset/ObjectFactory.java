package ru.torment.client.tileset;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory
{
	private final static QName _Tileset_QNAME = new QName("", "tileset");

	public ObjectFactory() {}

	public Tile     createTile()     { return new Tile();     }
	public Tileset  createTileset()  { return new Tileset();  }
	public Image    createImage()    { return new Image();    }
	public Property createProperty() { return new Property(); }
	public Terrain  createTerrain()  { return new Terrain();  }

	@XmlElementDecl(namespace = "", name = "tileset")
	public JAXBElement<Tileset> createTileset( Tileset value ) { return new JAXBElement<Tileset>(_Tileset_QNAME, Tileset.class, null, value); }
}
