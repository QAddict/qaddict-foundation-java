package org.qaddict.functions;

@FunctionalInterface
public interface Transformation<I, O> {

    O transform(I input) throws Throwable;

    static <I, O> Transformation<I, O> nullOr(Transformation<I, O> transformation) {
        return input -> input == null ? null : transformation.transform(input);
    }

}
