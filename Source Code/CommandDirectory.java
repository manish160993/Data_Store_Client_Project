package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.List;

import utd.persistentDataStore.datastoreServer.commands.ServerCommand;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class CommandDirectory extends ServerCommand {

	@Override
	public void run() throws IOException, ServerException {
		List<String> directory = FileUtil.directory();
		if (directory.size() != 0) {
			StreamUtil.writeLine("ok\n" + String.valueOf(directory.size()) + "\n", outputStream);
			for(String name : directory) {
				StreamUtil.writeLine(name, outputStream);
			}
		}
	}
}
