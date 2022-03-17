package at.pwimmer.ft.commands;

import at.pwimmer.ft.exceptions.FileTaggerException;
import at.pwimmer.ft.FileTaggerUtils;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "delete", description = "Deletes the specified attribute from each passed file")
public class AttributeDeleteCommand implements Callable<Integer> {

    @CommandLine.Parameters(arity = "1..*")
    private List<Path> targets;

    @CommandLine.Option(names = {"-an", "--attribute-name"}, required = true, description = "The name of the attribute to set")
    private String attributeName;

    @Override
    public Integer call() throws Exception {
        targets.forEach(path -> {
            try {
                final boolean success = FileTaggerUtils.deleteAttribute(path, attributeName);
                if(success) {
                    System.out.println("Deleted attribute '"+attributeName+"' from '" + path + "'");
                }
                else {
                    System.err.println("File at '" + path + "' did not have any attribute like '" + attributeName + "' set");
                }
            }
            catch(FileTaggerException ex) {
                System.err.println(ex.getMessage());
            }
        });

        return 41;
    }
}
