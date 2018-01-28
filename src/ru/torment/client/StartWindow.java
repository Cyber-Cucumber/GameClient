package ru.torment.client;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import ru.torment.shared.User;

import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StartWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static int serverPort; // здесь обязательно нужно указать порт к которому привязывается сервер
	private static String address; // это IP-адрес компьютера, где исполняется наша серверная программа

	static Socket socket = null;
	private static ObjectOutputStream objectOutputStream = null;
	private static ObjectInputStream  objectInputStream  = null;

	private final static MouseAdapter mouseAdapter = new MouseAdapter() {};

	JFrame jFrame_AuthWindow;

	private JTextField jTextField_host;
	private JTextField jTextField_port;
	private JTextField jTextField_login;
//	private JPasswordField jPasswordField_passwd;
//	private JComboBox jComboBox_grif;
//
//	Integer grif = -1;

	WaitWindow waitWindow;

	static User user = null;


	//======================================================================================
	//======================================================================================
	public StartWindow()
	{
		setName("jFrame_auth");
//		setSize(new Dimension(300, 200));
		setSize(new Dimension(300, 150));
//		setMaximumSize(new Dimension(300, 170));
//		setMinimumSize(new Dimension(300, 170));
		setMaximumSize(new Dimension(300, 150));
		setMinimumSize(new Dimension(300, 150));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Аутентификация");
		setResizable(false);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		Box verticalBox = Box.createVerticalBox();
		getContentPane().add(verticalBox);

		Component verticalStrut_4 = Box.createVerticalStrut(5);
		verticalBox.add(verticalStrut_4);

		Box horizontalBox_host = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_host);

		JLabel jLabel_host = new JLabel("Хост:   ");
			jLabel_host.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabel_host.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_host.setAlignmentX(Component.RIGHT_ALIGNMENT);
			jLabel_host.setMinimumSize(   new Dimension(70, 14) );
			jLabel_host.setMaximumSize(   new Dimension(70, 14) );
			jLabel_host.setPreferredSize( new Dimension(70, 14) );
		horizontalBox_host.add(jLabel_host);

			jTextField_host = new JTextField();
			jTextField_host.setMaximumSize(new Dimension(200, 20));
			jTextField_host.setMinimumSize(new Dimension(200, 20));
			jTextField_host.setColumns(10);
		horizontalBox_host.add(jTextField_host);

		Component verticalStrut = Box.createVerticalStrut(5);
		verticalBox.add(verticalStrut);

		Box horizontalBox_port = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_port);

		JLabel jLabel_port = new JLabel(" Порт:   ");
			jLabel_port.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabel_port.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_port.setAlignmentX(Component.RIGHT_ALIGNMENT);
			jLabel_port.setMinimumSize(   new Dimension(70, 14) );
			jLabel_port.setMaximumSize(   new Dimension(70, 14) );
			jLabel_port.setPreferredSize( new Dimension(70, 14) );
		horizontalBox_port.add(jLabel_port);

			jTextField_port = new JTextField();
			jTextField_port.setMinimumSize(new Dimension(200, 20));
			jTextField_port.setMaximumSize(new Dimension(200, 20));
			jTextField_port.setColumns(10);
		horizontalBox_port.add(jTextField_port);

		Component verticalStrut_1 = Box.createVerticalStrut(5);
		verticalBox.add(verticalStrut_1);

		Box horizontalBox_login = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_login);

		JLabel jLabel_login = new JLabel("Логин:   ");
			jLabel_login.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabel_login.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_login.setAlignmentX(Component.RIGHT_ALIGNMENT);
			jLabel_login.setPreferredSize( new Dimension(70, 14) );
			jLabel_login.setMaximumSize(   new Dimension(70, 14) );
			jLabel_login.setMinimumSize(   new Dimension(70, 14) );
		horizontalBox_login.add(jLabel_login);

			jTextField_login = new JTextField();
			jTextField_login.setMinimumSize(new Dimension(200, 20));
			jTextField_login.setMaximumSize(new Dimension(200, 20));
			jTextField_login.setColumns(10);
		horizontalBox_login.add(jTextField_login);

		Component verticalStrut_2 = Box.createVerticalStrut(5);
		verticalBox.add(verticalStrut_2);

		Box horizontalBox_passwd = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_passwd);

