package com.romiis.core;


import java.util.HashSet;
import java.util.Set;

/**
 * Configuration class for the EqualLib.
 * <p>
 * This class allows customization of deep object comparison behavior,
 * including settings for ignored fields, classes, packages, and comparison depth.
 * </p>
 */

public class EqualLibConfig {

    /**
     * The maximal depth of the comparison.
     */
    private int maxDepth = -1;

    /**
     * The fields to ignore during the comparison.
     */
    private Set<String> ignoredFields = new HashSet<>();

    /**
     * The classes to ignore during the comparison.
     */
    private Set<String> useCustomEquals = new HashSet<>();

    /**
     * Whether to use the equals method after the max depth is reached.
     */
    private boolean useEqualsAfterMaxDepth = false;

    /**
     * Whether to compare inherited fields.
     */
    private boolean equivalenceByInheritance = false;

    /**
     * Whether to treat collections as objects.
     */
    private boolean compareByElementsAndKeys = false;

    /**
     * Whether to print debug information.
     */
    private boolean debugMode = false;

    /**
     * Sets the maximum depth for comparison.
     * <p>
     * This setting controls how deep the comparison should go when analyzing object fields.
     * If set to -1, there is no depth limit, meaning the comparison will continue until all fields are checked.
     * A positive number limits the depth to a specified number of levels.
     * </p>
     * <p>
     * If set to {@code true}, the comparison will call the standard {@code equals()} method
     * on the objects when the depth limit is hit. If set to {@code false}, objects will be
     * considered equal beyond the max depth without further checks.
     * </p>
     *
     * @param maxDepth               The maximal depth (included) of the comparison (-1 means no limit).
     * @param useEqualsAfterMaxDepth If {@code true}, the {@code equals()} method is used when depth is reached,
     *                               otherwise objects are considered equal without further checks.
     * @return The updated configuration object.
     */
    public EqualLibConfig setMaxDepth(int maxDepth, boolean useEqualsAfterMaxDepth) {
        this.maxDepth = maxDepth;
        this.useEqualsAfterMaxDepth = useEqualsAfterMaxDepth;
        return this;
    }

    /**
     * Specifies the fields to ignore during the comparison.
     * <p>
     * The fields listed here will be skipped during the comparison process.
     * The fields must be specified with their full path, including package and class name.
     * Example: "com.example.data.Person.name"
     * </p>
     * <p>
     * BE CAREFUL: The field names are case-sensitive.
     * </p>
     *
     * @param ignoredFields Fully qualified field names with class and package. Example: "com.example.data.Person.name"
     * @return The updated configuration object.
     */
    public EqualLibConfig setIgnoredFields(String... ignoredFields) {
        this.ignoredFields = Set.of(ignoredFields);
        return this;
    }

    /**
     * Specifies the classes or packages in which the custom equals method should be used instead of the DeepEquals.
     * <p>
     * If a class is listed here, instances of that class will be excluded from comparison,
     * meaning all fields within such classes will be skipped.
     * </p>
     *
     * @param packagesAndClasses The classes or packages in which the custom equals method should be used instead of the DeepEquals.
     * @return The updated configuration object.
     */
    public EqualLibConfig setUseCustomEqualsIn(String... packagesAndClasses) {
        this.useCustomEquals = Set.of(packagesAndClasses);
        return this;
    }


    /**
     * Sets whether inheritance should be included in the comparison.
     * <p>
     * If set to {@code true}, for equality of two objects, only fields in superclass will be compared.
     * If set to {@code false}, Every field in the object will be compared.
     * </p>
     *
     * @param compareInheritance {@code true} superclass attributes enough for equality, {@code false} every field in the object should be compared.
     * @return The updated configuration object.
     */
    public EqualLibConfig equivalenceByInheritance(boolean compareInheritance) {
        this.equivalenceByInheritance = compareInheritance;
        return this;
    }

    /**
     * Defines how collections should be compared.
     * <p>
     * If set to {@code true}, collections (like Lists, Sets, and Maps) will be treated as objects,
     * meaning their internal structure will be compared field by field. If set to {@code false},
     * collections will be compared element by element, without considering their internal structure.
     * </p>
     *
     * @param compareByElementsAndKeys If {@code true}, collections are compared as objects.
     *                                 If {@code false}, their elements are compared separately.
     * @return The updated configuration object.
     */
    public EqualLibConfig setCompareByElementsAndKeys(boolean compareByElementsAndKeys) {
        this.compareByElementsAndKeys = compareByElementsAndKeys;
        return this;
    }

    /**
     * Sets the debug mode for the comparison.
     * <p>
     * If set to {@code true}, the comparison will print debug information to the console.
     * This information includes the fields being compared and the result of the comparison.
     * </p>
     *
     * @param debugMode {@code true} if debug information should be printed, {@code false} otherwise.
     * @return The updated configuration object.
     */
    public EqualLibConfig setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        return this;
    }


    /**
     * Gets the maximal depth of the comparison.
     *
     * @return The maximal depth of the comparison.
     */
    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Gets the fields to ignore during the comparison.
     *
     * @return The fields to ignore during the comparison.
     */
    public Set<String> getIgnoredFields() {
        return ignoredFields;
    }

    /**
     * Gets the classes to ignore during the comparison.
     *
     * @return The classes to ignore during the comparison.
     */
    public Set<String> getUseCustomEquals() {
        return useCustomEquals;
    }

    /**
     * Gets whether to use the equals method after the max depth is reached.
     *
     * @return {@code true} if the equals method is used after the max depth is reached, {@code false} otherwise.
     */
    public boolean isUseEqualsAfterMaxDepth() {
        return useEqualsAfterMaxDepth;
    }

    /**
     * Gets whether to compare inherited fields.
     *
     * @return {@code true} if inherited fields are compared, {@code false} otherwise.
     */
    public boolean isEquivalenceByInheritance() {
        return equivalenceByInheritance;
    }

    /**
     * Gets whether to treat collections as objects.
     *
     * @return {@code true} if collections are treated as objects, {@code false} otherwise.
     */
    public boolean isCompareByElementsAndKeys() {
        return compareByElementsAndKeys;
    }


    /**
     * Gets whether the debug mode is enabled.
     *
     * @return {@code true} if the debug mode is enabled, {@code false} otherwise.
     */
    public boolean isDebugMode() {
        return debugMode;
    }
}
