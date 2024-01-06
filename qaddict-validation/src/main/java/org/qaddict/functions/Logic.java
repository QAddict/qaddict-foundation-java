package org.qaddict.functions;

@FunctionalInterface
public interface Logic<D> {
    boolean test(D data) throws Throwable;
}
