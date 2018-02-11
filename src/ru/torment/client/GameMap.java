package ru.torment.client;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import ru.torment.shared.Unit;

public class GameMap
{
	Image baseGameMap = null;

	// Координаты верхнего левого и правого нижнего углов куска карты, отображаемого в данный момент
	public static int baseGameMapX1 = 0;
	public static int baseGameMapY1 = 0;
	public static int baseGameMapX2 = GameField.defaultGameFieldWidth;
	public static int baseGameMapY2 = GameField.defaultGameFieldHeight;

	boolean isGameMapShift_Left  = false;
	boolean isGameMapShift_Right = false;
	boolean isGameMapShift_Up    = false;
	boolean isGameMapShift_Down  = false;

	int gameMapShiftValue_Normal = 20;
	int gameMapShiftValue_Fast   = 40;
	int gameMapShiftValue = gameMapShiftValue_Normal;

	GameField gameField;

	//======================================================================================
	public GameMap( GameField gameField )
	{
		System.out.println(" + GameClient::GameMap::GameMap()");

		this.gameField = gameField;

		java.net.URL url = Class.class.getResource("/ru/torment/icons/sc_map.jpg");
		try
		{
			baseGameMap = ImageIO.read( url );
		}
		catch ( IOException e1 )
		{
			e1.printStackTrace();
		}
	}

	//======================================================================================
	Timer timer_GameMapShift = new Timer(
			50,
			new ActionListener()
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					if ( isGameMapShift_Left )
					{
						if ( baseGameMapX1 <= 0 )
						{
							baseGameMapX1 = 0;
							baseGameMapX2 = gameField.getWidth();
						}
						else
						{
							baseGameMapX1 -= gameMapShiftValue;
							baseGameMapX2 -= gameMapShiftValue;
							shiftAllObjects( gameMapShiftValue, 0);
						}
					}
					if ( isGameMapShift_Right )
					{
						if ( baseGameMapX2 >= baseGameMap.getWidth(null) )
						{
							baseGameMapX2 = baseGameMap.getWidth(null);
							baseGameMapX1 = baseGameMapX2 - gameField.getWidth();
						}
						else
						{
							baseGameMapX1 += gameMapShiftValue;
							baseGameMapX2 += gameMapShiftValue;
							shiftAllObjects( -gameMapShiftValue, 0);
						}
					}
					if ( isGameMapShift_Up )
					{
						if ( baseGameMapY1 <= 0 )
						{
							baseGameMapY1 = 0;
							baseGameMapY2 = gameField.getHeight();
						}
						else
						{
							baseGameMapY1 -= gameMapShiftValue;
							baseGameMapY2 -= gameMapShiftValue;
							shiftAllObjects( 0, gameMapShiftValue );
						}
					}
					if ( isGameMapShift_Down )
					{
						if ( baseGameMapY2 >= baseGameMap.getHeight(null) )
						{
							baseGameMapY2 = baseGameMap.getHeight(null);
							baseGameMapY1 = baseGameMapY2 - gameField.getHeight();
						}
						else
						{
							baseGameMapY1 += gameMapShiftValue;
							baseGameMapY2 += gameMapShiftValue;
							shiftAllObjects( 0, -gameMapShiftValue );
						}
					}
				}
			});

	//======================================================================================
	public void update()
	{
	}

	//======================================================================================
	public void render( Graphics2D g2d )
	{
		g2d.drawImage( baseGameMap, 0, 0, gameField.getWidth(), gameField.getHeight(), baseGameMapX1, baseGameMapY1, baseGameMapX2, baseGameMapY2, null );
	}

	//======================================================================================
	private void shiftAllObjects( int gameMapShiftValue_X, int gameMapShiftValue_Y )
	{
		System.out.println(" + GameClient::GameMap::shiftAllObjects()");

		for ( Unit unit_Dead : GameField.getListDeadUnits() )
		{
			unit_Dead.setCoordX( unit_Dead.getCoordX() + gameMapShiftValue_X );
			unit_Dead.setCoordY( unit_Dead.getCoordY() + gameMapShiftValue_Y );
		}

		for ( Unit unit : GameField.getListUnits() )
		{
			unit.setCoordX( unit.getCoordX() + gameMapShiftValue_X );
			unit.setCoordY( unit.getCoordY() + gameMapShiftValue_Y );
		}
	}
}
