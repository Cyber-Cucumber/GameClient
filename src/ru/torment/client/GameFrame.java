package ru.torment.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.torment.shared.Unit;
import ru.torment.shared.UnitType;

public class GameFrame
{
	public static JLabel jLabel = null;

	WindowedMode windowedMode;
	public GameFrame()
	{
//		windowedMode = new WindowedMode( new Dimension(1000, 750), true );
//		startGame();

		GameField gameField = GameField.getInstance();
			gameField.setColorMy( Color.RED );

		JFrame jFrame = new JFrame("Game");
			jFrame.setSize( new Dimension(1000, 750) );
			jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//			jFrame.setIgnoreRepaint(true);

		JPanel panel = new JPanel();
		panel.add( gameField );

		final JButton button = new JButton("Start");
		panel.add( button );

		jLabel = new JLabel();
			jLabel.setForeground( Color.RED );
			jLabel.setPreferredSize( new Dimension(150, 50) );
		panel.add( jLabel );

		jFrame.getContentPane().add( panel );
		jFrame.setVisible(true);

		button.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed( ActionEvent e )
					{
						gameField.startGame();
					}
				} );

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

		Unit unit = new Unit( UnitType.BMP, "Ball BMP", Color.BLUE, 100D, 100D, 20, 10, 3 );

		while (true)
		{
			System.out.println(" + GameClient::GameField::startGame() --- Looooooooooooop");
			
			Graphics2D g2d = windowedMode.getBackBuffer();
			g2d.setColor( Color.white );
			int width = 500;
			int height = 500;
			g2d.fillRect(0, 0, width, height);
			g2d.setColor( Color.black );
			g2d.drawRect( 0, 0, width - 1, height - 1 );

			g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2d.scale( 1.0, 1.0 );
			unit.render(g2d);

			elapsedTime = bsTimer.sleep();

			if ( elapsedTime > 100 )
			{
				// can't lower than 10 fps (1000/100)
				elapsedTime = 100;
			}
		}
	}
}