//		JLabel jLabel_passwd = new JLabel("Пароль:   ");
//			jLabel_passwd.setFont(new Font("Tahoma", Font.BOLD, 11));
//			jLabel_passwd.setPreferredSize( new Dimension(70, 14) );
//			jLabel_passwd.setMinimumSize(   new Dimension(70, 14) );
//			jLabel_passwd.setMaximumSize(   new Dimension(70, 14) );
//			jLabel_passwd.setHorizontalAlignment(SwingConstants.RIGHT);
//			jLabel_passwd.setAlignmentX(Component.RIGHT_ALIGNMENT);
//		horizontalBox_passwd.add(jLabel_passwd);
//
//			jPasswordField_passwd = new JPasswordField();
//			jPasswordField_passwd.setMinimumSize(new Dimension(200, 20));
//			jPasswordField_passwd.setMaximumSize(new Dimension(200, 20));
//		horizontalBox_passwd.add(jPasswordField_passwd);
//
//		Component verticalStrut_3 = Box.createVerticalStrut(5);
//		verticalBox.add(verticalStrut_3);
//		
//		Box horizontalBox_grif = Box.createHorizontalBox();
//		verticalBox.add(horizontalBox_grif);
//		
//		JLabel jLabel_grif = new JLabel("Гриф:   ");
//			jLabel_grif.setPreferredSize( new Dimension(70, 14) );
//			jLabel_grif.setMinimumSize(   new Dimension(70, 14) );
//			jLabel_grif.setMaximumSize(   new Dimension(70, 14) );
//			jLabel_grif.setHorizontalAlignment(SwingConstants.RIGHT);
//			jLabel_grif.setFont(new Font("Tahoma", Font.BOLD, 11));
//			jLabel_grif.setAlignmentX(1.0f);
//		horizontalBox_grif.add(jLabel_grif);
//
//			String grifs[] = { "", "Не секретно", "Секретно", "Сов. секретно" };
//			jComboBox_grif = new JComboBox( grifs );
//			jComboBox_grif.setMinimumSize(new Dimension(200, 20));
//			jComboBox_grif.setMaximumSize(new Dimension(200, 20));
//		horizontalBox_grif.add(jComboBox_grif);

		Component verticalStrut_5 = Box.createVerticalStrut(5);
		verticalBox.add(verticalStrut_5);

		Box horizontalBox_buttons = Box.createHorizontalBox();
			horizontalBox_buttons.setLocale(new Locale("ru", "RU"));
			horizontalBox_buttons.setSize(new Dimension(200, 30));
			horizontalBox_buttons.setPreferredSize( new Dimension(200, 30) );
			horizontalBox_buttons.setMinimumSize(   new Dimension(200, 30) );
			horizontalBox_buttons.setMaximumSize(   new Dimension(260, 30) );
			horizontalBox_buttons.setAlignmentY(Component.CENTER_ALIGNMENT);
		verticalBox.add(horizontalBox_buttons);

		JButton jButton_OK = new JButton("Ввод");
			jButton_OK.setFont(new Font("Tahoma", Font.BOLD, 11));
			jButton_OK.setMinimumSize(   new Dimension(100, 30) );
			jButton_OK.setMaximumSize(   new Dimension(100, 30) );
			jButton_OK.setPreferredSize( new Dimension(100, 30) );
		horizontalBox_buttons.add(jButton_OK);

		Component horizontalStrut = Box.createHorizontalStrut(50);
			horizontalStrut.setMaximumSize(new Dimension(150, 20));
		horizontalBox_buttons.add(horizontalStrut);

		JButton jButton_Close = new JButton("Закрыть");
			jButton_Close.setFont(new Font("Tahoma", Font.BOLD, 11));
			jButton_Close.setMinimumSize(   new Dimension(100, 30) );
			jButton_Close.setMaximumSize(   new Dimension(100, 30) );
			jButton_Close.setPreferredSize( new Dimension(100, 30) );
		horizontalBox_buttons.add(jButton_Close);

		jTextField_host.setDocument(       new JTextFieldFilter("[12]?\\d?\\d?\\.?[12]?\\d?\\d?\\.?[12]?\\d?\\d?\\.?[12]?\\d?\\d?") );
		jTextField_port.setDocument(       new JTextFieldFilter("\\d{0,5}") );
		jTextField_login.setDocument(      new JTextFieldFilter("\\w+") );
//		jPasswordField_passwd.setDocument( new JTextFieldFilter("\\w+") );

		jButton_OK.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						checkEnteredData();
