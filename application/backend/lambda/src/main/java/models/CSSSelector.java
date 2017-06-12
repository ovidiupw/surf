package models;

import java.util.Map;
import java.util.Objects;

public class CSSSelector {
    private String cssSelector;
    private Map<ExtractionPolicy, Integer> extractionPolicies;

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public Map<ExtractionPolicy, Integer> getExtractionPolicies() {
        return extractionPolicies;
    }

    public void setExtractionPolicies(Map<ExtractionPolicy, Integer> extractionPolicies) {
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
}
