package ru.torment.client.tileset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "image")
public class Image
{
	@XmlAttribute(name = "source")
	protected String source;
	@XmlAttribute(name = "width")
	protected int width;
	@XmlAttribute(name = "height")
	protected int height;

	public String getSource() { return source; }
	public int    getWidth()  { return width;  }
	public int    getHeight() { return height; }

	public void setSource( String source ) { this.source = source; }
	public void setWidth(  int    width  ) { this.width  = width;  }
	public void setHeight( int    height ) { this.height = height; }

	@Override
	public String toString()
	{
		return source + " " + width + " " + height;
	}
}