//						acceptData();
					}
				});

		jButton_Close.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setVisible(false);
						dispose();
					}
				});

		jTextField_host.addKeyListener(
				new KeyListener()
				{
					@Override
					public void keyPressed( KeyEvent ke )
					{
//						System.out.println("   ====== KEY: " + ke.getKeyChar() );

						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
						{
							checkEnteredData();
//							acceptData();
						}
					}

					@Override
					public void keyReleased( KeyEvent ke )
					{
//						System.out.println("   ====== KEY: " + ke.getKeyChar() );

						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
						{
						}
					}

					@Override
					public void keyTyped( KeyEvent ke )
					{
					}
				});

		jTextField_port.addKeyListener(
				new KeyListener()
				{
					@Override
					public void keyPressed( KeyEvent ke )
					{
//						System.out.println("   ====== KEY: " + ke.getKeyChar() );

						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
						{
							checkEnteredData();
//							acceptData();
						}
					}

					@Override
					public void keyReleased( KeyEvent ke )
					{
					}

					@Override
					public void keyTyped( KeyEvent ke )
					{
					}
				});

		jTextField_login.addKeyListener(
				new KeyListener()
				{
					@Override
					public void keyPressed( KeyEvent ke )
					{
//						System.out.println("   ====== KEY: " + ke.getKeyChar() );

						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
						{
							checkEnteredData();
//							acceptData();
						}
					}

					@Override
					public void keyReleased( KeyEvent ke )
					{
					}

					@Override
					public void keyTyped( KeyEvent ke )
					{
					}
				});

//		jPasswordField_passwd.addKeyListener(
//				new KeyListener()
//				{
//					@Override
//					public void keyPressed( KeyEvent ke )
//					{
////						System.out.println("   ====== KEY: " + ke.getKeyChar() );
//
//						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
//						{
//							checkEnteredData();
////							acceptData();
//						}
//					}
//
//					@Override
//					public void keyReleased( KeyEvent ke )
//					{
//					}
//
//					@Override
//					public void keyTyped( KeyEvent ke )
//					{
//					}
//				});
//
//		jComboBox_grif.addKeyListener(
//				new KeyListener()
//				{
//					@Override
//					public void keyPressed( KeyEvent ke )
//					{
////						System.out.println("   ====== KEY: " + ke.getKeyChar() );
//
//						if ( ke.getKeyCode() == KeyEvent.VK_ENTER )
//						{
//							checkEnteredData();
////							acceptData();
//						}
//					}
//
//					@Override
//					public void keyReleased( KeyEvent ke )
//					{
//					}
//
//					@Override
//					public void keyTyped( KeyEvent ke )
//					{
//					}
//				});

		centerFrameOnScreen( this );

		// Для тестирования
		jTextField_host.setText("127.0.0.1");
		jTextField_port.setText("54321");
		jTextField_login.setText("User1");
