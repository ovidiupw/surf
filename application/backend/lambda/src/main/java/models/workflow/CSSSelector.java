package models.workflow;

import com.google.common.base.Preconditions;
import models.Validateable;
import utils.CrawlDataValidator;

import java.util.Objects;

public class CSSSelector implements Validateable {

    /**
     * A valid css selector.
     */
    private String cssSelector;

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSSSelector that = (CSSSelector) o;
        return Objects.equals(cssSelector, that.cssSelector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cssSelector);
    }

    @Override
    public String toString() {
        return "CSSSelector{" +
                "cssSelector='" + cssSelector + '\'' +
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

        Preconditions.checkArgument(
                CrawlDataValidator.isValidCSSSelector(cssSelector),
                "The css selector 'cssSelector' must be a valid css selector!"
        );
    }
}
