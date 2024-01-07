package org.qaddict;

public interface Described {

    /**
     * Description of the defined expectation.
     * @return Object representing the expectation.
     */
    Object description();

    static Described description(Object descritpion) {
        return () -> descritpion;
    }

}