//		jPasswordField_passwd.setText("12345678");
//		jComboBox_grif.setSelectedIndex(1);
	}


	//======================================================================================
	//======================================================================================
	public boolean checkEnteredData()
	{
//		String password = String.valueOf( jPasswordField_passwd.getPassword() );

		String authErrorMsg = "";
		if ( jTextField_host.getText().isEmpty()  ) { authErrorMsg += "  - не указан хост\n";  }
		if ( jTextField_port.getText().isEmpty()  ) { authErrorMsg += "  - не указан порт\n";  }
		if ( jTextField_login.getText().isEmpty() ) { authErrorMsg += "  - не введён логин\n"; }

//		if ( password.isEmpty() )
//		{
//			authErrorMsg += "  - не введён пароль\n";
//		}
//
//		Integer grif = -1;
//
//		String selectedGrif = jComboBox_grif.getSelectedItem().toString();
//		System.out.println("   === GRIF: " + selectedGrif );
//
//		if      ( selectedGrif.equals("Не секретно"  ) ) { grif = 0; }
//		else if ( selectedGrif.equals("Секретно"     ) ) { grif = 2; }
//		else if ( selectedGrif.equals("Сов. секретно") ) { grif = 3; }
//
//		System.out.println("   === GRIF: " + grif );
//
//		if ( grif == -1 )
//		{
//			authErrorMsg += "  - не указан уровень секретности\n";
//		}

		if ( !authErrorMsg.isEmpty() )
		{
			authErrorMsg = "Ошибка аутентификации:\n" + authErrorMsg;
			JOptionPane.showMessageDialog( null, authErrorMsg, "Аутентификация", JOptionPane.ERROR_MESSAGE, null );
			return false;
		}

//		this.grif = grif;

		waitWindow = new WaitWindow( this, "AUTH", null );
		waitWindow.setVisible(true);
		centerFrameOnScreen( waitWindow );

		return true;
	}


	//======================================================================================
	//======================================================================================
	public void acceptData()
	{
		boolean isAuth = false;

//		String password = String.valueOf( jPasswordField_passwd.getPassword() );

		setVisible(false);

//		setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
		// Надо делать так, потому что приложение во время смены курсора
		// будет не доступно в любом случае (даже если произошла ошибка)
//		startWaitCursor( getRootPane() );
		startWaitCursor( waitWindow.getRootPane() );

		ChatWindow chatWindow = ChatWindow.getInstance();

		String connectStatus = connectToServer();
		if ( connectStatus.isEmpty() )
		{
			System.out.println(" + DesktopChatClient::StartWindow::acceptData() --- OK");
			isAuth = true;
		}
		else if ( connectStatus.contains("Connection refused") )
		{
			System.out.println(" + DesktopChatClient::StartWindow::acceptData() --- Connection refused");
		}
		else if ( connectStatus.contains("Connection timed out") )
		{
			System.out.println(" + DesktopChatClient::StartWindow::acceptData() --- Connection timed out");
		}
		else
		{
			System.out.println(" + DesktopChatClient::StartWindow::acceptData() --- " + connectStatus );
		}

		if ( isAuth )
		{
			setVisible(false);

			user = new User( jTextField_login.getText() );
			chatWindow.start( objectInputStream, objectOutputStream, user );

//			setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
//			stopWaitCursor( getRootPane() );
			stopWaitCursor( waitWindow.getRootPane() );

			waitWindow.setVisible(false);
			waitWindow.dispose();

			dispose();
		}
		else
		{
//			stopWaitCursor( getRootPane() );
			stopWaitCursor( waitWindow.getRootPane() );

			waitWindow.setVisible(false);
			waitWindow.dispose();

//			JOptionPane.showMessageDialog( null, "Ошибка аутентификации\n\nВозможные причины:\n - Сервер не доступен\n - Не правильно введён логин\n - Не правильно введён пароль", "Ошибка аутентификации", JOptionPane.ERROR_MESSAGE, null );
			JOptionPane.showMessageDialog( null, "Ошибка подключения к серверу\nСервер не доступен, либо введён не корректный IP-адрес или номер порта\n\n[ " + connectStatus + " ]", "Ошибка подключения к серверу", JOptionPane.ERROR_MESSAGE, null );
			setVisible(true);
		}
	}


	//======================================================================================
	//======================================================================================
	public static void startWaitCursor( JComponent component )
	{
		RootPaneContainer rootPaneContainer = ( (RootPaneContainer) component.getTopLevelAncestor() );
			rootPaneContainer.getGlassPane().setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
			rootPaneContainer.getGlassPane().addMouseListener( mouseAdapter );
			rootPaneContainer.getGlassPane().setVisible(true);
	}


	//======================================================================================
	//======================================================================================
	public static void stopWaitCursor( JComponent component )
	{
		RootPaneContainer rootPaneContainer = ( (RootPaneContainer) component.getTopLevelAncestor() );
			rootPaneContainer.getGlassPane().setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
			rootPaneContainer.getGlassPane().removeMouseListener( mouseAdapter );
			rootPaneContainer.getGlassPane().setVisible(false);
	}


	//======================================================================================
	// Позиционирует frame по центру экрана
	//======================================================================================
	public static void centerFrameOnScreen( final Window frame )
	{
		positionFrameOnScreen( frame, 0.5, 0.5 );
	}


	//======================================================================================
	// Позиционирует фрэйм в мето на экране, расчитаное относительно размера экрана
	// @param: frame
	//			фрэйм
	// @param: horizontalPercent
	//			относительное расположение фрэйма по горизонтали (от 0.0 до 1.0, где 0.5 - центр экрана).
	// @param: verticalPercent
	//			относительное расположение фрэйма по вертикали (от 0.0 до 1.0, где 0.5 - центр экрана).
	//======================================================================================
	public static void positionFrameOnScreen( final Window frame, final double horizontalPercent, final double verticalPercent )
	{
		final Rectangle s = getMaximumWindowBounds();
		final Dimension f = frame.getSize();
		final int w = Math.max( s.width  - f.width,  0 );
		final int h = Math.max( s.height - f.height, 0 );
		final int x = (int) (horizontalPercent * w) + s.x;
		final int y = (int) (verticalPercent   * h) + s.y;
		frame.setBounds( x, y, f.width, f.height );
	}


	//======================================================================================
	// Вычисляет максимальные размеры текущего устройства вывода (экрана)
	// @return максимальные размеры текущего устройства вывода (экрана)
	//======================================================================================
	public static Rectangle getMaximumWindowBounds()
	{
		final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

		try
		{
			final Method method = GraphicsEnvironment.class.getMethod("getMaximumWindowBounds", (Class[]) null );
			return (Rectangle) method.invoke( localGraphicsEnvironment, (Object[]) null );
		}
		catch ( Exception e )
		{}

		final Dimension s = Toolkit.getDefaultToolkit().getScreenSize();

		return new Rectangle( 0, 0, s.width, s.height );
	}


	//======================================================================================
	//======================================================================================
	private String connectToServer()
	{
		address    = jTextField_host.getText();
		serverPort = Integer.parseInt( jTextField_port.getText() );

		try
		{
			InetAddress ipAddress = InetAddress.getByName( address ); // создаем объект который отображает вышеописанный IP-адрес
			System.out.println("Создаю сокет " + address + ":" + serverPort );
			socket = new Socket( ipAddress, serverPort ); // создаем сокет используя IP-адрес и порт сервера
			System.out.println("Сокет создан");

			// Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом
			InputStream  inputStream  = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			// Конвертируем потоки в другой тип, чтоб легче обрабатывать сообщения
			//------------------------------------------------------------------------------------------------------------------------------
			// Есть одна особенность в работе с объектовыми потоками сокета (Только с объектовыми! -> ObjectOutputStream, ObjectInputStream).
			// При создании входного объектового потока (ObjectInputStream) он ждёт неких данных («шапки» потока) для инициализации,
			// которые приходят автоматически при создании выходного объектового потока (ObjectOutputStream) сокета на другой стороне соединения.
			// Именно поэтому первым создаётся выходной поток (ObjectOutputStream), поскольку на серверной стороне первым создаётся входной (ObjectInputStream).
			// Если первм попытаться создать входной объектовый поток (ObjectInputStream), программа на этом месте зависнет (будет вечно ожидать инициализации).
			//------------------------------------------------------------------------------------------------------------------------------
			objectOutputStream = new ObjectOutputStream( outputStream );
			objectInputStream  = new ObjectInputStream(  inputStream  );

			// Сразу после установления соединения с сервером создаём "слушателя" сообщений от сервера
			new ServerListenerThread( objectOutputStream, objectInputStream );
		}
		catch ( ConnectException e )
		{
			System.out.println(" + DesktopChatClient::StartWindow::connectToServer() --- ConnectException");
			System.out.println( e.getMessage() );
			closeSocket();
			return e.getMessage();
		}
		catch ( Exception e )
		{
			System.out.println(" + DesktopChatClient::StartWindow::connectToServer() --- Exception");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
			closeSocket();
			return e.getMessage();
		}

		return "";
	}


	//======================================================================================
	//======================================================================================
	static void closeSocket()
	{
		System.out.println(" + DesktopChatClient::StartWindow::closeSocket()");
		try
		{
			if ( socket != null )
			{
				System.out.println(" + DesktopChatClient::StartWindow::closeSocket() --- socket != null");
				socket.close();
				System.out.println(" + DesktopChatClient::StartWindow::closeSocket() --- socket CLOSE");
			}
		}
		catch ( IOException e )
		{
			System.out.println(" + DesktopChatClient::StartWindow::closeSocket() --- IOException");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
		}
	}
}


