
package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.datastoreServer.commands.ServerCommand;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class CommandRead extends ServerCommand {
	@Override
	public void run() throws IOException, ServerException {
		String providedName = StreamUtil.readLine(inputStream);
		byte[] binaryData = FileUtil.readData(providedName);
		
		//if no binary data, send an error
		if (binaryData != null) {
			int length = binaryData.length;
		    StreamUtil.writeLine("ok\n" + String.valueOf(length) + "\n", outputStream);
		    StreamUtil.writeData(binaryData, outputStream);
		} else {
			StreamUtil.sendError("Error: No file/data found",outputStream);
		}
	}
}
