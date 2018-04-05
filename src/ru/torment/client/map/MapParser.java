package ru.torment.client.map;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class MapParser
{
	public static void parseMap( String fileName )
	{
//		String fileName = "sc_map_test.tmx";
		byte[] mapXML = null;
		try {
			mapXML = fileToByteArray("bin/data/" + fileName );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try
		{
			Map map = (Map) unmarshal( Map.class, mapXML );
			System.out.println( map.toString() );
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	//======================================================================================
	public static byte[] fileToByteArray( String filePath ) throws IOException
	{
		System.out.println(" + fileToByteArray() --- filePath: " + filePath );
		ByteArrayOutputStream out = null;
		InputStream input = null;
		try
		{
			out = new ByteArrayOutputStream();
			input = new BufferedInputStream( new FileInputStream( new File( filePath ) ) );
			int data = 0;
			while ( (data = input.read()) != -1 ) { out.write( data ); }
		}
		catch ( FileNotFoundException e )
		{ e.printStackTrace(); }
		finally
		{
			if ( null != input ) { input.close(); }
			if ( null != out   ) { out.close();   }
		}
		System.out.println( out.toString() );
		return out.toByteArray();
	}

	//======================================================================================
	public static java.lang.Object unmarshal( Class class_, byte[] xml ) throws JAXBException
	{
		System.out.println(" + unmarshal() --- xml.length: " + xml.length + " bytes");
		JAXBContext context = JAXBContext.newInstance( class_ );
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// read XML from array of bytes
		InputStream inputStream = new ByteArrayInputStream( xml );
		java.lang.Object object = unmarshaller.unmarshal( inputStream );
		return object;
	}
}
