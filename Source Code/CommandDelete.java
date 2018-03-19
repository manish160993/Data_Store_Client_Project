package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.datastoreServer.commands.ServerCommand;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class CommandDelete extends ServerCommand {

	@Override
	public void run() throws IOException, ServerException {
		String givenName = StreamUtil.readLine(inputStream);
		boolean dataAssociated = FileUtil.deleteData(givenName);
		if (dataAssociated != false) {
			StreamUtil.writeLine("ok\n", outputStream);
		} else {
			StreamUtil.sendError("Error: No file/data found",outputStream);
		}
	}
}
