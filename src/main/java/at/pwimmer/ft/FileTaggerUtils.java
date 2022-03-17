package at.pwimmer.ft;

import at.pwimmer.ft.exceptions.FileTaggerException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>FileTagger Utilities</h1>
 * <p>
 *     This class holds some static methods for reading and writing attributes to any type of file
 *     which can be stored on a filesystem. It does not support files on remote locations like webservers.
 * </p>
 * <p>
 *     There are multiple helper methods available. In example to check if a file has a specific attribute set
 *     or to list all user-defined attributes of a specific file.
 * </p>
 * <p>
 *     <b>Note</b>: This utility can only read/write user-defined attributes. Attributes like the name, title
 *     or creation-date will and cannot be modified by these methods. This is because the implementation uses
 *     the {@link UserDefinedFileAttributeView} to access the attributes of the passed files/paths.
 * </p>
 */
public class FileTaggerUtils {

    private FileTaggerUtils() {
        // private no-arg constructor to hide the implicit public one.
    }

    /**
     * Returns a map containing all user-defined attributes set on the file specified by the passed {@link Path} object.
     * Each attribute-name and -value will be added to the map as an entry and therefore the map-key is the attribute-name
     * and the map-value is the attribute-value.
     * <p>
     * The method makes use of the {@link UserDefinedFileAttributeView} and iterates over all returned attribute-names
     * and extracts its assigned value.
     * @param path The path of the file to list its attributes.
     * @return A map containing the attribute-names as the key and the attribute-value as the value.
     * @throws FileTaggerException Will be thrown if the attributes could not be read from the passed path.
     */
    public static Map<String, String> listAttributes(Path path) throws FileTaggerException {
        validateParameters(path);

        try {
            // Open the view and init an empty Hash-Map.
            final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
            final Map<String, String> map = new HashMap<>();

            // Then load all available attributes from the specified file.
            final List<String> attributes = view.list();

            // Iterate over the retrieved list of attributes and extract each value into the map.
            for(String an : attributes) {
                final String result = extractAttribute(view, an);
                map.put(an, result);
            }

            // Finally, return the map.
            return map;
        }
        catch(IOException ex) {
            // Will be thrown if the file at the specified path could not be opened or its attributes read.
            throw new FileTaggerException("Failed to read all attributes from '"+path+"'", ex);
        }
    }

    /**
     * Checks if the file at the specified {@link Path} object does have an attribute set which
     * matches the also passed <em>attributeName</em>.
     * <p>
     * Will return true if an attribute with that name could be found, will return false otherwise.
     * @param path The path of the file to check if the attribute is set.
     * @param attributeName The name of the attribute to check the file for.
     * @return The boolean result, true if attribute available, false othwerise.
     * @throws FileTaggerException Will be thrown if the attributes could not be read from the file.
     */
    public static boolean hasAttribute(Path path, String attributeName) throws FileTaggerException {
        validateParameters(path, attributeName);

        try {
            // Open the User-Defined File-Attribute view and check if it does have the passed attribute-name set.
            final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
            return hasViewAttribute(view, attributeName);
        }
        catch(IOException ex) {
            // Will be thrown if the attributes could not be read from the view/file.
            throw new FileTaggerException("Failed to read list of attributes from '"+path+"'", ex);
        }
    }

