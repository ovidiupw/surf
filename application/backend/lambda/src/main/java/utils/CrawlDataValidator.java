package utils;

import org.apache.commons.validator.routines.UrlValidator;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CrawlDataValidator {

    private static final String[] urlSchemes = {"http", "https"};

        private CrawlDataValidator() {
            throw new UnsupportedOperationException("This class is not instantiable");
        }

    public static String getUrlSchemes() {
        return Arrays.toString(urlSchemes);
    }

    public static boolean isValidUrl(@Nonnull final String url) {
        final UrlValidator urlValidator = new UrlValidator(urlSchemes);
        return urlValidator.isValid(url);
    }

    public static boolean isValidRegexp(@Nonnull final String regexp) {
        try {
            //TODO get back with what the error is (as api response)
            Pattern.compile(regexp);
        } catch (final PatternSyntaxException ignored) {
            return false;
        }
        return true;
    }

    public static boolean isValidMaxWebPageSize(final long maxWebPageSizeBytes) {
        return maxWebPageSizeBytes >= 1 && maxWebPageSizeBytes <= 26_214_400;
    }
}
