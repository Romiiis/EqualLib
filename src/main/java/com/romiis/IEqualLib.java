package com.romiis;


/**
 * Interface that defines the methods that the EqualLib class contains.
 *
 * @author Roman Pejs
 * @version 1.0
 */
public interface IEqualLib {

    /**
     * Method that takes two objects and deep compares them to determine if they are equal
     * <p>
     *     This method uses a breadth-first search algorithm to compare the objects.
     *     It compares the objects field by field and if the field is an object, it compares the fields of the object.
     *     If the field is a primitive type, it compares the values of the fields.
     *     Wrappers and String are compared by calling the equals' method.
     *     <br>
     *     Arrays are compared index by index.
     *     Lists are compared index by index.
     *     Maps are compared key by key.
     *     Sets (UNORDERED) are compared element by element.
     *     <br>
     *     Collection comparison is done with recursion.
     *     <br>
     *     Object can be null. If two objects are null, they are considered equal.
     *</p>
     *
     * @param obj1 First object to compare (can be null)
     * @param obj2 Second object to compare (can be null)
     * @return True if the objects are deeply equal, false otherwise
     */
    boolean areEqual(Object obj1, Object obj2);

}
