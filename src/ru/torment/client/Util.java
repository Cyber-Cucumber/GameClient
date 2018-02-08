package ru.torment.client;

import java.awt.image.BufferedImage;

import ru.torment.shared.MovingDirection;
import ru.torment.shared.TargetQuarter;

public class Util
{
	//======================================================================================
	// Склеить два BufferedImage[]
	//======================================================================================
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

	//======================================================================================
	// Узнать направление движения объекта (всего 16 - направлений)
	//======================================================================================
	public static MovingDirection getMovingDirection( TargetQuarter targetQuarter, Double targetYtoX )
	{
		System.out.println(" + GameClient::Unit::getMovingDirection()");
		System.out.println(" + GameClient::Unit::getMovingDirection() --- targetYtoX: " + targetYtoX );

		MovingDirection movingDirection = null;

		if ( targetQuarter.equals( TargetQuarter.II_Quarter ) )
		{
			System.out.println(" + GameClient::Unit::getMovingDirection() --- II_Quarter");
			if      ( targetYtoX > 0                                  && targetYtoX < Math.tan( Math.toRadians(11.25) ) ) { movingDirection = MovingDirection.LEFT;    }
			else if ( targetYtoX >= Math.tan( Math.toRadians(11.25) ) && targetYtoX < Math.tan( Math.toRadians(33.75) ) ) { movingDirection = MovingDirection.LEFT_up; }
			else if ( targetYtoX >= Math.tan( Math.toRadians(33.75) ) && targetYtoX < Math.tan( Math.toRadians(56.25) ) ) { movingDirection = MovingDirection.UP_LEFT; }
			else if ( targetYtoX >= Math.tan( Math.toRadians(56.25) ) && targetYtoX < Math.tan( Math.toRadians(78.75) ) ) { movingDirection = MovingDirection.UP_left; }
			else if ( targetYtoX >= Math.tan( Math.toRadians(78.75) ) && targetYtoX < Math.tan( Math.toRadians(90   ) ) ) { movingDirection = MovingDirection.UP;      }
		}
		else if ( targetQuarter.equals( TargetQuarter.I_Quarter ) )
		{
			double addition = 90D;
			System.out.println(" + GameClient::Unit::getMovingDirection() --- I_Quarter ");
			if      ( -targetYtoX >= Math.tan( Math.toRadians( addition +  0.0000001 ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 11.25 ) ) ) { movingDirection = MovingDirection.UP;       }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 11.25      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 33.75 ) ) ) { movingDirection = MovingDirection.UP_right; }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 33.75      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 56.25 ) ) ) { movingDirection = MovingDirection.UP_RIGHT; }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 56.25      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 78.75 ) ) ) { movingDirection = MovingDirection.RIGHT_up; }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 78.75      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 90    ) ) ) { movingDirection = MovingDirection.RIGHT;    }
		}
		else if ( targetQuarter.equals( TargetQuarter.IV_Quarter ) )
		{
			double addition = 180D;
			System.out.println(" + GameClient::Unit::getMovingDirection() --- IV_Quarter");
			if      ( targetYtoX >= Math.tan( Math.toRadians( addition         ) ) && targetYtoX < Math.tan( Math.toRadians( addition + 11.25 ) ) ) { movingDirection = MovingDirection.RIGHT;      }
			else if ( targetYtoX >= Math.tan( Math.toRadians( addition + 11.25 ) ) && targetYtoX < Math.tan( Math.toRadians( addition + 33.75 ) ) ) { movingDirection = MovingDirection.RIGHT_down; }
			else if ( targetYtoX >= Math.tan( Math.toRadians( addition + 33.75 ) ) && targetYtoX < Math.tan( Math.toRadians( addition + 56.25 ) ) ) { movingDirection = MovingDirection.DOWN_RIGHT; }
			else if ( targetYtoX >= Math.tan( Math.toRadians( addition + 56.25 ) ) && targetYtoX < Math.tan( Math.toRadians( addition + 78.75 ) ) ) { movingDirection = MovingDirection.DOWN_right; }
			else if ( targetYtoX >= Math.tan( Math.toRadians( addition + 78.75 ) ) && targetYtoX < Math.tan( Math.toRadians( addition + 90    ) ) ) { movingDirection = MovingDirection.DOWN;       }
		}
		else if ( targetQuarter.equals( TargetQuarter.III_Quarter ) )
		{
			double addition = 270D;
			System.out.println(" + GameClient::Unit::getMovingDirection() --- III_Quarter");
			if      ( -targetYtoX >= Math.tan( Math.toRadians( addition +  0.0000001 ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 11.25 ) ) ) { movingDirection = MovingDirection.DOWN;      }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 11.25      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 33.75 ) ) ) { movingDirection = MovingDirection.DOWN_left; }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 33.75      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 56.25 ) ) ) { movingDirection = MovingDirection.DOWN_LEFT; }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 56.25      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 78.75 ) ) ) { movingDirection = MovingDirection.LEFT_down; }
			else if ( -targetYtoX >= Math.tan( Math.toRadians( addition + 78.75      ) ) && -targetYtoX < Math.tan( Math.toRadians( addition + 90    ) ) ) { movingDirection = MovingDirection.LEFT;      }
		}

		return movingDirection;
	}
}
