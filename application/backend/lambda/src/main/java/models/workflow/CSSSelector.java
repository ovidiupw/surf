package models.workflow;

import com.google.common.base.Preconditions;
import models.Validateable;

import java.util.Objects;
import java.util.Set;

public class CSSSelector implements Validateable {

    /**
     * A valid css selector.
     */
    private String cssSelector;

    /**
     * A set of {@link ExtractionPolicy policies} to apply to the text extraction
     * obtained by running the {@link #cssSelector} over the crawled web page.
     */
    private Set<ExtractionPolicy> extractionPolicies;

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
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
        CSSSelector that = (CSSSelector) o;
        return Objects.equals(cssSelector, that.cssSelector) &&
                Objects.equals(extractionPolicies, that.extractionPolicies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cssSelector, extractionPolicies);
    }

    @Override
    public String toString() {
        return "CSSSelector{" +
                "cssSelector='" + cssSelector + '\'' +
                ", extractionPolicies=" + extractionPolicies +
                '}';
    }

    @Override
    public void validate() throws RuntimeException {
        Preconditions.checkNotNull(
                cssSelector,
                "The css selector 'cssSelector' must not be null!"
        );
        Preconditions.checkArgument(
                !cssSelector.isEmpty(),
                "The css selector 'cssSelector' must not be the empty string!"
        );

        if (extractionPolicies != null) {
            for (final ExtractionPolicy extractionPolicy : extractionPolicies) {
                extractionPolicy.validate();
            }
        }

    }
}
