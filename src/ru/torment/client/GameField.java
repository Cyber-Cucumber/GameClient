package ru.torment.client;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

import ru.torment.shared.Coin;
import ru.torment.shared.GameData;
import ru.torment.shared.GameDataType;
import ru.torment.shared.TargetQuarter;
import ru.torment.shared.Ultralisk;
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
	private static List<Unit> list_Unit;
	private User secondUser;
	private Unit selectedUnit;
	private static int fps = 50; // Кадров в секунду

	public static final GraphicsConfiguration CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

	Image baseGameMap = null;

	int defaultGameFieldWidth  = 700;
	int defaultGameFieldHeight = 700;

	int baseGameMapX1 = 0;
	int baseGameMapY1 = 0;
	int baseGameMapX2 = defaultGameFieldWidth;
	int baseGameMapY2 = defaultGameFieldHeight;

	boolean isGameMapShift_Left  = false;
	boolean isGameMapShift_Right = false;
	boolean isGameMapShift_Up    = false;
	boolean isGameMapShift_Down  = false;

	int gameMapShiftValue_Normal = 20;
	int gameMapShiftValue_Fast   = 40;
	int gameMapShiftValue = gameMapShiftValue_Normal;

	//======================================================================================
	//======================================================================================
	private GameField( final Color colorMy )
	{
		System.out.println(" + DesktopChatClient::GameField::GameField()");

		java.net.URL url = Class.class.getResource("/ru/torment/icons/sc_map.jpg");
		try
		{
			baseGameMap = ImageIO.read( url );
		}
		catch ( IOException e1 )
		{
			e1.printStackTrace();
		}

		list_Unit = new ArrayList<Unit>();

		scale = 1.0;
		timer = new Timer( 1000 / fps, this );
		this.colorMy = colorMy;

		setPreferredSize( new Dimension( defaultGameFieldWidth, defaultGameFieldHeight ) );

		addMouseListener(
				new MouseListener()
				{
					@Override
					public void mouseReleased( MouseEvent e ) {
					}
					@Override
					public void mousePressed( MouseEvent e )
					{
						System.out.println(" + DesktopChatClient::GameField::GameField() --- mousePressed()");

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
									System.out.println(" + DesktopChatClient::GameField::GameField() --- mousePressed() --- moveTo");

									selectedUnit.setTargetCoordX( Double.valueOf( e.getX() ) );
									selectedUnit.setTargetCoordY( Double.valueOf( e.getY() ) );

									// Определяем в какой четверти координатной плоскости находится точка назначения (относительно объекта)
									if ( selectedUnit.getCoordX() < selectedUnit.getTargetCoordX() )  // Точка назначения справа (I или IV четверть)
									{
										if      ( selectedUnit.getCoordY() < selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter( TargetQuarter.IV_Quarter      ); }
										else if ( selectedUnit.getCoordY() > selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter( TargetQuarter.I_Quarter       ); }
										else                                                                  { selectedUnit.setTargetQuarter( TargetQuarter.On_Y_Axis_Right ); }
									}
									else if ( selectedUnit.getCoordX() > selectedUnit.getTargetCoordX() )  // Точка назначения слева (II или III четверть)
									{
										if      ( selectedUnit.getCoordY() < selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter( TargetQuarter.III_Quarter    ); }
										else if ( selectedUnit.getCoordY() > selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter( TargetQuarter.II_Quarter     ); }
										else                                                                  { selectedUnit.setTargetQuarter( TargetQuarter.On_Y_Axis_Left ); }
									}
									else  // Точка назначения на оси X
									{
										if ( selectedUnit.getCoordY() < selectedUnit.getTargetCoordY() ) { selectedUnit.setTargetQuarter( TargetQuarter.On_X_Axis_Down ); }
										else                                                             { selectedUnit.setTargetQuarter( TargetQuarter.On_X_Axis_Up   ); }
									}

									Double dX = Math.abs( selectedUnit.getCoordX() - selectedUnit.getTargetCoordX() );
									Double dY = Math.abs( selectedUnit.getCoordY() - selectedUnit.getTargetCoordY() );
									selectedUnit.setTargetYtoX( Double.valueOf(dY/dX) );

									selectedUnit.setIsMoving(true);
								}
								break;
							case MouseEvent.BUTTON2:
								unit = new Coin( UnitType.BTR, "Ball BTR", GameField.this.colorMy, Double.valueOf( e.getX() ), Double.valueOf( e.getY() ), 20, 20, 4 );
								break;
							case MouseEvent.BUTTON3:
								unit = new Ultralisk( UnitType.BMP, "Ball BMP", GameField.this.colorMy, Double.valueOf( e.getX() ), Double.valueOf( e.getY() ), 3 );
								break;
							default:
								break;
						}
						if ( unit == null ) { return; }
						addUnit( unit );
						ChatWindow.sendMessage( new GameData( GameDataType.NEW_UNIT, StartWindow.user, "GameData", unit ) );
					}
					@Override
					public void mouseExited( MouseEvent e )
					{
						System.out.println(" + GameClient::GameField::GameField() --- mouseExited()");

						isGameMapShift_Left  = false;
						isGameMapShift_Right = false;
						isGameMapShift_Up    = false;
						isGameMapShift_Down  = false;
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

						isGameMapShift_Left  = false;
						isGameMapShift_Right = false;
						isGameMapShift_Up    = false;
						isGameMapShift_Down  = false;

						timer_GameMapShift.start();

						setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

						// Сдвиг карты вправо
						if ( e.getX() > getWidth() - 50 )
						{
							isGameMapShift_Right = true;
							gameMapShiftValue = gameMapShiftValue_Normal;
							GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " >>");
							Image curImage = Toolkit.getDefaultToolkit().createImage( getClass().getResource("/ru/torment/icons/check_24.png") );
							setCursor( Toolkit.getDefaultToolkit().createCustomCursor( curImage, new Point(8,8), "CustomCursor" ) );
							if ( e.getX() > getWidth() - 20 )  // Быстрый сдвиг
							{
								GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " >>>>");
								gameMapShiftValue = gameMapShiftValue_Fast;
							}
						}
						// Сдвиг карты влево
						else if ( e.getX() < 50 )
						{
							isGameMapShift_Left = true;
							gameMapShiftValue = gameMapShiftValue_Normal;
							GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " <<");
							if ( e.getX() < 20 )  // Быстрый сдвиг
							{
								GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " <<<<");
								gameMapShiftValue = gameMapShiftValue_Fast;
							}
						}

						// Сдвиг карты вниз
						if ( e.getY() > getHeight() - 50 )
						{
							isGameMapShift_Down = true;
							gameMapShiftValue = gameMapShiftValue_Normal;
							GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " vv");
							if ( e.getY() > getHeight() - 20 )  // Быстрый сдвиг
							{
								GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " vvvv");
								gameMapShiftValue = gameMapShiftValue_Fast;
							}
						}
						// Сдвиг карты вверх
						else if ( e.getY() < 50 )
						{
							isGameMapShift_Up = true;
							gameMapShiftValue = gameMapShiftValue_Normal;
							GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " ^^");
							if ( e.getY() < 20 )  // Быстрый сдвиг
							{
								GameFrame.jLabel.setText("X: " + e.getX() + ", Y: " + e.getY() + " ^^^^");
								gameMapShiftValue = gameMapShiftValue_Fast;
							}
						}
					}
					@Override
					public void mouseDragged( MouseEvent e )
					{
//						System.out.println("mouseDragged");
					}
				});

		addKeyListener(
				new KeyListener()
				{
					@Override
					public void keyTyped( KeyEvent e )
					{
						System.out.println(" + GameClient::GameField::GameField() --- keyTyped() --- KeyCode: " + e.getKeyCode() );
					}

					@Override
					public void keyReleased( KeyEvent e )
					{
						System.out.println(" + GameClient::GameField::GameField() --- keyReleased() --- KeyCode: " + e.getKeyCode() );
					}

					@Override
					public void keyPressed( KeyEvent e )
					{
						System.out.println(" + GameClient::GameField::GameField() --- keyPressed() --- KeyCode: " + e.getKeyCode() );
					}
				});

		timerStart();
	}

	long elapsedTime = 0;
	public void startGame()
	{
		System.out.println(" + GameClient::GameField::startGame()");

		// start game loop!
		// before play, clear memory (runs garbage collector)
		System.gc();
		System.runFinalization();
		
		BaseTimer bsTimer = new SystemTimer();
		bsTimer.refresh();
		
		while (true)
		{
			System.out.println(" + GameClient::GameField::startGame() --- Looooooooooooop");
			
//			Graphics2D g = bsGraphics.getBackBuffer();
			elapsedTime = bsTimer.sleep();
			repaint();
			
			if ( elapsedTime > 100 )
			{
				// can't lower than 10 fps (1000/100)
				elapsedTime = 100;
			}
		}
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
							baseGameMapX2 = getWidth();
						}
						else
						{
							baseGameMapX1 -= gameMapShiftValue;
							baseGameMapX2 -= gameMapShiftValue;
						}
					}
					if ( isGameMapShift_Right )
					{
						if ( baseGameMapX2 >= baseGameMap.getWidth(null) )
						{
							baseGameMapX2 = baseGameMap.getWidth(null);
							baseGameMapX1 = baseGameMapX2 - getWidth();
						}
						else
						{
							baseGameMapX1 += gameMapShiftValue;
							baseGameMapX2 += gameMapShiftValue;
						}
					}
					if ( isGameMapShift_Up )
					{
						if ( baseGameMapY1 <= 0 )
						{
							baseGameMapY1 = 0;
							baseGameMapY2 = getHeight();
						}
						else
						{
							baseGameMapY1 -= gameMapShiftValue;
							baseGameMapY2 -= gameMapShiftValue;
						}
					}
					if ( isGameMapShift_Down )
					{
						if ( baseGameMapY2 >= baseGameMap.getHeight(null) )
						{
							baseGameMapY2 = baseGameMap.getHeight(null);
							baseGameMapY1 = baseGameMapY2 - getHeight();
						}
						else
						{
							baseGameMapY1 += gameMapShiftValue;
							baseGameMapY2 += gameMapShiftValue;
						}
					}
				}
			});

	//======================================================================================
	//======================================================================================
	@Override
	protected void paintComponent( Graphics g )
	{
//		System.out.println(" + DesktopChatClient::GameField::paintComponent()");

		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor( Color.white );
//		int width = getWidth();
//		int height = getHeight();
//		g.fillRect(0, 0, width, height);
//		g2d.setColor( Color.black );
//		g2d.drawRect( 0, 0, width - 1, height - 1 );

		g2d.drawImage( baseGameMap, 0, 0, getWidth(), getHeight(), baseGameMapX1, baseGameMapY1, baseGameMapX2, baseGameMapY2, null );

		g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.scale( scale, scale );

		for ( Unit unit : list_Unit )
		{
//			System.out.println(" + DesktopChatClient::GameField::paintComponent() --- unit ID: " + unit.getId() + " --- Name: " + unit.getName() + " --- Type: " + unit.getUnitType() );

/*
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
*/
//			BufferedImage[] array_BufferedImages = getImages("CoinAnim.png", 4, 1 );
//			for ( BufferedImage bufferedImage : array_BufferedImages )
//			{
//				g2d.drawImage( array_BufferedImages[1], null, 50, 50 );
//			}

			unit.update( 20 );//elapsedTime );
			unit.render( g2d );

			if ( unit.isDead() ) { continue; }

			unit.checkInteraction();
		}
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

	private static final MediaTracker tracker;
	static {
		// dummy component
		Canvas canvas = new Canvas();
		
		// create media tracker
		tracker = new MediaTracker(canvas);
	}

	private static void waitForResource(Image image) throws Exception {
		if (image == null) {
			throw new NullPointerException();
		}
		
		try {
			tracker.addImage(image, 0);
			tracker.waitForAll();
			if ((tracker.statusID(0, true) & MediaTracker.ERRORED) != 0) {
				throw new RuntimeException();
			}
		}
		catch (Exception e) {
			tracker.removeImage(image, 0);
			throw e;
		}
	}

	/**
	 * Applying mask into image using specified masking color. Any Color in the
	 * image that matches the masking color will be converted to transparent.
	 * 
	 * @param img The source image
	 * @param keyColor Masking color
	 * @return Masked image
	 */
	public static BufferedImage applyMask(Image img, Color keyColor) {
		BufferedImage alpha = createImage(img.getWidth(null), img.getHeight(null), Transparency.BITMASK);
		
		Graphics2D g = alpha.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		for (int y = 0; y < alpha.getHeight(); y++) {
			for (int x = 0; x < alpha.getWidth(); x++) {
				int col = alpha.getRGB(x, y);
				if (col == keyColor.getRGB()) {
					// make transparent
					alpha.setRGB(x, y, col & 0x00ffffff);
				}
			}
		}
		
		return alpha;
	}

	/**
	 * Creates blank image with specified width, height, without transparency
	 * (opaque).
	 * 
	 * @param width image width
	 * @param height image height
	 * @return Blank image.
	 */
	public static BufferedImage createImage_( int width, int height )
	{
		return createImage(width, height, Transparency.OPAQUE);
	}

	/**
	 * Loads and splits image from URL with specified masking color. The images
	 * will be split by specified column and row.
	 * 
	 * @param url image url
	 * @param col column
	 * @param row row
	 * @param keyColor masking color
	 * @return Loaded and splitted images.
	 */
	public static BufferedImage[] getImages( URL url, int col, int row, Color keyColor )
	{
		return splitImages( getImage( url, keyColor ), col, row );
	}

	/**
	 * Loads an image using the specified URL and masking color. This function
	 * will wait until the image has been loaded from file. Note: Using this
	 * function will always return a a new image loaded from file.
	 * 
	 * @param url image url
	 * @param keyColor masking color
	 * @return Loaded image.
	 */
	public static BufferedImage getImage(URL url, Color keyColor)
	{
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(url);
			waitForResource(image);
			
			return applyMask(image, keyColor);
		}
		catch (Exception e) {
			System.err.println("ERROR: Unable to load Image = " + url);
			e.printStackTrace();
			return createImage_(50, 50);
		}
	}

	/**
	 * Creates blank image with specified width, height, and transparency.
	 * 
	 * @param width image width
	 * @param height image height
	 * @param transparency image transparency
	 * @return Blank image.
	 * @see Transparency#OPAQUE
	 * @see Transparency#BITMASK
	 * @see Transparency#TRANSLUCENT
	 */
	public static BufferedImage createImage( int width, int height, int transparency )
	{
		return CONFIG.createCompatibleImage( width, height, transparency );
	}

	/**
	 * Splits a single image into an array of images. The image is cut by
	 * specified column and row.
	 * 
	 * @param image the source image
	 * @param col image column
	 * @param row image row
	 * @return Array of images cutted by specified column and row.
	 */
	public static BufferedImage[] splitImages(BufferedImage image, int col, int row)
	{
		int total = col * row; // total returned images
		int frame = 0; // frame counter
		int i, j;
		int w = image.getWidth() / col, h = image.getHeight() / row;
		BufferedImage[] images = new BufferedImage[total];
		
		for (j = 0; j < row; j++) {
			for (i = 0; i < col; i++) {
				int transparency = image.getColorModel().getTransparency();
				images[frame] = createImage(w, h, transparency);
				Graphics2D g = images[frame].createGraphics();
				g.drawImage( image, 0, 0, w, h, // destination
				        i * w, j * h, (i + 1) * w, (j + 1) * h, // source
				        null );
				g.dispose();
				
				frame++;
			}
		}
		
		return images;
	}
	/**
	 * Loads and returns image strip with specified file using masking color.
	 * Images that have been loaded before will return immediately from cache.
	 * 
	 * @param imagefile the image filename to be loaded
	 * @param col image strip column
	 * @param row image strip row
	 * @return Requested image.
	 * 
	 * @see #getImages(String, int, int, boolean)
	 */
	public static BufferedImage[] getImages( String imagefile, int col, int row )
	{
		return getImages( imagefile, col, row, true );
	}
	/**
	 * Loads and returns image strip with specified file and whether using
	 * masking color or not. Images that have been loaded before will return
	 * immediately from cache.
	 * 
	 * @param imagefile the image filename to be loaded
	 * @param col image strip column
	 * @param row image strip row
	 * @param useMask true, the image is using transparent color
	 * @return Requested image.
	 */
	public static BufferedImage[] getImages( String imagefile, int col, int row, boolean useMask )
	{
		BufferedImage[] image = null;
		if (image == null)
		{
			java.net.URL url = Class.class.getResource("/ru/torment/icons/" + imagefile );
			image = getImages( url, col, row, Color.GRAY );
		}
		return image;
	}


	//======================================================================================
	// Проверка досягаемости других объектов относительно текущего
	//======================================================================================
	public static List<Unit> getEnemysInKillZone( Unit unit )
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
