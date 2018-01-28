package ru.torment.client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class WaitWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private String defaultMessage = "Пожалуйста, подождите...";

	public WaitWindow( final JFrame jFrame, final String mode, String message )
	{
		setSize( new Dimension(300, 170) );
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths  = new int[]{ 1, 0 };
			gridBagLayout.rowHeights    = new int[]{ 0, 0 };
			gridBagLayout.columnWeights = new double[]{ 1.0, Double.MIN_VALUE };
			gridBagLayout.rowWeights    = new double[]{ 1.0, Double.MIN_VALUE };
		getContentPane().setLayout( gridBagLayout );

		if ( message == null || message.isEmpty() )
		{
			message = defaultMessage;
		}

		JLabel label = new JLabel( message );
			label.setBorder( new LineBorder( new Color(0, 153, 255), 2 ) );
			label.setHorizontalAlignment( SwingConstants.CENTER );
			label.setForeground( new Color(0, 102, 255) );
			label.setFont( new Font("Tahoma", Font.BOLD, 14) );
		GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.fill  = GridBagConstraints.BOTH;
			gbc_label.gridx = 0;
			gbc_label.gridy = 0;
		getContentPane().add( label, gbc_label );

		addWindowListener(
				new WindowListener()
				{
					@Override
					public void windowActivated(   WindowEvent e ) {}
					@Override
					public void windowClosed(      WindowEvent e ) {}
					@Override
					public void windowClosing(     WindowEvent e ) {}
					@Override
					public void windowDeactivated( WindowEvent e ) {}
					@Override
					public void windowDeiconified( WindowEvent e ) {}
					@Override
					public void windowIconified(   WindowEvent e ) {}

					@Override
					public void windowOpened(      WindowEvent e )
					{
						System.out.println(" === WaitWindow OPENED ===");

						if ( mode.equals("AUTH") )
						{
							System.out.println(" === AUTH ===");

							( (StartWindow)jFrame ).acceptData();
						}
					}
				});
	}
}
