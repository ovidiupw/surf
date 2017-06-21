package utils;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Selector;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CrawlDataValidator {

    private static final String[] urlSchemes = {"http", "https"};

    private CrawlDataValidator() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    public static String getUrlSchemesAsString() {
        return Arrays.toString(urlSchemes);
    }

    public static List<String> getUrlSchemesAsList() {
        return Arrays.asList(urlSchemes);
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

    public static boolean isValidCSSSelector(@Nonnull final String cssSelector) {
        try {
            final Document dummyDocument = Jsoup.parse("<html></html>");
            dummyDocument.select(cssSelector);
        } catch (Selector.SelectorParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidMaxWebPageSize(final long maxWebPageSizeBytes) {
        return maxWebPageSizeBytes >= 1 && maxWebPageSizeBytes <= 26_214_400;
    }
}
