package at.pwimmer.ft.commands;

import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "file-tagger", mixinStandardHelpOptions = true,
        subcommands = {AttributeReadCommand.class, AttributeWriteCommand.class, AttributeDeleteCommand.class})
public class TaggerCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-V", "--version"}, versionHelp = true, description = "display version info")
    private boolean versionInfoRequested;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message")
    private boolean usageHelpRequested;

    @CommandLine.Option(names = {"-f", "--file"}, description = "The file to tag")
    private Path target;

    @Override
    public Integer call() throws Exception {
        return 11;
    }
}
