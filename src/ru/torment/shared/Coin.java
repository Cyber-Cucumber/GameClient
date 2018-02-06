package ru.torment.shared;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ru.torment.client.GameField;
import ru.torment.client.Util;

public class Coin extends Unit
{
	private static final long serialVersionUID = 1L;

	private static BufferedImage[] bufferedImages;

	static
	{
		bufferedImages = GameField.getImages("Coin.png", 3, 1 );		
	}

	public Coin( UnitType unitType, String name, Color color, Double coordX, Double coordY, Integer width, Integer height, Integer speed )
	{
		super( bufferedImages, unitType, name, color, coordX, coordY, width, height, speed );
		int[] animation = { 0, 0, 0, 0, 1, 2, 1, 0 };
		this.setAnimationFrame( animation );
		this.setAnimate(true);
		this.setLoopAnim(true);
		this.setAnimationTimer( new Timer(150) );
	}
}
