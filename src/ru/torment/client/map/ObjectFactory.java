package ru.torment.client.map;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
	public ObjectFactory() {}

	public Data        createData()        { return new Data();        }
	public Tileset     createTileset()     { return new Tileset();     }
	public Map         createMap()         { return new Map();         }
	public Layer       createLayer()       { return new Layer();       }
	public Objectgroup createObjectgroup() { return new Objectgroup(); }
	public Object      createObject()      { return new Object();      }
}
