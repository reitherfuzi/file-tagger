package at.pwimmer.ft.commands;

import at.pwimmer.ft.exceptions.FileTaggerException;
import at.pwimmer.ft.FileTaggerUtils;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "read", description = "Reads the specified attribute-value from the target file")
public class AttributeReadCommand implements Callable<Integer> {
    @CommandLine.Parameters(arity = "1..*", description = "List of files to read the attributes from")
    private List<Path> targets;

    @CommandLine.Option(names = {"-an", "--attribute-name"}, arity = "0..1", description = "The name of a specific attribute to read only")
    private String attributeName;

    @Override
    public Integer call() throws Exception {
        try {
            if(targets.size() > 1)  return 23;
            final Path target = targets.get(0);

            System.out.println("Target File: " + target);
            System.out.println("----------------------------");
            if(attributeName == null) {
                final Map<String, String> map = FileTaggerUtils.listAttributes(target);
                map.forEach(this::outputAttribute);
            }
            else {
                final String result = FileTaggerUtils.readAttributeFrom(target, attributeName);
                outputAttribute(attributeName, result);
            }

            return 21;
        }
        catch(FileTaggerException ex) {
            System.err.println(ex.getMessage());
            return 22;
        }
    }

    private void outputAttribute(String attributeName, String result) {
        System.out.println("Attribute-Name  : " + attributeName);
        System.out.println("Attribute-Value : " + result);
        System.out.println("----------------------------------");
    }
}
