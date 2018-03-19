package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.datastoreServer.commands.ServerCommand;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class CommandWrite extends ServerCommand {

	@Override
	public void run() throws IOException, ServerException {
		String givenDataName = StreamUtil.readLine(inputStream);
		int length = Integer.parseInt(StreamUtil.readLine(inputStream));
		byte[] dataAssociated = StreamUtil.readData(length, inputStream);
		FileUtil.writeData(givenDataName, dataAssociated);
		StreamUtil.writeLine("ok\n", outputStream);
	}
}
