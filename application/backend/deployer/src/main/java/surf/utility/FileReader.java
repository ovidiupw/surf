package surf.utility;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.ExitCode;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

    private static final Logger LOG = LoggerFactory.getLogger(FileReader.class);

    private FileReader() {
        throw new UnsupportedOperationException("Use the static methods instead of constructing a FileReader instance.");
    }

    public static String readFile(@Nonnull final String filePath) {
        Preconditions.checkNotNull(filePath);

        String fileContent = null;
        try {
            fileContent = new String(
                    Files.readAllBytes(Paths.get(filePath)),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Exception while reading file!", e);
            System.exit(ExitCode.Error.getCode());
        }
        return fileContent;
    }
}
