package com.romiis.core;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration class for the EqualLib.
 * <p>
 * This class allows customization of deep object comparison behavior,
 * including settings for ignored fields, classes, packages, and comparison depth.
 * </p>
 */
@Getter
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
    private Set<Class<?>> ignoredClasses = new HashSet<>();

    /**
     * The packages to ignore during the comparison.
     */
    private Set<String> ignoredPackages = new HashSet<>();

    /**
     * Whether to use the equals method after the max depth is reached.
     */
    private boolean useEqualsAfterMaxDepth = false;

    /**
     * Whether to compare inherited fields.
     */
    private boolean compareInheritance = true;

    /**
     * Whether to treat collections as objects.
     */
    private boolean treatCollectionsAsObjects = false;

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
     * @param ignoredFields Fully qualified field names to ignore during comparison.
     * @return The updated configuration object.
     */
    public EqualLibConfig setIgnoredFields(String... ignoredFields) {
        this.ignoredFields = Set.of(ignoredFields);
        return this;
    }

    /**
     * Specifies the classes to ignore during the comparison.
     * <p>
     * If a class is listed here, instances of that class will be excluded from comparison,
     * meaning all fields within such classes will be skipped.
     * </p>
     *
     * @param ignoredClasses The classes that should be excluded from the comparison.
     * @return The updated configuration object.
     */
    public EqualLibConfig setIgnoredClasses(Class<?>... ignoredClasses) {
        this.ignoredClasses = Set.of(ignoredClasses);
        return this;
    }

    /**
     * Specifies the packages to ignore during the comparison.
     * <p>
     * If a package is listed here, all classes within that package will be ignored during the comparison.
     * The package names must be provided in the standard format, e.g., "com.example.package".
     * </p>
     * <p>
     * BE CAREFUL: The package names are case-sensitive.
     * </p>
     *
     * @param ignoredPackages The package names that should be excluded from the comparison.
     * @return The updated configuration object.
     */
    public EqualLibConfig setIgnoredPackages(String... ignoredPackages) {
        this.ignoredPackages = Set.of(ignoredPackages);
        return this;
    }

    /**
     * Sets whether inheritance should be included in the comparison.
     * <p>
     * If set to {@code true}, fields declared in superclasses will be included in the comparison process.
     * Otherwise, only fields declared in the actual class of the compared objects will be checked.
     * </p>
     *
     * @param compareInheritance {@code true} if inherited fields should be included, {@code false} otherwise.
     * @return The updated configuration object.
     */
    public EqualLibConfig setCompareInheritance(boolean compareInheritance) {
        this.compareInheritance = compareInheritance;
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
     * @param treatCollectionsAsObjects If {@code true}, collections are compared as objects.
     *                                  If {@code false}, their elements are compared separately.
     * @return The updated configuration object.
     */
    public EqualLibConfig setTreatCollectionsAsObjects(boolean treatCollectionsAsObjects) {
        this.treatCollectionsAsObjects = treatCollectionsAsObjects;
        return this;
    }
}
