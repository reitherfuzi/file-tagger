package at.pwimmer.ft;

import at.pwimmer.ft.commands.TaggerCommand;
import picocli.CommandLine;

public class FileTaggerApplication {

	public static void main(String[] args) {
		final int exitCode = new CommandLine(new TaggerCommand()).execute(args);
		System.exit(exitCode);
	}
}
