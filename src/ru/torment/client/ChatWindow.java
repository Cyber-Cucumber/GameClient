package ru.torment.client;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ru.torment.shared.GameRequest;
import ru.torment.shared.Message;
import ru.torment.shared.StartGame;
import ru.torment.shared.StopGame;
import ru.torment.shared.User;


@SuppressWarnings("unused")
public class ChatWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static ChatWindow chatWindow;

	private static ObjectOutputStream objectOutputStream = null;
	private static ObjectInputStream  objectInputStream  = null;
	private static User user = null;
	private static List<User> list_User = null;

	private final static Box verticalBox = Box.createVerticalBox();
	private static JSplitPane splitPane = null;

	public static JTextPane jTextPane;
	public final static DefaultListModel<User> model = new DefaultListModel<User>();
	public final static JList<User> jList = new JList<User>( model );
	public final static CustomListCellRenderer customListCellRenderer = new CustomListCellRenderer();

	private JTextField jTextField_Input = null;
	private JLabel jLabel_UserName;
	
	private static GameField gameField;

	private static JButton jButton_StartGame;
	private static JButton jButton_CloseGame;

	//======================================================================================
	//======================================================================================
	private ChatWindow()
	{
		System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow()");

		setSize( new Dimension(850, 650) );
		setMinimumSize( new Dimension(400, 300) );
		setTitle("Клиент чата");
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths  = new int[]{ 327,  0 };
			gridBagLayout.rowHeights    = new int[]{ 0, 0, 0, 0 };
			gridBagLayout.columnWeights = new double[]{ 1.0,                Double.MIN_VALUE };
			gridBagLayout.rowWeights    = new double[]{ 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout( gridBagLayout );

		jLabel_UserName = new JLabel("Пользователь");
			jLabel_UserName.setForeground( Color.BLUE );
			jLabel_UserName.setHorizontalAlignment( SwingConstants.CENTER );
			jLabel_UserName.setFont( new Font("Tahoma", Font.BOLD, 12) );
//			jLabel_UserName.setPreferredSize( new Dimension(0, 30) );
//			jLabel_UserName.setMaximumSize( new Dimension(32000, 30) );
		getContentPane().add( jLabel_UserName );

		JToolBar jToolBar = new JToolBar();
		GridBagConstraints gbc_JToolBar = new GridBagConstraints();
			gbc_JToolBar.insets = new Insets(5, 5, 5, 5);
			gbc_JToolBar.anchor = GridBagConstraints.WEST;
			gbc_JToolBar.fill   = GridBagConstraints.VERTICAL;
			gbc_JToolBar.gridx  = 0;
			gbc_JToolBar.gridy  = 1;
		getContentPane().add( jToolBar, gbc_JToolBar );

		jButton_StartGame = new JButton("Начать игру");
			jButton_StartGame.setFont( new Font("Tahoma", Font.BOLD, 11) );
			jButton_StartGame.setEnabled(false);
		jToolBar.add( jButton_StartGame );

		jButton_CloseGame = new JButton("Завершить игру");
			jButton_CloseGame.setFont( new Font("Tahoma", Font.BOLD, 11) );
			jButton_CloseGame.setEnabled(false);
		jToolBar.add( jButton_CloseGame );

		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
			gbc_splitPane.insets = new Insets(5, 5, 5, 5);
			gbc_splitPane.fill   = GridBagConstraints.BOTH;
			gbc_splitPane.gridx  = 0;
			gbc_splitPane.gridy  = 2;
		getContentPane().add( splitPane, gbc_splitPane );

		splitPane.setRightComponent( verticalBox );

		JLabel label = new JLabel("Комната чата:");
			label.setHorizontalAlignment( SwingConstants.CENTER );
			label.setFont( new Font("Tahoma", Font.BOLD, 12) );
			label.setPreferredSize( new Dimension(0, 30) );
			label.setMaximumSize( new Dimension(32000, 30) );
		verticalBox.add( label );

		Component verticalStrut = Box.createVerticalStrut(10);
			verticalStrut.setMaximumSize(   new Dimension(32767, 10) );
			verticalStrut.setPreferredSize( new Dimension(0, 10)     );
		verticalBox.add( verticalStrut );

		jTextPane = new JTextPane();
//			jTextPane.setContentType("text/html");
//			jTextPane.setPreferredSize( new Dimension(500, 200) );
			jTextPane.setBorder( UIManager.getBorder("TextField.border") );
//			jTextPane.setMaximumSize( new Dimension(32000, 32000) );
			jTextPane.setEditable(false);

		JScrollPane scrollPane = new JScrollPane( jTextPane );
		verticalBox.add( scrollPane );

		jTextField_Input = new JTextField();
			jTextField_Input.setFont( new Font("Meiryo UI", Font.BOLD, 12) );
			jTextField_Input.setPreferredSize( new Dimension(0, 30) );
			jTextField_Input.setMaximumSize( new Dimension(32000, 30) );
//			jTextField_Input.setColumns(10);
		verticalBox.add( jTextField_Input );

//			jList.setPreferredSize( new Dimension(100, 300) );
			jList.setBorder( UIManager.getBorder("TextField.border") );
//			jList.setMaximumSize( new Dimension(32000, 32000) );
			jList.setMinimumSize( new Dimension(200, 300) );
			jList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			jList.setCellRenderer( customListCellRenderer );

		JScrollPane scrollPane_jList = new JScrollPane( jList );
			splitPane.setLeftComponent( scrollPane_jList );

		Box horizontalBox = Box.createHorizontalBox();
		GridBagConstraints gbc_horizontalBox = new GridBagConstraints();
			gbc_horizontalBox.insets = new Insets(5, 5, 5, 5);
			gbc_horizontalBox.anchor = GridBagConstraints.EAST;
			gbc_horizontalBox.fill   = GridBagConstraints.VERTICAL;
			gbc_horizontalBox.gridx  = 0;
			gbc_horizontalBox.gridy  = 3;
		getContentPane().add( horizontalBox, gbc_horizontalBox );

		JButton jButton_Close = new JButton("Закрыть");
			jButton_Close.setFont( new Font("Tahoma", Font.BOLD, 11) );
		horizontalBox.add( jButton_Close );

		jButton_StartGame.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed( ActionEvent e )
					{
						System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow() --- jButton_StartGame::actionPerformed()");
						try
						{
							objectOutputStream.writeObject( new GameRequest( user, jList.getSelectedValue() ) );
						}
						catch ( IOException ioe )
						{
							System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow() --- jButton_StartGame::actionPerformed() --- IOException");
							System.out.println( ioe.getMessage() );
//							ioe.printStackTrace();
						}
					}
				});

		jButton_CloseGame.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed( ActionEvent e )
					{
						System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow() --- jButton_CloseGame::actionPerformed()");
						closeGame();
						try
						{
							objectOutputStream.writeObject( new StopGame( user ) );
						}
						catch ( IOException ioe )
						{
							System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow() --- jButton_StartGame::actionPerformed() --- IOException");
							System.out.println( ioe.getMessage() );
//							ioe.printStackTrace();
						}
					}
				});

		jButton_Close.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed( ActionEvent e )
					{
//						setVisible(false);
//						jList.removeAll();
//						jList.repaint();
						StartWindow.closeSocket();
//						dispose();
						Runtime.getRuntime().exit(0);
					}
				});

		jTextField_Input.addKeyListener(
				new KeyListener()
				{
					@Override
					public void keyPressed( KeyEvent ke )
					{
//						System.out.println("   ====== KEY: " + ke.getKeyChar() );

						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
						{
							sendMessage( jTextField_Input.getText() );
							jTextField_Input.setText("");
						}
					}

					@Override
					public void keyReleased( KeyEvent ke ){}

					@Override
					public void keyTyped( KeyEvent ke ){}
				});

		jList.addListSelectionListener(
				new ListSelectionListener()
				{
					@Override
					public void valueChanged( ListSelectionEvent e )
					{
						System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow() --- jList::valueChanged()");
						if ( jList.getSelectedValue() == null )
						{
							jButton_StartGame.setEnabled(false);
							return;
						}

						System.out.println(" + DesktopChatClient::ChatWindow::ChatWindow() --- jList::valueChanged() --- selectedValue: " + jList.getSelectedValue().getLogin() );
						if ( !jList.getSelectedValue().equals( user ) && gameField == null )
						{
							jButton_StartGame.setEnabled(true);
						}
						else
						{
							jButton_StartGame.setEnabled(false);
						}
					}
				});

		DefaultCaret caret = (DefaultCaret) jTextPane.getCaret();
		caret.setUpdatePolicy( DefaultCaret.NEVER_UPDATE );
	}

	//======================================================================================
	//======================================================================================
	public static ChatWindow getInstance()
	{
		System.out.println(" + DesktopChatClient::ChatWindow::getInstance()");
		if ( chatWindow == null ) { chatWindow = new ChatWindow(); }
		return chatWindow;
	}

	//======================================================================================
	//======================================================================================
	public void start( ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, User user )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::start()");

		this.objectInputStream  = objectInputStream;
		this.objectOutputStream = objectOutputStream;
		ChatWindow.user = user;

		jLabel_UserName.setText( user.getLogin() );

		StartWindow.centerFrameOnScreen( this );
		setVisible(true);
		init();
	}

	//======================================================================================
	//======================================================================================
	public void init()
	{
		System.out.println(" + DesktopChatClient::ChatWindow::init()");

		customListCellRenderer.clearMapCheckedItems();

		StyledDocument document = (StyledDocument) jTextPane.getDocument();

		Style style_Simple     = document.addStyle("SimpleStyle",     null );
		Style style_Simple_b   = document.addStyle("SimpleStyle_b",   null );
		Style style_Error      = document.addStyle("ErrorStyle",      null );
		Style style_Error_b    = document.addStyle("ErrorStyle_b",    null );
		Style style_NoUpdate   = document.addStyle("NoUpdateStyle",   null );
		Style style_NoUpdate_b = document.addStyle("NoUpdateStyle_b", null );

		StyleConstants.setFontSize( style_Simple,     14 );
		StyleConstants.setFontSize( style_Simple_b,   14 );
		StyleConstants.setFontSize( style_Error,      14 );
		StyleConstants.setFontSize( style_Error_b,    14 );
		StyleConstants.setFontSize( style_NoUpdate,   14 );
		StyleConstants.setFontSize( style_NoUpdate_b, 14 );

		StyleConstants.setForeground( style_Error,      Color.RED  );
		StyleConstants.setForeground( style_Error_b,    Color.RED  );
		StyleConstants.setForeground( style_NoUpdate,   Color.BLUE );
		StyleConstants.setForeground( style_NoUpdate_b, Color.BLUE );

		StyleConstants.setBold( style_Simple_b,   true );
		StyleConstants.setBold( style_Error_b,    true );
		StyleConstants.setBold( style_NoUpdate_b, true );

		int countUploadInfObj_success = 0;
		int countUploadInfObj_error   = 0;

		model.addElement( user );

//		String fileName = "Tra-la-la.rar";
//		for (int i = 1; i <= 300; i++)
//		{
//			model.addElement( i + " " + fileName );
//		}
//		int currentUploadInfObj = 0;
//		for(int i = 1; i <= 100; i++)
//		{
//			currentUploadInfObj++;
//			logging( document, fileName, style_Simple,   style_Simple_b,   i + " Документ  ",         "  обновлён\n",       "check", currentUploadInfObj );
//			currentUploadInfObj++;
//			logging( document, fileName, style_NoUpdate, style_NoUpdate_b, i + " Документ  ",         "  опубликован\n",    "abort", currentUploadInfObj );
//			currentUploadInfObj++;
//			logging( document, fileName, style_Error,    style_Error_b,    i + " ОШИБКА: Документ  ", "  не опубликован\n", "error", currentUploadInfObj );
//		}

//		publicationEnd( document, style_Simple, countUploadInfObj_success, countUploadInfObj_error, "завершён", "Публикация документов завершена!\n" );
	}

	//======================================================================================
	//======================================================================================
	private void sendMessage( String msg )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::sendMessage()");
		System.out.println("Отправляю сообщение на сервер...");
		try
		{
//			out.writeUTF( message ); // отсылаем введенную строку текста серверу.
			// отсылаем введенную строку текста серверу
			objectOutputStream.writeObject( new Message( user, msg ) );
//			objectOutputStream.flush(); // заставляем поток закончить передачу данных.
		}
		catch ( IOException e )
		{
			String time = new Time( Calendar.getInstance().getTime().getTime() ).toString();
			jTextPane.setText( jTextPane.getText() + "\n" + "[ " + time + " ] " + user.getLogin() + " : " + msg + " (Сервер не доступен. Сообщение не было отправлено.)" );
			System.out.println( e.getMessage() );
		}
		System.out.println("Сообщение отправлено");
	}

	//======================================================================================
	//======================================================================================
	static void sendMessage( Message message )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::sendMessage()");
		try
		{
			objectOutputStream.writeObject( message );
		}
		catch ( IOException e )
		{
			System.out.println(" + DesktopChatClient::ChatWindow::sendMessage() --- IOException");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
		}
	}

	//======================================================================================
	//======================================================================================
	static void logging( StyledDocument document,
						  String fileName,
						  Style style_message,
						  Style style_fileName,
						  String message_beg,
						  String message_end,
						  String state,
						  int currentUploadInfObj )
	{
		try
		{
			document.insertString( document.getLength(), message_beg, style_message  );
			document.insertString( document.getLength(), fileName,    style_fileName );
			document.insertString( document.getLength(), message_end, style_message  );
		}
		catch ( BadLocationException e )
		{
			e.printStackTrace();
		}

		int indexListElement = currentUploadInfObj - 1;
		String listItem = model.getElementAt( indexListElement ).toString();
		customListCellRenderer.setItemState( jList, listItem, indexListElement, state );
	}

	//======================================================================================
	//======================================================================================
	static void logging( String msg )
	{
		jTextPane.setText( jTextPane.getText() + "\n" + msg );
	}

	//======================================================================================
	//======================================================================================
	private void publicationEnd( StyledDocument document,
								 Style style_Simple,
								 int countUploadInfObj_success,
								 int countUploadInfObj_error,
								 String dlgMsgEnd,
								 String msgEnd )
	{
		String  message  = "==========================================\n";
				message += msgEnd;

		try
		{
			document.insertString( document.getLength(), message, style_Simple );
		}
		catch ( BadLocationException e )
		{
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog( null, String.format("Процесс публикации документов \"ТС-666\" в ИБД ИО %s\n\nОпубликовано документов: %s\nНе опубликовано из-за ошибок: %s", dlgMsgEnd, countUploadInfObj_success, countUploadInfObj_error ), "Публикация документов \"ТС-666\" в ИБД ИО", JOptionPane.INFORMATION_MESSAGE, null );
	}

	//======================================================================================
	//======================================================================================
	static void setListUser( List<User> list_User )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::setListUser()");
		System.out.println(" + DesktopChatClient::ChatWindow::setListUser() --- list_User SIZE: " + list_User.size() );
		ChatWindow.list_User = list_User;
		for ( User user : list_User )
		{
			model.addElement( user );
		}
	}

	//======================================================================================
	// Добавить нового пользователя в список всех пользователей чата
	//======================================================================================
	static void addUserToListUser( User user )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::addUserToListUser()");
		System.out.println(" + DesktopChatClient::ChatWindow::addUserToListUser() --- user: " + user.getLogin() );
		list_User.add( user );
		model.addElement( user );
	}

	//======================================================================================
	// Удалить пользователя из списка всех пользователей чата
	//======================================================================================
	static void removeUserFromListUser( User user )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::removeUserFromListUser()");
		System.out.println(" + DesktopChatClient::ChatWindow::removeUserFromListUser() --- user: " + user.getLogin() );
		list_User.remove( user );
		jList.clearSelection();
		model.removeElement( user );

		// Если запущена игра с этим пользователем
		if ( gameField != null && gameField.getSecondUser() != null && gameField.getSecondUser().equals( user ) )
		{
			closeGame();
		}
	}

	//======================================================================================
	// Закрыть игру
	//======================================================================================
	static void closeGame()
	{
//		gameField.setSecondUser( null );
		gameField = null;
		GameField.destroyInstance();
		splitPane.setRightComponent( verticalBox );
		jButton_StartGame.setEnabled(true);
		jButton_CloseGame.setEnabled(false);
	}

	//======================================================================================
	// Открыть диалог "Приглашение сыграть"
	//======================================================================================
	static void openNewGameRequestDialog( User secondUser )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::openNewGameRequestDialog()");
		int res = JOptionPane.showConfirmDialog( ChatWindow.getInstance(), "Пользователь [" + secondUser.getLogin() + "] приглашает Вас сыграть с ним. Согласиться?", "Приглашение сыграть", JOptionPane.YES_NO_OPTION );
		System.out.println(" + DesktopChatClient::ChatWindow::openNewGameRequestDialog() --- yes/no: " + res );
		if ( res == 0 )
		{
//			verticalBox.setVisible(false);
			gameField = GameField.getInstance();
				gameField.setColorMy( Color.GREEN );
				gameField.setSecondUser( secondUser );
			splitPane.setRightComponent( gameField );
			jButton_StartGame.setEnabled(false);
			jButton_CloseGame.setEnabled(true);
			try
			{
				objectOutputStream.writeObject( new StartGame( user, secondUser ) );
			}
			catch ( IOException e )
			{
				System.out.println(" + DesktopChatClient::ChatWindow::openNewGameRequestDialog() --- IOException");
				System.out.println( e.getMessage() );
//				e.printStackTrace();
			}
		}
	}

	//======================================================================================
	// Начало игры
	//======================================================================================
	static void startGame( User secondUser )
	{
		System.out.println(" + DesktopChatClient::ChatWindow::startGame()");
		gameField = GameField.getInstance();
			gameField.setColorMy( Color.RED );
			gameField.setSecondUser( secondUser );
		splitPane.setRightComponent( gameField );

		jButton_StartGame.setEnabled(false);
		jButton_CloseGame.setEnabled(true);
	}
}


