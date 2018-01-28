package ru.torment.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import ru.torment.shared.GameData;
import ru.torment.shared.GameRequest;
import ru.torment.shared.InitMessage;
import ru.torment.shared.Message;
import ru.torment.shared.NewUser;
import ru.torment.shared.Ping;
import ru.torment.shared.StartGame;
import ru.torment.shared.StopGame;
import ru.torment.shared.User;
import ru.torment.shared.UserOut;

public class ServerListenerThread implements Runnable
{
	private Thread thread = null;
//	private Socket socket = null;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream  objectInputStream  = null;

	//======================================================================================
	//======================================================================================
	public ServerListenerThread( ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream )
	{
		System.out.println(" + DesktopChatClient::ServerListenerThread::ServerListenerThread()");

//		this.socket = socket;
		this.objectOutputStream = objectOutputStream;
		this.objectInputStream  = objectInputStream;

		thread = new Thread( this );
		thread.start();
	}

	//======================================================================================
	//======================================================================================
	@Override
	public void run()
	{
		System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- Thread: " + thread.getName() );

//		ObjectOutputStream objectOutputStream = null;
//		ObjectInputStream  objectInputStream  = null;
//		try
//		{
//			objectInputStream  = new ObjectInputStream(  socket.getInputStream()  );
//			objectOutputStream = new ObjectOutputStream( socket.getOutputStream() );
//		}
//		catch ( IOException e )
//		{
//			e.getMessage();
//		}

		try
		{
			while (true)
			{
				Message messageIn = (Message) objectInputStream.readObject();
				// Сообщение типа Ping (для проверки связи)
				if ( messageIn instanceof Ping )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- Ping");
					Ping ping = (Ping) messageIn;
					System.out.println("[ " + ping.getDate() + " ] " + ping.getUser().getLogin() + " : " + ping.getMessage() );
					System.out.println("Отправляю ping на сервер...");
					objectOutputStream.writeObject( new Ping() );
					System.out.println("ping отправлен");
				}
				// Мы только что подсоединились к серверу и нам от него пришло первое инициирующее сообщение
				else if ( messageIn instanceof InitMessage )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- InitMessage");
					InitMessage initMessage = (InitMessage) messageIn;

					List<User> list_User = initMessage.getUsers();
					ChatWindow.setListUser( list_User );

					String msg = initMessage.getMessage();
					System.out.println( msg );
					ChatWindow.logging( msg );

					// Нужно сразу же ответить серверу (для инициализации переменных потока нашего соединения)
					objectOutputStream.writeObject( new Message( StartWindow.user, "Здравствуй, сервер!") );
				}
				// К чату подключился новый пользователь
				else if ( messageIn instanceof NewUser )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- NewUser");
					ChatWindow.addUserToListUser( messageIn.getUser() );
				}
				// Пользователь покинул чат
				else if ( messageIn instanceof UserOut )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- UserOut");
					ChatWindow.removeUserFromListUser( messageIn.getUser() );
				}
				// Пользователь приглашает поиграть
				else if ( messageIn instanceof GameRequest )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- GameRequest");
					GameRequest gameRequest = (GameRequest) messageIn;
					ChatWindow.openNewGameRequestDialog( gameRequest.getUser() );
				}
				// Пользователь приглашает поиграть
				else if ( messageIn instanceof StartGame )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- StartGame");
					StartGame startGame = (StartGame) messageIn;
					ChatWindow.startGame( startGame.getUser() );
				}
				// Пользователь приглашает поиграть
				else if ( messageIn instanceof GameData )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- GameData");
					GameData gameData = (GameData) messageIn;
					GameField.getInstance().newGameData( gameData );
				}
				// Пользователь завершает игру
				else if ( messageIn instanceof StopGame )
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- StopGame");
					StopGame stopGame = (StopGame) messageIn;
					ChatWindow.closeGame();
				}
				// Обычное сообщение
				else
				{
					System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- Message");
					String msg = "[ " + messageIn.getDate().toString() + " ] " + messageIn.getUser().getLogin() + " : " + messageIn.getMessage();
					System.out.println( msg );
					ChatWindow.logging( msg );
				}
			}
		}
		catch ( SocketException e )
		{
			System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- SocketException");
			System.out.println( e.getMessage() );
		}
		catch ( ClassNotFoundException e )
		{
			System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- ClassNotFoundException");
			System.out.println( e.getMessage() );
		}
		catch ( IOException e )
		{
			System.out.println(" + DesktopChatClient::ServerListenerThread::run() --- IOException");
			System.out.println( e.getMessage() );
		}
	}
}
