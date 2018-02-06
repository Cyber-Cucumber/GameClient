package ru.torment.client;

import java.awt.image.BufferedImage;

public class Util
{
	public static BufferedImage[] concatTwoBufferedImages( BufferedImage[] array_BufferedImage_1, BufferedImage[] array_BufferedImage_2 )
	{
		BufferedImage[] abi = new BufferedImage[ array_BufferedImage_1.length + array_BufferedImage_2.length ];
		for ( int i = 0; i < array_BufferedImage_1.length - 1; i++ )
		{
			abi[i] = array_BufferedImage_1[i];
		}
		for ( int i = 0; i < array_BufferedImage_2.length - 1; i++ )
		{
			abi[array_BufferedImage_1.length + i] = array_BufferedImage_2[i];
		}
		return abi;
	}
}