    /**
     * Reads the associated attribute-value for the passed <code>attributeName</code> from the also
     * passed file. The file is specified using its {@link Path} object.
     * <p>
     * Before reading, the path will be checked to point to an exisiting file. If the file does not have
     * the specified attribute set, then this method will throw a {@link FileTaggerException}.
     * @param path The path of the file to read the attribute from.
     * @param attributeName The name of the attribute to read.
     * @return The value of the attribute read from the specified path.
     * @throws FileTaggerException Will be thrown if the attribute could not be read from the file.
     */
    public static String readAttributeFrom(Path path, String attributeName) throws FileTaggerException {
        validateParameters(path, attributeName);

        try {
            // Get the UserDefined File-Attribute-View of the passed "path".
            final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);

            // Before continuing, check that the view has the corresponding attribute.
            if(!hasViewAttribute(view, attributeName))
                throw new FileTaggerException("The attribute '"+attributeName+"' is not set on '"+path+"'");

            return extractAttribute(view, attributeName);
        }
        catch(IOException ex) {
            // Will be thrown if view could not be opened or attribute could not be read.
            throw new FileTaggerException("Could not read attribute from '"+path+"'", ex);
        }
    }

    /**
     * Writes the passed attribute-name and attribute-value combination to the file specified by the passed
     * {@link Path} object. If there is already an attribute set with that name, its value will be overridden.
     * <p>
     * This method will alwawys return true as long as there is no exception thrown.
     * @param path The path of the file to write the attribute combination to.
     * @param attributeName The name of the attribute to write.
     * @param attributeValue The value of the attribute to write.
     * @return The boolean result, which will always be true as long as one byte has been written.
     * @throws FileTaggerException Will be thrown if the attribute could not be written to the file.
     */
    public static boolean writeAttributeTo(Path path, String attributeName, String attributeValue) throws FileTaggerException {
        validateParameters(path, attributeName, attributeValue);

        try {
            final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
            final ByteBuffer src = StandardCharsets.UTF_8.encode(attributeValue);
            view.write(attributeName, src);
            return true;
        }
        catch(IOException ex) {
            throw new FileTaggerException("Could not write attribute to '"+path+"'", ex);
        }
    }

    /**
     * Deletes the passed <code>attributeName</code> from the file specified by the passed {@link Path} object.
     * If the attribute did not exist before deleting, this method will do nothing and returns <code>false</code>.
     * @param path The path of the file to delete the attribute from.
     * @param attributeName The name of the attribute to delete from the file.
     * @return The boolean result, <code>true</code> if the attribute has been removed, <code>false</code> otherwise.
     * @throws FileTaggerException
     */
    public static boolean deleteAttribute(Path path, String attributeName) throws FileTaggerException {
        validateParameters(path, attributeName);

        try {
            // Get the UserDefined File-Attribute-View of the passed path.
            final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);

            // If the view/file does not have the passed "attributeName" then return false.
            if(!hasViewAttribute(view, attributeName))  return false;

            // Then try to delete the attribute from the file and return true.
            view.delete(attributeName);
            return true;
        }
        catch (IOException ex) {
            // Will be thrown if the view could not be opened or attribute not deleted.
            throw new FileTaggerException("Could not delete attribute '"+attributeName+"' from '"+path+"'", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///
    /// PRIVATE - The private methods for check the view if the attribute is set.
    ///
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void validateParameters(Path path) {
        if(path == null || Files.notExists(path))
            throw new IllegalArgumentException("The passed path is null or does not exist!");
    }

    private static void validateParameters(Path path, String attributeName) {
        validateParameters(path);

        if(attributeName == null || attributeName.isBlank())
            throw new IllegalArgumentException("The passed attribute-name is null or blank!");
    }

    private static void validateParameters(Path path, String attributeName, String attributeValue) {
        validateParameters(path, attributeName);

        if(attributeValue == null || attributeValue.isBlank())
            throw new IllegalArgumentException("The passed attribute-value is null or blank!");
    }

    private static String extractAttribute(UserDefinedFileAttributeView view, String attributeName) throws IOException {
        // If view has the attribute, allocate a Byte-Buffer and read the attribute into it.
        final ByteBuffer dst = ByteBuffer.allocate(view.size(attributeName));
        view.read(attributeName, dst);
        dst.flip();

        // Finally, decode the byte-buffer into a string and return it.
        return String.valueOf(StandardCharsets.UTF_8.decode(dst));
    }

    private static boolean hasViewAttribute(UserDefinedFileAttributeView view, String attributeName) throws IOException {
        final List<String> list = view.list();
        return list.contains(attributeName);
    }
}