//======================================================================================
//======================================================================================
class JTextFieldFilter extends PlainDocument
{
	private static final long serialVersionUID = 1L;

//	public static final String NUMERIC = "0123456789";
//
//	public static final String CHAR_RU_LOWER = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя";
//	public static final String CHAR_RU_UPPER = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ";
//	public static final String CHAR_RU_FULL  = CHAR_RU_LOWER + CHAR_RU_UPPER;
//
//	public static final String CHAR_EN_LOWER = "abcdefghijklmnopqrstuvwxyz";
//	public static final String CHAR_EN_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//	public static final String CHAR_EN_FULL  = CHAR_EN_LOWER + CHAR_EN_UPPER;
//
//	public static final String FULL_RU = NUMERIC + CHAR_RU_FULL;
//	public static final String FULL_EN = NUMERIC + CHAR_EN_FULL;

	protected String regExp = null;

	public JTextFieldFilter( String regExp )
	{
		this.regExp = regExp;
	}

	public void insertString( int offset, String str, AttributeSet attr ) throws BadLocationException
	{
		String currentText    = getText( 0, getLength() );
		String beforeOffset   = currentText.substring( 0, offset );
		String afterOffset    = currentText.substring( offset, currentText.length() );
		String proposedResult = beforeOffset + str + afterOffset;

		Pattern pattern = Pattern.compile( regExp );
		Matcher matcher = pattern.matcher( proposedResult );

		if ( matcher.matches() )
		{
			super.insertString( offset, str, attr );
		}
	}
}
