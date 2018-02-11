package ru.torment.shared;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

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

	//======================================================================================
	public Ultralisk( Color color, Double coordX, Double coordY, Integer speed )
	{
		super( bufferedImages, UnitType.ULTRALISK, "Ultralisk", color, coordX, coordY, bufferedImages[0].getWidth(), bufferedImages[0].getHeight(), speed );

		this.setRace( Race.ZERG );
		int[] animation = { 5 };
		this.setAnimationFrame( animation );
		this.setAnimate(true);
		this.setLoopAnim(true);
		this.setAnimationTimer( new Timer(60) );

		createMovingDirectionAnimationMap();
		createAttackDirectionAnimationMap();
		createDeathAnimation();
	}

	//======================================================================================
	// Анимация движений
	//======================================================================================
	void createMovingDirectionAnimationMap()
	{
		map_MovingDirectionAnimation = new HashMap< MovingDirection, int[] >();
			map_MovingDirectionAnimation.put( MovingDirection.LEFT,       new int[]{ 5, 15, 25, 35, 45, 55, 65, 75, 85 } );
			map_MovingDirectionAnimation.put( MovingDirection.LEFT_up,    new int[]{ 6, 16, 26, 36, 46, 56, 66, 76, 86 } );
			map_MovingDirectionAnimation.put( MovingDirection.UP_LEFT,    new int[]{ 7, 17, 27, 37, 47, 57, 67, 77, 87 } );
			map_MovingDirectionAnimation.put( MovingDirection.UP_left,    new int[]{ 8, 18, 28, 38, 48, 58, 68, 78, 88 } );
			map_MovingDirectionAnimation.put( MovingDirection.UP,         new int[]{ 9, 19, 29, 39, 49, 59, 69, 79, 89 } );
			map_MovingDirectionAnimation.put( MovingDirection.UP,         new int[]{ 160, 170, 180, 190, 200, 210, 220, 230, 240 } );
			map_MovingDirectionAnimation.put( MovingDirection.UP_right,   new int[]{ 161, 171, 181, 191, 201, 211, 221, 231, 241 } );
			map_MovingDirectionAnimation.put( MovingDirection.UP_RIGHT,   new int[]{ 162, 172, 182, 192, 202, 212, 222, 232, 242 } );
			map_MovingDirectionAnimation.put( MovingDirection.RIGHT_up,   new int[]{ 163, 173, 183, 193, 203, 213, 223, 233, 243 } );
			map_MovingDirectionAnimation.put( MovingDirection.RIGHT,      new int[]{ 163, 174, 184, 194, 204, 214, 224, 234, 244 } );
			map_MovingDirectionAnimation.put( MovingDirection.RIGHT,      new int[]{ 165, 175, 185, 195, 205, 215, 225, 235, 245 } );
			map_MovingDirectionAnimation.put( MovingDirection.RIGHT_down, new int[]{ 166, 176, 186, 196, 206, 216, 226, 236, 246 } );
			map_MovingDirectionAnimation.put( MovingDirection.DOWN_RIGHT, new int[]{ 167, 177, 187, 197, 207, 217, 227, 237, 247 } );
			map_MovingDirectionAnimation.put( MovingDirection.DOWN_right, new int[]{ 168, 178, 188, 198, 208, 218, 228, 238, 248 } );
			map_MovingDirectionAnimation.put( MovingDirection.DOWN,       new int[]{ 169, 179, 189, 199, 209, 219, 229, 239, 249 } );
			map_MovingDirectionAnimation.put( MovingDirection.DOWN,       new int[]{ 0, 10, 20, 30, 40, 50, 60, 70, 80 } );
			map_MovingDirectionAnimation.put( MovingDirection.DOWN_left,  new int[]{ 1, 11, 21, 31, 41, 51, 61, 71, 81 } );
			map_MovingDirectionAnimation.put( MovingDirection.DOWN_LEFT,  new int[]{ 2, 12, 22, 32, 42, 52, 62, 72, 82 } );
			map_MovingDirectionAnimation.put( MovingDirection.LEFT_down,  new int[]{ 3, 13, 23, 33, 43, 53, 63, 73, 83 } );
			map_MovingDirectionAnimation.put( MovingDirection.LEFT,       new int[]{ 4, 14, 24, 34, 44, 54, 64, 74, 84 } );
	}

	//======================================================================================
	// Анимация атаки
	//======================================================================================
	void createAttackDirectionAnimationMap()
	{
		map_AttackDirectionAnimation = new HashMap< MovingDirection, int[] >();
			map_AttackDirectionAnimation.put( MovingDirection.LEFT,       new int[]{ 145, 135, 125, 115, 105,  95 } );
			map_AttackDirectionAnimation.put( MovingDirection.LEFT_up,    new int[]{ 146, 136, 126, 116, 106,  96 } );
			map_AttackDirectionAnimation.put( MovingDirection.UP_LEFT,    new int[]{ 147, 137, 127, 117, 107,  97 } );
			map_AttackDirectionAnimation.put( MovingDirection.UP_left,    new int[]{ 148, 138, 128, 118, 108,  98 } );
			map_AttackDirectionAnimation.put( MovingDirection.UP,         new int[]{ 149, 139, 129, 119, 109,  99 } );
			map_AttackDirectionAnimation.put( MovingDirection.UP,         new int[]{ 300, 290, 280, 270, 260, 250 } );
			map_AttackDirectionAnimation.put( MovingDirection.UP_right,   new int[]{ 301, 291, 281, 271, 261, 251 } );
			map_AttackDirectionAnimation.put( MovingDirection.UP_RIGHT,   new int[]{ 302, 292, 282, 272, 262, 252 } );
			map_AttackDirectionAnimation.put( MovingDirection.RIGHT_up,   new int[]{ 303, 293, 283, 273, 263, 253 } );
			map_AttackDirectionAnimation.put( MovingDirection.RIGHT,      new int[]{ 303, 294, 284, 274, 264, 254 } );
			map_AttackDirectionAnimation.put( MovingDirection.RIGHT,      new int[]{ 305, 295, 285, 275, 265, 255 } );
			map_AttackDirectionAnimation.put( MovingDirection.RIGHT_down, new int[]{ 306, 296, 286, 276, 266, 256 } );
			map_AttackDirectionAnimation.put( MovingDirection.DOWN_RIGHT, new int[]{ 307, 297, 287, 277, 267, 257 } );
			map_AttackDirectionAnimation.put( MovingDirection.DOWN_right, new int[]{ 308, 298, 288, 278, 268, 258 } );
			map_AttackDirectionAnimation.put( MovingDirection.DOWN,       new int[]{ 309, 299, 289, 279, 269, 259 } );
			map_AttackDirectionAnimation.put( MovingDirection.DOWN,       new int[]{ 140, 130, 120, 110, 100,  90 } );
			map_AttackDirectionAnimation.put( MovingDirection.DOWN_left,  new int[]{ 141, 131, 121, 111, 101,  91 } );
			map_AttackDirectionAnimation.put( MovingDirection.DOWN_LEFT,  new int[]{ 142, 132, 122, 112, 102,  92 } );
			map_AttackDirectionAnimation.put( MovingDirection.LEFT_down,  new int[]{ 143, 133, 123, 113, 103,  93 } );
			map_AttackDirectionAnimation.put( MovingDirection.LEFT,       new int[]{ 144, 134, 124, 114, 104,  94 } );
	}

	//======================================================================================
	// Анимация гибели
	//======================================================================================
	void createDeathAnimation()
	{
		deathAnimation = new int[]{ 310, 311, 312, 313, 314, 315, 316, 317, 318, 319 };
	}

	//======================================================================================
	public void update( long elapsedTime )
	{
		super.update( elapsedTime );
	}
}
