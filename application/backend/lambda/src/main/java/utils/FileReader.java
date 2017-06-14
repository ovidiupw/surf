package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import models.exceptions.InternalServerException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {

    private FileReader() {
        throw new UnsupportedOperationException("Use the static methods instead of constructing a FileReader instance.");
    }

    public static String readFile(@Nonnull final String filePath) {
        Preconditions.checkNotNull(filePath);

        try {
            final Path path = Paths.get(FileReader.class.getResource("/").toURI());
            final Path resourceLocation = path.resolve(filePath);

            return new String(
                    Files.readAllBytes(resourceLocation),
                    StandardCharsets.UTF_8);
        } catch (IOException
                | URISyntaxException e) {
            throw new InternalServerException("Exception while reading file: " + e.getMessage());
        }
    }

    public static <T> T readObjectFromFile(@Nonnull final String filePath, @Nonnull final Class<T> clazz) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String fileContents = readFile(filePath);
        try {
            return objectMapper.readValue(fileContents, clazz);
        } catch (IOException e) {
            throw new InternalServerException("Could not read object from file: " + e.getMessage());
        }
    }
}
