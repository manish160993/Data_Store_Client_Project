// Project 2
package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient {
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);
	private InetAddress address;
	private int port;
	private Socket socket;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte data[]) throws ClientException, ConnectionException {
		logger.debug("Executing Write Operation");
		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(address, port);
			socket.connect(socketAddress);
			OutputStream outputStream = socket.getOutputStream();
			StreamUtil.writeLine("write\n" + name + "\n" + String.valueOf(data.length) + "\n", outputStream);
			StreamUtil.writeData(data, outputStream);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException, ConnectionException {
		logger.debug("Executing Read Operation");
		byte[] data = null;

		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(address, port);
			socket.connect(socketAddress);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			StreamUtil.writeLine("read\n" + name + "\n", outputStream);
			String response = StreamUtil.readLine(inputStream);
			if(response.equalsIgnoreCase("ok")) {
					int size = Integer.parseInt(StreamUtil.readLine(inputStream));
					data = StreamUtil.readData(size, inputStream);
			} else {
					throw new ClientException("File Not Found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException, ConnectionException {
		logger.debug("Executing Delete Operation");
		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(address, port);
			socket.connect(socketAddress);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			StreamUtil.writeLine("delete\n" + name + "\n", outputStream);
			if (!StreamUtil.readLine(inputStream).equalsIgnoreCase("ok")) {
				throw new ClientException("File Not Found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException, ConnectionException {
		
		logger.debug("Executing Directory Operation");
		List<String> directories = new ArrayList<String>();

		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(address, port);
			socket.connect(socketAddress);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			StreamUtil.writeLine("directory\n", outputStream);
			if (StreamUtil.readLine(inputStream).equalsIgnoreCase("ok")) {
				int size = Integer.parseInt(StreamUtil.readLine(inputStream));
				for (int i = 0; i < size; i++) {
					String dirName = StreamUtil.readLine(inputStream);
					directories.add(dirName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directories;
	}

}
