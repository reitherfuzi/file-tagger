package at.pwimmer.ft.commands;

import at.pwimmer.ft.exceptions.FileTaggerException;
import at.pwimmer.ft.FileTaggerUtils;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "write", description = "Writes the specified attribute to the target-file")
public class AttributeWriteCommand implements Callable<Integer> {
        @CommandLine.Parameters(arity = "1..*")
        private List<Path> targets;

        @CommandLine.Option(names = {"-an", "--attribute-name"}, description = "The name of the attribute to set")
        private String attributeName;

        @CommandLine.Option(names = {"-av", "--attribute-value"}, description = "The value of the attribute to set")
        private String attributeValue;

        @Override
        public Integer call() throws Exception {
            try {
                if(targets.size() > 1)  return 34;

                final Path target = targets.get(0);
                final boolean success = FileTaggerUtils.writeAttributeTo(target, attributeName, attributeValue);

                if(success) {
                    System.out.println("Added tag '"+ attributeName + ": " + attributeValue +"' to file '" + target + "'");
                    return 31;
                }
                else {
                    System.err.println("Failed to add attribute '"+ attributeName +"' to file '" + target + "'");
                    return 33;
                }
            }
            catch(FileTaggerException ex) {
                System.err.println(ex.getMessage());
                return 32;
            }
    }
}
