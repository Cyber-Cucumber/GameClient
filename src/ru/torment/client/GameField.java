package ru.torment.client;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

import ru.torment.shared.GameData;
import ru.torment.shared.GameDataType;
import ru.torment.shared.Unit;
import ru.torment.shared.UnitType;
import ru.torment.shared.User;
 
@SuppressWarnings("serial")
public class GameField extends JComponent implements ActionListener
{
	private static GameField gameField;

	private double scale;
	private Color colorMy;
	private Timer timer;
	private List<Unit> list_Unit;
	private User secondUser;
	private Unit selectedUnit;
	private static int fps = 50; // Кадров в секунду

	public static final GraphicsConfiguration CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

	//======================================================================================
	//======================================================================================
	private GameField( final Color colorMy )
	{
		System.out.println(" + DesktopChatClient::GameField::GameField()");

		list_Unit = new ArrayList<Unit>();

		scale = 1.0;
		timer = new Timer( 1000/fps, this );
		this.colorMy = colorMy;

		setPreferredSize( new Dimension(700, 700) );

		addMouseListener(
				new MouseListener()
				{
					@Override
					public void mouseReleased( MouseEvent e ) {
					}
					@Override
					public void mousePressed( MouseEvent e ) {
//						GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() );
						Unit unit = null;
						switch ( e.getButton() )
						{
							case MouseEvent.BUTTON1:
//								unit = new Unit( UnitType.TANK, "Ball Tank", GameField.this.colorMy, e.getX(), e.getY(), 20, 20, 1 );

								Boolean isUnitSelected = false;
								// Выделение объекта
								for ( Unit unit_ : list_Unit )
								{
									if ( unit_.isDead() ) { continue; }

									if ( e.getX() > unit_.getCoordX() - unit_.getWidth()/2  && e.getX() < unit_.getCoordX() + unit_.getWidth()/2 &&
										 e.getY() > unit_.getCoordY() - unit_.getHeight()/2 && e.getY() < unit_.getCoordY() + unit_.getHeight()/2 )
									{
										// Снимаем выделение с ранее выделенного объекта
										if ( selectedUnit != null && !selectedUnit.isDead() )
										{
											selectedUnit.setIsSelected(false);
											selectedUnit.setColor( GameField.this.colorMy );
										}

										isUnitSelected = true;
										unit_.setIsSelected(true);
										unit_.setColor( Color.BLUE );
										selectedUnit = unit_;
										repaint();
										break;
									}
								}

								// Перемещаем выделенный объект в новую точку
								if ( selectedUnit != null && !selectedUnit.isDead() && !isUnitSelected )
								{
									selectedUnit.setTargetCoordX( Double.valueOf( e.getX() ) );
									selectedUnit.setTargetCoordY( Double.valueOf( e.getY() ) );

									if ( selectedUnit.getCoordX() < selectedUnit.getTargetCoordX() )
									{
										if ( selectedUnit.getCoordY() < selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter(4); }
										else { selectedUnit.setTargetQuarter(2); }
									}
									else
									{
										if ( selectedUnit.getCoordY() < selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter(3); }
										else { selectedUnit.setTargetQuarter(1); }
									}

									Double dX = Math.abs( selectedUnit.getCoordX() - selectedUnit.getTargetCoordX() );
									Double dY = Math.abs( selectedUnit.getCoordY() - selectedUnit.getTargetCoordY() );
									selectedUnit.setTargetXtoY( Double.valueOf(dX/dY) );

									selectedUnit.setIsMoving(true);
									if ( !timer.isRunning() ) { timerStart(); }
								}
								break;
							case MouseEvent.BUTTON2:
								unit = new Unit( UnitType.BMP, "Ball BMP", GameField.this.colorMy, Double.valueOf( e.getX() ), Double.valueOf( e.getY() ), 20, 10, 3 );
								break;
							case MouseEvent.BUTTON3:
								unit = new Unit( UnitType.BTR, "Ball BTR", GameField.this.colorMy, Double.valueOf( e.getX() ), Double.valueOf( e.getY() ), 20, 20, 4 );
								break;
							default:
								break;
						}
						if ( unit == null ) { return; }
						addUnit( unit );
						ChatWindow.sendMessage( new GameData( GameDataType.NEW_UNIT, StartWindow.user, "GameData", unit ) );
					}
					@Override
					public void mouseExited( MouseEvent e ) {
					}
					@Override
					public void mouseEntered( MouseEvent e ) {
					}
					@Override
					public void mouseClicked( MouseEvent e ) {
					}
				});

		addMouseMotionListener(
				new MouseMotionListener()
				{
					@Override
					public void mouseMoved( MouseEvent e )
					{
//						System.out.println("mouseMoved() --- X: " + e.getX() + " --- Y: " + e.getY() );
						GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() );
					}
					@Override
					public void mouseDragged( MouseEvent e )
					{
//						System.out.println("mouseDragged");
					}
				});
	}

	//======================================================================================
	//======================================================================================
	public static GameField getInstance()
	{
		System.out.println(" + DesktopChatClient::GameField::getInstance()");
		if ( gameField == null ) { gameField = new GameField( Color.GREEN ); }
		return gameField;
	}

	//======================================================================================
	//======================================================================================
	public static void destroyInstance()
	{
		System.out.println(" + DesktopChatClient::GameField::destroyInstance()");
		if ( gameField != null ) { gameField = null; }
	}

	//======================================================================================
	//======================================================================================
	public void timerStart()
	{
		System.out.println(" + DesktopChatClient::GameField::timerStart()");
		timer.start();
	}

	//======================================================================================
	//======================================================================================
	public void timerStop()
	{
		System.out.println(" + DesktopChatClient::GameField::timerStop()");
		timer.stop();
	}

	//======================================================================================
	//======================================================================================
	@Override
	public void actionPerformed( ActionEvent e )
	{ 
//		System.out.println(" + DesktopChatClient::GameField::actionPerformed()");
		repaint();
	}

	//======================================================================================
	//======================================================================================
	@Override
	protected void paintComponent( Graphics g )
	{
//		System.out.println(" + DesktopChatClient::GameField::paintComponent()");

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor( Color.white );
		int width = getWidth();
		int height = getHeight();
		g.fillRect(0, 0, width, height);
		g2d.setColor( Color.black );
		g2d.drawRect( 0, 0, width - 1, height - 1 );

		g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.scale( scale, scale );

		boolean isAllUnitsStop = true;
		for ( Unit unit : list_Unit )
		{
//			System.out.println(" + DesktopChatClient::GameField::paintComponent() --- unit ID: " + unit.getId() + " --- Name: " + unit.getName() + " --- Type: " + unit.getUnitType() );

			Shape shape = null;
			g2d.setColor( unit.getColor() );
			if ( unit.getUnitType().equals( UnitType.TANK ) )
			{
//				Ellipse2D ellipse2d
				shape = new Ellipse2D.Double( unit.getCoordX() - unit.getWidth()/2, unit.getCoordY() - unit.getHeight()/2, unit.getWidth(), unit.getHeight() );
			}
			else if ( unit.getUnitType().equals( UnitType.BMP ) )
			{
//				Rectangle2D rectangle2d
				shape = new Rectangle2D.Double( unit.getCoordX() - unit.getWidth()/2, unit.getCoordY() - unit.getHeight()/2, unit.getWidth(), unit.getHeight() );
			}
			else if ( unit.getUnitType().equals( UnitType.BTR ) )
			{
//				Rectangle2D rectangle2d
				shape = new Rectangle2D.Double( unit.getCoordX() - unit.getWidth()/2, unit.getCoordY() - unit.getHeight()/2, unit.getWidth(), unit.getHeight() );
			}

			// Если объекту указана новая точка
			if ( unit.isMoving() && unit.getTargetCoordX() != null && unit.getTargetCoordY() != null )
			{
				// Если добрался до точки назначения
				// TODO Сделать по-другому
				if ( unit.getTargetCoordX() < unit.getCoordX() + 5 &&
					 unit.getTargetCoordX() > unit.getCoordX() - 5 &&
					 unit.getTargetCoordY() < unit.getCoordY() + 5 &&
					 unit.getTargetCoordY() > unit.getCoordY() - 5 )
				{
//					timerStop();
					unit.setTargetCoordX(null);
					unit.setTargetCoordY(null);
					unit.setIsMoving(false);
				}

				Integer sign = 1;
				Double XtoY = 1D;
				if ( unit.getTargetQuarter().equals(1) || unit.getTargetQuarter().equals(3) ) { sign = -1; }
				if ( unit.getTargetXtoY() < 1 ) { XtoY = unit.getTargetXtoY(); }
				unit.setCoordX( unit.getCoordX() + sign * unit.getSpeed() * XtoY );

				sign = 1;
				XtoY = 1D;
				if ( unit.getTargetQuarter().equals(1) || unit.getTargetQuarter().equals(2) ) { sign = -1; }
				if ( unit.getTargetXtoY() > 1 ) { XtoY = 1/unit.getTargetXtoY(); }
				unit.setCoordY( unit.getCoordY() + sign * unit.getSpeed() * XtoY );
			}

			String iconPath = "/ru/torment/icons/";
			ImageIcon imageIcon_ListEmptyItem = createImageIcon( iconPath + "banana.gif", "");
//			g2d.drawImage( imageIcon_ListEmptyItem.getImage(), 100, 100, null );

			java.net.URL url_Icon = Class.class.getResource( iconPath + "banana.gif" );
//			java.net.URL url_Icon = Class.class.getResource( iconPath + "error_24.png" );
			Image image = Toolkit.getDefaultToolkit().getImage( url_Icon );
			BufferedImage buffImage = CONFIG.createCompatibleImage( image.getWidth(null), image.getHeight(null), Transparency.BITMASK );
			Graphics2D graphics2d = (Graphics2D) buffImage.createGraphics();
			graphics2d.setComposite( AlphaComposite.Src );
			graphics2d.drawImage( image, 100, 100, null );
			graphics2d.dispose();
			g2d.drawImage( image, 100, 100, null );

			g2d.fill( shape );

			if ( unit.isDead() ) { continue; }

			// Проверка досягаемости других объектов относительно текущего
			List<Unit> list_EnemyUnits = getEnemysInKillZone( unit );
			for ( Unit unit_Enemy : list_EnemyUnits )
			{
				unit_Enemy.setHealthPoints( unit_Enemy.getHealthPoints() - unit.getAttack() );
				if ( unit_Enemy.getHealthPoints() <= 0 )
				{
					unit_Enemy.setIsDead(true);
				}
			}

			if ( unit.isMoving() ) { isAllUnitsStop = false; }
		}
		// Останавливаем перерисовку только в случае, когда все объекты закончили перемещение
		if ( isAllUnitsStop ) { timerStop(); }
	}


	//======================================================================================
	//======================================================================================
	protected static ImageIcon createImageIcon( String path, String description )
	{
		java.net.URL url_Icon = Class.class.getResource( path );

		if ( url_Icon != null )
		{
			return new ImageIcon( url_Icon, description );
		}
		else
		{
			System.err.println("Иконка не найдена: " + path );
			return null;
		}
	}


	//======================================================================================
	// Проверка досягаемости других объектов относительно текущего
	//======================================================================================
	private List<Unit> getEnemysInKillZone( Unit unit )
	{
		List<Unit> list_EnemyUnits = new ArrayList<Unit>();
		for ( Unit unit_Enemy : list_Unit )
		{
			if ( unit_Enemy.isDead() ) { continue; }

			if ( unit.getCoordX() > unit_Enemy.getCoordX() && unit.getCoordX() <= unit_Enemy.getCoordX() + unit.getAttackRadius() )
			{
				if ( ( unit.getCoordY() > unit_Enemy.getCoordY() && unit.getCoordY() <= unit_Enemy.getCoordY() + unit.getAttackRadius() ) ||
					 ( unit.getCoordY() < unit_Enemy.getCoordY() && unit.getCoordY() >= unit_Enemy.getCoordY() - unit.getAttackRadius() ) )
				{
					System.out.println(" + DesktopChatClient::GameField::paintComponent() --- Unit: " + unit.getName() + " --- attacked Unit: " + unit_Enemy.getName() );
					list_EnemyUnits.add( unit_Enemy );
				}
			}
			else if ( unit.getCoordX() < unit_Enemy.getCoordX() && unit.getCoordX() >= unit_Enemy.getCoordX() - unit.getAttackRadius() )
			{
				if ( ( unit.getCoordY() > unit_Enemy.getCoordY() && unit.getCoordY() <= unit_Enemy.getCoordY() + unit.getAttackRadius() ) ||
					 ( unit.getCoordY() < unit_Enemy.getCoordY() && unit.getCoordY() >= unit_Enemy.getCoordY() - unit.getAttackRadius() ) )
				{
					System.out.println(" + DesktopChatClient::GameField::paintComponent() --- Unit: " + unit.getName() + " --- attacked Unit: " + unit_Enemy.getName() );
					list_EnemyUnits.add( unit_Enemy );
				}
			}
		}
		return list_EnemyUnits;
	}


	//======================================================================================
	//======================================================================================
	private void addUnit( Unit unit )
	{
		System.out.println(" + DesktopChatClient::GameField::addUnit()");
		list_Unit.add( unit );
		repaint();
	}

	//======================================================================================
	//======================================================================================
	private void deleteUnit( Unit unit )
	{
		System.out.println(" + DesktopChatClient::GameField::deleteUnit()");
		list_Unit.remove( unit );
		repaint();
	}

	//======================================================================================
	//======================================================================================
	private void moveUnit( Unit unitForMove )
	{
		System.out.println(" + DesktopChatClient::GameField::moveUnit()");
		for ( Unit unit : list_Unit )
		{
			if ( unit.equals( unitForMove ) )
			{
				unit.setCoordX( unitForMove.getCoordX() );
				unit.setCoordY( unitForMove.getCoordY() );
				break;
			}
		}
		repaint();
	}

	//======================================================================================
	//======================================================================================
	public void newGameData( GameData gameData )
	{
		System.out.println(" + DesktopChatClient::GameField::newGameData()");
		if ( gameData.getGameDataType().equals( GameDataType.NEW_UNIT ) )
		{
			addUnit( gameData.getUnit() );
		}
		else if ( gameData.getGameDataType().equals( GameDataType.UNIT_NEW_POSITION ) )
		{
			moveUnit( gameData.getUnit() );
		}
		else if ( gameData.getGameDataType().equals( GameDataType.DELETE_UNIT ) )
		{
			deleteUnit( gameData.getUnit() );
		}
	}

	//======================================================================================
	public Color getColorMy()    { return colorMy;    }
	public User  getSecondUser() { return secondUser; }

	//======================================================================================
	public void setColorMy(    Color colorMy    ) { this.colorMy    = colorMy;    }
	public void setSecondUser( User  secondUser ) { this.secondUser = secondUser; }
}
