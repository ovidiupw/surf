package surf.utility;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter {

    private static final Logger LOG = LoggerFactory.getLogger(FileWriter.class);

    private FileWriter() {
        throw new UnsupportedOperationException("Use the static methods instead of constructing a FileWriter instance.");
    }

    public static Path writeZipFile(@Nonnull final byte[] sourceBytes,
                                    @Nonnull final String zipName,
                                    @Nonnull final String outputPath) {
        Preconditions.checkNotNull(sourceBytes);
        Preconditions.checkNotNull(outputPath);
        Preconditions.checkNotNull(zipName);

        final Path zipFilePath = Paths.get(new File(outputPath, zipName).getPath());

        //TODO add safety check back once out of initial development
        /*if (Files.exists(zipFilePath) && Files.isRegularFile(zipFilePath)) {
            throw new UnsupportedOperationException(
                    "Cannot create zip file because a file with the same name already exists: " + zipFilePath);
        }*/

        try {
            Files.write(zipFilePath, sourceBytes);
        } catch (IOException e) {
            LOG.error("Error while writing zip file!", e);
            throw new OperationFailedException(e);
        }
        return zipFilePath;
    }

}
