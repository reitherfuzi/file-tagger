package at.pwimmer.ft;

import at.pwimmer.ft.exceptions.FileTaggerException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FileTaggerUtilsTest {
    private static Path testFile;

    @BeforeAll
    public static void setupBefore() throws IOException {
        testFile = Path.of("junit-test-file.mp4");
        Files.createFile(testFile);
        assertTrue(Files.exists(testFile));
    }

    @Test
    void testInit() throws URISyntaxException {
        final Path invalidPath = Path.of("some-super-curios-file-which-should-not-exist.test");
        final Path validPath = testFile;
        assertTrue(Files.notExists(invalidPath));

        assertThrows(IllegalArgumentException.class, () -> FileTaggerUtils.readAttributeFrom(null, "test-uuid"));
        assertThrows(IllegalArgumentException.class, () -> FileTaggerUtils.readAttributeFrom(validPath, null));
        assertThrows(IllegalArgumentException.class, () -> FileTaggerUtils.readAttributeFrom(validPath, ""));
        assertThrows(IllegalArgumentException.class, () -> FileTaggerUtils.readAttributeFrom(invalidPath, "test-uuid"));
    }

    @Test
    void testReadAndWrite() throws FileTaggerException {
        // Test that a FileTaggerException will be thrown when reading an attribute which does not exist yet.
        assertThrows(FileTaggerException.class, () -> FileTaggerUtils.readAttributeFrom(testFile, "test-uuid"));

        // Declare the test attribute and the uuid to save.
        final String testAttribute = "test-write-read";
        final UUID uuid = UUID.randomUUID();

        // Then check that the attribute is not set yet, write it to the file and check again.
        assertFalse(FileTaggerUtils.hasAttribute(testFile, testAttribute));
        FileTaggerUtils.writeAttributeTo(testFile, testAttribute, uuid.toString());
        assertTrue(FileTaggerUtils.hasAttribute(testFile, testAttribute));

        // Then finally, read the attribute and validate it against the original one.
        final String value = FileTaggerUtils.readAttributeFrom(testFile, testAttribute);
        assertEquals(uuid, UUID.fromString(value));
    }

    @Test
    void testDeleteAttribute() throws FileTaggerException {
        // Declare the attribute to be used by this test-case.
        final String testAttribute = "test-delete-attr";

        assertFalse(FileTaggerUtils.hasAttribute(testFile, testAttribute));
        FileTaggerUtils.writeAttributeTo(testFile, testAttribute, "test_value_yeah");
        assertTrue(FileTaggerUtils.hasAttribute(testFile, testAttribute));
        FileTaggerUtils.deleteAttribute(testFile, testAttribute);
        assertFalse(FileTaggerUtils.hasAttribute(testFile, testAttribute));
    }

    @AfterAll
    public static void clearAfter() throws IOException {
        Files.delete(testFile);
        assertTrue(Files.notExists(testFile));
    }
}