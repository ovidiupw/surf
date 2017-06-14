package models;

import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.Set;

/**
 * Encloses the details about how the information should be selected
 * from a web page obtained through the crawling process.
 */
public class SelectionPolicy implements Validateable {
    /**
     * The {@link TextSelector text selectors} associated with the selection policy.
     * If multiple text selectors are set in this {@link Set}, then all of them will
     * be used to extract information from the web-page.
     */
    private Set<TextSelector> textSelectors;

    /**
     * The {@link CSSSelector CSS selectors} associated with the selection policy.
     * If multiple CSS selectors are set in this {@link Set}, then all of them will
     * be used to extract information from the web-page.
     */
    private Set<CSSSelector> cssSelectors;

    public Set<TextSelector> getTextSelectors() {
        return textSelectors;
    }

    public void setTextSelectors(Set<TextSelector> textSelectors) {
        this.textSelectors = textSelectors;
    }

    public Set<CSSSelector> getCssSelectors() {
        return cssSelectors;
    }

    public void setCssSelectors(Set<CSSSelector> cssSelectors) {
        this.cssSelectors = cssSelectors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectionPolicy that = (SelectionPolicy) o;
        return Objects.equals(textSelectors, that.textSelectors) &&
                Objects.equals(cssSelectors, that.cssSelectors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textSelectors, cssSelectors);
    }

    @Override
    public String toString() {
        return "SelectionPolicy{" +
                "textSelectors=" + textSelectors +
                ", cssSelectors=" + cssSelectors +
                '}';
    }

    @Override
    public void validate() throws RuntimeException {
        try {
            MoreObjects.firstNonNull(textSelectors, cssSelectors);
        } catch (NullPointerException ignored) {
            throw new IllegalArgumentException(
                    "At least one of the SelectionPolicy's 'textSelectors' or 'cssSelectors' must be non-null!"
            );
        }

        if (textSelectors.isEmpty() && cssSelectors.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one of the SelectionPolicy's 'textSelectors' or 'cssSelectors' must be non-empty!"
            );
        }

        if (textSelectors != null) {
            for (final TextSelector textSelector : textSelectors) {
                textSelector.validate();
            }
        }
        if (cssSelectors != null) {
            for (final CSSSelector cssSelector : cssSelectors) {
                cssSelector.validate();
            }
        }
    }
}
