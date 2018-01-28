package ru.torment.client;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame
{
	public static JLabel jLabel = null;

	public GameFrame()
	{
		GameField gameField = GameField.getInstance();
			gameField.setColorMy( Color.RED );

		JFrame jFrame = new JFrame("Game");
			jFrame.setSize( new Dimension(1000, 750) );
			jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

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
	}
}