//======================================================================================
//======================================================================================
class CustomListCellRenderer extends DefaultListCellRenderer
{
	private static final long serialVersionUID = 1L;

	Map<Integer,String> map_checkedItems = new HashMap<Integer,String>();

	public static String iconPath = "/ru/torment/icons/";

	public final ImageIcon imageIcon_ListEmptyItem = createImageIcon( iconPath + "empty_24.png", "");
	public final ImageIcon imageIcon_ListCheckItem = createImageIcon( iconPath + "check_24.png", "");
	public final ImageIcon imageIcon_ListAbortItem = createImageIcon( iconPath + "abort_24.png", "");
	public final ImageIcon imageIcon_ListErrorItem = createImageIcon( iconPath + "error_24.png", "");


	@Override
	public Component getListCellRendererComponent( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus )
	{
//		System.out.println("   === List Renderer === value: " + value.toString() );

		Component component = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

		JLabel label = (JLabel) component;
			label.setText( ((User)value).getLogin() );

		Icon icon = imageIcon_ListEmptyItem;

		Iterator< Entry<Integer,String> > it = map_checkedItems.entrySet().iterator();
		while ( it.hasNext() )
		{
			Map.Entry<Integer,String> pairs = it.next();

			String state = pairs.getValue();

			if ( pairs.getKey() == index )
			{
//				System.out.println("   === List Renderer!!! === value: " + value.toString() );
				if ( state.equals("check") )
				{
					icon = imageIcon_ListCheckItem;
				}
				else if ( state.equals("abort") )
				{
					icon = imageIcon_ListAbortItem;
				}
				else if ( state.equals("error") )
				{
					icon = imageIcon_ListErrorItem;
				}
			}
		}

		label.setIcon( icon );

		return label;
	}


	public void setItemState( JList<?> list, Object value, int index, String state )
	{
		map_checkedItems.put( index, state );
		getListCellRendererComponent( list, value, index, false, false );
		list.repaint();
	}


	public void clearMapCheckedItems()
	{
		map_checkedItems.clear();
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
}
