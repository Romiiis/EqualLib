package com.romiis.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration class for EqualLib.
 *
 * This class customizes deep object comparison behavior, including settings for
 * ignored field paths, custom equals for specified classes or packages, maximum
 * comparison depth, and additional options.
 */
public class EqualLibConfig {

    /**
     * Maximum depth for object comparison.
     * A value of -1 indicates no depth limit.
     */
    private int maxComparisonDepth = -1;

    /**
     * Set of fully qualified field paths to ignore during comparison.
     * Example: "com.example.data.Person.name"
     */
    private Set<String> ignoredFieldPaths = new HashSet<>();

    /**
     * Set of fully qualified class or package names where the custom equals method should be used.
     */
    private Set<String> customEqualsClasses = new HashSet<>();

    /**
     * If true, once the maximum depth is reached, the standard equals() method will be used for further comparison.
     */
    private boolean useStandardEqualsAfterDepth = false;

    /**
     * If true, only inherited fields (fields declared in superclasses) are compared.
     * If false, all fields of the object are compared.
     */
    private boolean compareInheritedFields = false;

    /**
     * If true, collections (Lists, Sets, Maps) are compared as whole objects by examining their internal structure.
     * If false, collections are compared element by element.
     */
    private boolean compareCollectionsByElements = false;

    /**
     * If true, debug information is printed during the comparison process.
     */
    private boolean debugEnabled = false;

    /**
     * Sets the maximum depth for comparison.
     *
     * @param maxComparisonDepth Maximum depth for comparison (-1 for no limit).
     * @param useStandardEqualsAfterDepth If true, standard equals() is used once the maximum depth is reached.
     * @return Updated EqualLibConfig instance.
     */
    public EqualLibConfig setMaxComparisonDepth(int maxComparisonDepth, boolean useStandardEqualsAfterDepth) {
        this.maxComparisonDepth = maxComparisonDepth;
        this.useStandardEqualsAfterDepth = useStandardEqualsAfterDepth;
        return this;
    }

    /**
     * Sets the field paths to be ignored during comparison.
     *
     * @param ignoredFieldPaths Fully qualified field paths to ignore.
     * @return Updated EqualLibConfig instance.
     */
    public EqualLibConfig setIgnoredFieldPaths(String... ignoredFieldPaths) {
        this.ignoredFieldPaths = Set.of(ignoredFieldPaths);
        return this;
    }

    /**
     * Sets the classes or packages for which the custom equals method should be used.
     *
     * @param customEqualsClasses Fully qualified class or package names.
     * @return Updated EqualLibConfig instance.
     */
    public EqualLibConfig setCustomEqualsClasses(String... customEqualsClasses) {
        this.customEqualsClasses = Set.of(customEqualsClasses);
        return this;
    }

    /**
     * Sets whether to compare only inherited fields.
     *
     * @param compareInheritedFields If true, only fields from superclasses are compared.
     * @return Updated EqualLibConfig instance.
     */
    public EqualLibConfig setCompareInheritedFields(boolean compareInheritedFields) {
        this.compareInheritedFields = compareInheritedFields;
        return this;
    }

    /**
     * Sets how collections should be compared.
     *
     * @param compareCollectionsByElements If true, collections are compared as whole objects by examining their internal structure.
     *                                  If false, collections are compared element by element.
     * @return Updated EqualLibConfig instance.
     */
    public EqualLibConfig setCompareCollectionsByElements(boolean compareCollectionsByElements) {
        this.compareCollectionsByElements = compareCollectionsByElements;
        return this;
    }

    /**
     * Enables or disables debug mode.
     *
     * @param debugEnabled If true, debug information will be printed during comparison.
     * @return Updated EqualLibConfig instance.
     */
    public EqualLibConfig setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    public int getMaxComparisonDepth() {
        return maxComparisonDepth;
    }

    public Set<String> getIgnoredFieldPaths() {
        return ignoredFieldPaths;
    }

    public Set<String> getCustomEqualsClasses() {
        return customEqualsClasses;
    }

    public boolean isUseStandardEqualsAfterDepth() {
        return useStandardEqualsAfterDepth;
    }

    public boolean isCompareInheritedFields() {
        return compareInheritedFields;
    }

    public boolean isCompareCollectionsByElements() {
        return compareCollectionsByElements;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
