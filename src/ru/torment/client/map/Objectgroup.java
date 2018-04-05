package ru.torment.client.map;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"objects"} )
public class Objectgroup
{
	@XmlAttribute(name = "name")
	protected String name;

	@XmlElement( name = "object", required = true)
	protected List<Object> objects = new ArrayList<Object>();

	// Getters
	public String getName() { return name; }
	public List<Object> getObjects() { return objects; }

	// Setters
	public void setName( String name ) { this.name = name; }
	public void setObjects( List<Object> objects ) { this.objects = objects; }

	@Override
	public String toString()
	{
		String outObjects = "\n\t\t------------------------------------";
		for ( Object object : objects )
		{
			outObjects += "\n\t\t" + object;
		}
		return name + outObjects;
	}
}
