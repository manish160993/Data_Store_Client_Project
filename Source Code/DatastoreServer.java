package utd.persistentDataStore.datastoreServer;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import utd.persistentDataStore.datastoreServer.commands.*;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

enum Instruction {
	WRITE("write"),
	READ("read"),
	DELETE("delete"),
	DIRECTORY("directory");
	
	private String value;
	
	Instruction(String value) {
		this.value = value;
	}
	   
    public String getValue() {
    	return value;
	}
}

public class DatastoreServer
{
	private static Logger logger = Logger.getLogger(DatastoreServer.class);

	static public final int port = 10023;

	public void startup() throws IOException
	{
		logger.debug("Starting Service at port " + port);

		ServerSocket serverSocket = new ServerSocket(port);

		InputStream inputStream = null;
		OutputStream outputStream = null;
		while (true) {
			try {
				logger.debug("Waiting for request");
				// The following accept() will block until a client connection 
				// request is received at the configured port number
				Socket clientSocket = serverSocket.accept();
				logger.debug("Request received");

				inputStream = clientSocket.getInputStream();
				outputStream = clientSocket.getOutputStream();

				ServerCommand command = dispatchCommand(inputStream);
				logger.debug("Processing Request: " + command);
				command.setInputStream(inputStream);
				command.setOutputStream(outputStream);
				command.run();
				
				StreamUtil.closeSocket(inputStream);
			}
			catch (ServerException ex) {
				String msg = ex.getMessage();
				logger.error("Exception while processing request. " + msg);
				StreamUtil.sendError(msg, outputStream);
				StreamUtil.closeSocket(inputStream);
			}
			catch (Exception ex) {
				logger.error("Exception while processing request. " + ex.getMessage());
				ex.printStackTrace();
				StreamUtil.closeSocket(inputStream);
			}
		}
	}

	private ServerCommand dispatchCommand(InputStream inputStream) throws ServerException
	{
		ServerCommand serverCommand = null;
		try {
			String command = StreamUtil.readLine(inputStream);
			if (command != null) {
				Instruction value = Instruction.valueOf(command.toUpperCase());
				switch (value) {
				   case WRITE:
					   serverCommand = new CommandWrite();
				   		break;
				   case READ:
					   serverCommand = new CommandRead();
				   		break;
				   case DELETE:
					   serverCommand = new CommandDelete();
				   		break;
				   case DIRECTORY:
					   serverCommand = new CommandDirectory();
					   break;
				   default: 
					   serverCommand = null;
					   break;
				}
					
				return serverCommand;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return null;
	}

	public static void main(String args[])
	{
		DatastoreServer server = new DatastoreServer();
		try {
			server.startup();
		}
		catch (IOException ex) {
			logger.error("Unable to start server. " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
