package foundation.qaddict.functions;

@FunctionalInterface
public interface Logic<D> {
    boolean test(D data) throws Throwable;
}
