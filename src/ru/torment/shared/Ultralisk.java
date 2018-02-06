package ru.torment.shared;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ru.torment.client.GameField;
import ru.torment.client.Util;

public class Ultralisk extends Unit
{
	private static final long serialVersionUID = 1L;

	private static BufferedImage[] bufferedImages;

	static
	{
		bufferedImages = Util.concatTwoBufferedImages( GameField.getImages("sc_ultralisk_l.png", 10, 16 ), GameField.getImages("sc_ultralisk_r.png", 10, 16 ) );		
	}

	public Ultralisk( UnitType unitType, String name, Color color, Double coordX, Double coordY, Integer width, Integer height, Integer speed )
	{
		super( bufferedImages, unitType, name, color, coordX, coordY, width, height, speed);
		int[] animation = { 5 };
		this.setAnimationFrame( animation );
		this.setAnimate(true);
		this.setLoopAnim(true);
		this.setAnimationTimer( new Timer(60) );
	}
}
