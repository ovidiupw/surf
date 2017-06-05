package surf.utility;

import com.google.common.base.Preconditions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    private static final Logger LOG = LoggerFactory.getLogger(FileHelper.class);

    private FileHelper() {
        throw new UnsupportedOperationException("Use the static methods instead of constructing a FileHelper instance.");
    }

    public static void extractZip(@Nonnull final Path zipFilePath, @Nonnull final String outputFilePath) {
        Preconditions.checkNotNull(zipFilePath);
        Preconditions.checkNotNull(outputFilePath);

        final Path unzippedFilePath = Paths.get(outputFilePath);

        //TODO add safety check back once out of initial development
       /* if (Files.exists(unzippedFilePath)) {
            throw new UnsupportedOperationException(
                    "Cannot extract zip file because the output path already exists: " + zipFilePath);
        }*/

        try {
            ZipFile zipFile = new ZipFile(zipFilePath.toFile());
            zipFile.extractAll(outputFilePath);
        } catch (ZipException e) {
            LOG.error("ZipException while trying to extract zip file!", e);
            throw new OperationFailedException(e);
        }

    }
}
