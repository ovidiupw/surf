package models;

import com.google.common.base.Preconditions;
import utils.CrawlDataValidator;

import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates logic about selecting text from a crawled web page.
 */
public class TextSelector implements Validateable {

    /**
     * A regexp that identifies blocks that are to be selected for indexing during
     * the crawling process.
     */
    private String textMatcher;

    /**
     * A set of {@link ExtractionPolicy policies} to apply to the text extraction
     * obtained by running the {@link #textMatcher} over the crawled web page.
     */
    private Set<ExtractionPolicy> extractionPolicies;

    public String getTextMatcher() {
        return textMatcher;
    }

    public void setTextMatcher(String textMatcher) {
        this.textMatcher = textMatcher;
    }

    public Set<ExtractionPolicy> getExtractionPolicies() {
        return extractionPolicies;
    }

    public void setExtractionPolicies(Set<ExtractionPolicy> extractionPolicies) {
        this.extractionPolicies = extractionPolicies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSelector that = (TextSelector) o;
        return Objects.equals(textMatcher, that.textMatcher) &&
                Objects.equals(extractionPolicies, that.extractionPolicies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textMatcher, extractionPolicies);
    }

    @Override
    public String toString() {
        return "TextSelector{" +
                "textMatcher='" + textMatcher + '\'' +
                ", extractionPolicies=" + extractionPolicies +
                '}';
    }

    @Override
    public void validate() throws RuntimeException {
        Preconditions.checkNotNull(
                textMatcher,
                "The text selector 'textMatcher' must not be null!"
        );
        Preconditions.checkArgument(
                CrawlDataValidator.isValidRegexp(textMatcher),
                "The text selector 'textMatcher' must be a valid regexp!");

        if (extractionPolicies != null) {
            for (final ExtractionPolicy extractionPolicy : extractionPolicies) {
                extractionPolicy.validate();
            }
        }
    }
}
