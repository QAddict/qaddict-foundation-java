package org.qaddict;

import org.qaddict.adapters.Retry;
import org.qaddict.expectation.ConditionalPredicate;
import org.qaddict.expectation.EveryElementExpectation;
import org.qaddict.expectation.ExpectationBuilder;
import org.qaddict.expectation.ExpectationDescription;
import org.qaddict.expectation.FluentTransformation;
import org.qaddict.expectation.InAnyOrderExpectation;
import org.qaddict.expectation.InOrderOfDefinitionExpectation;
import org.qaddict.expectation.Mode;
import org.qaddict.expectation.Negation;
import org.qaddict.expectation.OperatorExpectation;
import org.qaddict.expectation.PredicateExpectation;
import org.qaddict.expectation.ThrowableExpectation;
import org.qaddict.expectation.TransformedExpectation;
import org.qaddict.functions.Executable;
import org.qaddict.functions.Logic;
import org.qaddict.functions.Transformation;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 * Utility class providing factory methods for building plenty of ready to use expectations.
 */
@SuppressWarnings("unused")
public final class Expectations {

    private static final Expectation<Object> IS_NULL = sameInstanceAs(null);
    private static final Expectation<Object> NON_NULL = not(IS_NULL);
    private static final Expectation<Object> ANYTHING = expect0("anything", actualValue -> true);

    private Expectations() {}

    public static <D> Expectation<D> describe(Object description, Expectation<D> expectation) {
        return new ExpectationDescription<>(description, expectation);
    }

    private static <D> Expectation<D> expect0(Object description, Logic<D> logic) {
        return describe(description, new PredicateExpectation<>(logic));
    }

    /**
     * Create expectation using description and implementation logic (predicate).
     *
     * @param description Description of the expectation.
     * @param logic Implementation logic.
     * @return Expectation.
     * @param <D> Type of the data on which the expectation may apply.
     */
    public static <D> Expectation<D> expect(String description, Logic<D> logic) {
        return expect0(description, logic);
    }

    /**
     * Create expectation, which is satisfied, when actual value is exactly the same instance as provided
     * 'expectedInstance'. It really uses under the hood equality operator ==, which compares instances.
     *
     * @param expectedInstance Expected instance.
     * @return New Expectation.
     * @param <D> Type of the data, on which the expectation may apply. It should be equal to the type of expected
     *           instance.
     */
    public static <D> Expectation<D> sameInstanceAs(D expectedInstance) {
        return expect0(expectedInstance, actualInstance -> expectedInstance == actualInstance);
    }

    public static Expectation<Object> instanceOf(Class<?> expectedClass) {
        return expect("instance of " + expectedClass, expectedClass::isInstance);
    }

    public static <D> Expectation<D> require(Expectation<? super D> requirement, Expectation<? super D> expectation) {
        return new ConditionalPredicate<>(requirement, expectation);
    }

    public static <D> Expectation<D> requireNonNull(Expectation<? super D> expectation) {
        return require(nonNull(), expectation);
    }

    @SuppressWarnings("unchecked")
    public static <D> Expectation<D> equalTo(D expectedValue) {
        if(expectedValue == null)
            return (Expectation<D>) IS_NULL;
        if(expectedValue instanceof Double d)
            return (Expectation<D>) closeTo(d, d * 0.000000001);
        if(expectedValue instanceof Float f)
            return (Expectation<D>) closeTo(f, f * 0.00001f);
        if(expectedValue.getClass().isArray()) {
            int length = getLength(expectedValue);
            return require(nonNull(),
                    require(expect("is array", v -> v.getClass().isArray()),
                            require(expect("same length", v -> getLength(v) == length),
                                    allOf(range(0, length).mapToObj(i -> transform("item " + i, v -> get(v, i), equalTo(get(expectedValue, i)))).collect(toList())))));
        }
        return expect0(expectedValue, expectedValue::equals);
    }

    public static <D, V> Expectation<D> transform(String name, Transformation<D, ? extends V> transformation, Expectation<? super V> expectation) {
        return describe(name, new TransformedExpectation<>(transformation, expectation));
    }

    public static <D> Expectation<D> not(Expectation<D> expectationToNegate) {
        return new Negation<>(expectationToNegate);
    }

    public static <D> Expectation<D> not(D unexpectedValue) {
        return not(equalTo(unexpectedValue));
    }

    public static Expectation<Object> anything() {
        return ANYTHING;
    }

    public static Expectation<Object> isNull() {
        return IS_NULL;
    }

    public static Expectation<Object> nonNull() {
        return NON_NULL;
    }

    @SafeVarargs
    public static <D> Expectation<D> allOf(Expectation<? super D>... operands) {
        return allOf(List.<Expectation<? super D>>of(operands));
    }

    public static <D> Expectation<D> allOf(Collection<Expectation<? super D>> operands) {
        return new OperatorExpectation<>(operands, OperatorExpectation.AND);
    }

    @SafeVarargs
    public static <D> Expectation<D> anyOf(Expectation<? super D>... operands) {
        return anyOf(List.<Expectation<? super D>>of(operands));
    }

    public static <D> Expectation<D> anyOf(Collection<Expectation<? super D>> operands) {
        return new OperatorExpectation<>(operands, OperatorExpectation.OR);
    }

    @SafeVarargs
    public static <D> Expectation<D> anyOf(D... operands) {
        Set<D> set = Set.of(operands);
        return expect0("Any of " + set, set::contains);
    }

    public static <D> Expectation<D> moreThan(D lowerBoundary, Comparator<? super D> comparator) {
        return expect("> " + lowerBoundary, actualValue -> comparator.compare(lowerBoundary, actualValue) < 0);
    }

    public static <D extends Comparable<D>> Expectation<D> moreThan(D lowerBoundary) {
        return moreThan(lowerBoundary, Comparator.naturalOrder());
    }

    public static <D> Expectation<D> moreOrEqualTo(D lowerBoundary, Comparator<? super D> comparator) {
        return expect(">= " + lowerBoundary, actualValue -> comparator.compare(lowerBoundary, actualValue) <= 0);
    }

    public static <D extends Comparable<D>> Expectation<D> moreOrEqualTo(D lowerBoundary) {
        return moreOrEqualTo(lowerBoundary, Comparator.naturalOrder());
    }

    public static <D> Expectation<D> lessThan(D upperBoundary, Comparator<? super D> comparator) {
        return expect("< " + upperBoundary, actualValue -> comparator.compare(upperBoundary, actualValue) > 0);
    }

    public static <D extends Comparable<D>> Expectation<D> lessThan(D upperBoundary) {
        return lessThan(upperBoundary, Comparator.naturalOrder());
    }

    public static <D> Expectation<D> lessOrEqualTo(D upperBoundary, Comparator<? super D> comparator) {
        return expect("<= " + upperBoundary, actualValue -> comparator.compare(upperBoundary, actualValue) >= 0);
    }

    public static <D extends Comparable<D>> Expectation<D> lessOrEqualTo(D upperBoundary) {
        return lessOrEqualTo(upperBoundary, Comparator.naturalOrder());
    }

    public static <D> Expectation<D> betweenInclusive(D lowerBoundary, D upperBoundary, Comparator<D> comparator) {
        return comparator.compare(lowerBoundary, upperBoundary) > 0
                ? allOf(moreOrEqualTo(upperBoundary, comparator), lessOrEqualTo(lowerBoundary, comparator))
                : allOf(moreOrEqualTo(lowerBoundary, comparator), lessOrEqualTo(upperBoundary, comparator));
    }

    public static <D extends Comparable<D>> Expectation<D> betweenInclusive(D lowerBoundary, D upperBoundary) {
        return betweenInclusive(lowerBoundary, upperBoundary, Comparator.naturalOrder());
    }

    public static <D> Expectation<D> betweenExclusive(D lowerBoundary, D upperBoundary, Comparator<D> comparator) {
        return comparator.compare(lowerBoundary, upperBoundary) > 0
                ? allOf(moreThan(upperBoundary, comparator), lessThan(lowerBoundary, comparator))
                : allOf(moreThan(lowerBoundary, comparator), lessThan(upperBoundary, comparator));
    }

    public static <D extends Comparable<D>> Expectation<D> betweenExclusive(D lowerBoundary, D upperBoundary) {
        return betweenExclusive(lowerBoundary, upperBoundary, Comparator.naturalOrder());
    }

    public static Expectation<Double> closeTo(double expectedValue, double tolerance) {
        double low = expectedValue - tolerance, high = expectedValue + tolerance;
        return expect(expectedValue + " with tolerance " + tolerance, actualValue -> low <= actualValue && actualValue <= high);
    }

    public static Expectation<Double> doubleIsNan() {
        return expect("double NaN", actualValue -> Double.isNaN(actualValue));
    }

    public static Expectation<Float> closeTo(float expectedValue, float tolerance) {
        float low = expectedValue - tolerance, high = expectedValue + tolerance;
        return expect(expectedValue + " with tolerance " + tolerance, actualValue -> low <= actualValue && actualValue <= high);
    }

    public static Expectation<Float> floatIsNan() {
        return expect("float NaN", actualValue -> Float.isNaN(actualValue));
    }

    public static Expectation<String> startsWith(String expectedPrefix) {
        return expect("starts with " + expectedPrefix, actualValue -> actualValue.startsWith(expectedPrefix));
    }

    public static Expectation<String> endsWith(String expectedSuffix) {
        return expect("ends with " + expectedSuffix, actualValue -> actualValue.endsWith(expectedSuffix));
    }

    public static Expectation<String> contains(String expectedPart) {
        return expect("contains with " + expectedPart, actualValue -> actualValue.contains(expectedPart));
    }

    public static Expectation<String> equalToIgnoreCase(String expectedPart) {
        return expect("<" + expectedPart + "> ignore case", actualValue -> actualValue.equalsIgnoreCase(expectedPart));
    }

    public static Expectation<String> findPattern(String expectedRegexPattern) {
        return findPattern(Pattern.compile(expectedRegexPattern, Pattern.DOTALL));
    }

    public static Expectation<String> findPattern(Pattern expectedRegexPattern) {
        return expect("/" + expectedRegexPattern + "/", expectedRegexPattern.asPredicate()::test);
    }

    public static Expectation<String> matchesPattern(String expectedRegexPattern) {
        return matchesPattern(Pattern.compile(expectedRegexPattern, Pattern.DOTALL));
    }

    public static Expectation<String> matchesPattern(Pattern expectedRegexPattern) {
        return expect("/" + expectedRegexPattern + "/", expectedRegexPattern.asMatchPredicate()::test);
    }

    public static Expectation<String> emptyString() {
        return expect("empty string", String::isEmpty);
    }

    public static Expectation<String> nullOrEmptyString() {
        return anyOf(isNull(), emptyString());
    }

    public static Expectation<String> nullOrBlankString() {
        return anyOf(isNull(), blankString());
    }

    public static Expectation<String> blankString() {
        return expect("blank string", String::isBlank);
    }

    public static Expectation<String> parseInt(Expectation<? super Integer> expectation) {
        return describe("", new TransformedExpectation<>(Transformation.nullOr(Integer::parseInt), expectation));
    }

    public static Expectation<String> parseInt(Integer expectedValue) {
        return parseInt(equalTo(expectedValue));
    }

    public static Expectation<String> parseLong(Expectation<? super Long> expectation) {
        return describe("", new TransformedExpectation<>(Transformation.nullOr(Long::parseLong), expectation));
    }

    public static Expectation<String> parseLong(Long expectedValue) {
        return parseLong(equalTo(expectedValue));
    }

    public static Expectation<String> parseDouble(Expectation<? super Double> expectation) {
        return describe("", new TransformedExpectation<>(Transformation.nullOr(Double::parseDouble), expectation));
    }

    public static Expectation<String> parseDouble(Double expectedValue) {
        return parseDouble(equalTo(expectedValue));
    }

    public static <T extends Enum<T>> Expectation<String> parseEnum(Expectation<? super T> expectation, Class<T> enumClass) {
        return describe("", new TransformedExpectation<>(Transformation.nullOr(v -> Enum.valueOf(enumClass, v)), expectation));
    }

    public static <T extends Enum<T>> Expectation<String> parseEnum(T expectedValue) {
        return expectedValue == null ? sameInstanceAs(null) : parseEnum(equalTo(expectedValue), expectedValue.getDeclaringClass());
    }

    public static <D, V> FluentTransformation<V, ExpectationBuilder<D>> has(String name, Transformation<D, V> transformation) {
        return new ExpectationBuilder<D>().and(name, transformation);
    }

    public static <D, V> FluentTransformation<V, ExpectationBuilder<D>> has(Transformation<D, V> transformation) {
        return new ExpectationBuilder<D>().and(transformation);
    }

    public static <V> FluentTransformation<V, Expectation<Object>> as(Class<V> requiredClass) {
        return c -> require(instanceOf(requiredClass), has(requiredClass::cast).matching(c));
    }

    public static <D> Expectation<Iterable<D>> collectionEqualsInAnyOrder(Collection<Expectation<? super D>> expectations) {
        return new InAnyOrderExpectation<>(expectations, Mode.EQUALS);
    }

    @SafeVarargs
    public static <D> Expectation<Iterable<D>> collectionEqualsInAnyOrder(Expectation<? super D>... expectations) {
        return Expectations.<D>collectionEqualsInAnyOrder(List.of(expectations));
    }

    public static <D> Expectation<Iterable<D>> collectionContainsInAnyOrder(Collection<Expectation<? super D>> expectations) {
        return new InAnyOrderExpectation<>(expectations, Mode.CONTAINS);
    }

    @SafeVarargs
    public static <D> Expectation<Iterable<D>> collectionContainsInAnyOrder(Expectation<? super D>... expectations) {
        return Expectations.<D>collectionContainsInAnyOrder(List.of(expectations));
    }

    public static <D> Expectation<Iterable<D>> collectionStartsInAnyOrderWith(Collection<Expectation<? super D>> expectations) {
        return new InAnyOrderExpectation<>(expectations, Mode.STARTS);
    }

    @SafeVarargs
    public static <D> Expectation<Iterable<D>> collectionStartsInAnyOrderWith(Expectation<? super D>... expectations) {
        return Expectations.<D>collectionStartsInAnyOrderWith(List.of(expectations));
    }

    public static <D> Expectation<Iterable<D>> collectionEquals(Collection<Expectation<? super D>> expectations) {
        return new InOrderOfDefinitionExpectation<>(expectations, Mode.EQUALS);
    }

    @SafeVarargs
    public static <D> Expectation<Iterable<D>> collectionEquals(Expectation<? super D>... expectations) {
        return Expectations.<D>collectionEquals(List.of(expectations));
    }

    public static <D> Expectation<Iterable<D>> collectionContains(Collection<Expectation<? super D>> expectations) {
        return new InOrderOfDefinitionExpectation<>(expectations, Mode.CONTAINS);
    }

    @SafeVarargs
    public static <D> Expectation<Iterable<D>> collectionContains(Expectation<? super D>... expectations) {
        return Expectations.<D>collectionContains(List.of(expectations));
    }

    public static <D> Expectation<Iterable<D>> collectionStartsWith(Collection<Expectation<? super D>> expectations) {
        return new InOrderOfDefinitionExpectation<>(expectations, Mode.STARTS);
    }

    @SafeVarargs
    public static <D> Expectation<Iterable<D>> collectionStartsWith(Expectation<? super D>... expectations) {
        return Expectations.<D>collectionStartsWith(List.of(expectations));
    }

    public static <D> Expectation<Iterable<D>> everyElement(Expectation<? super D> expectation) {
        return new EveryElementExpectation<>(expectation);
    }

    public static <D> Expectation<Iterable<D>> everyElement(D expectedElement) {
        return everyElement(equalTo(expectedElement));
    }

    public static <D> Expectation<Iterable<D>> existsElement(Expectation<? super D> expectation) {
        return collectionContains(expectation);
    }

    public static <D> Expectation<Iterable<D>> existsElement(D expectedElement) {
        return existsElement(equalTo(expectedElement));
    }

    public static <D> Expectation<D> byExample(D exampleBean) {
        if(exampleBean == null) return sameInstanceAs(null);
        return allOf(Stream.of(exampleBean.getClass().getMethods()).filter(m -> m.getName().startsWith("get")).map(m -> new TransformedExpectation<>(m::invoke, new PredicateExpectation<>(v -> Objects.equals(v, m.invoke(exampleBean))))).collect(toList()));
    }

    public static Expectation<Executable> throwing(Expectation<? super Throwable> expectation) {
        return new ThrowableExpectation(expectation);
    }

    public static <D> Expectation<D> retryUntilMatch(Expectation<? super D> expectation, int max, Duration delay) {
        return retry(existsElement(expectation), max, delay);
    }

    public static <D> Expectation<D> retry(Expectation<? super Iterable<D>> expectation, int max, Duration delay) {
        return has((D d) -> new Retry<>(d, max, delay)).matching(expectation);
    }

    public static Expectation<File> fileExists() {
        return expect("file exists", File::exists);
    }

    public static Expectation<File> isReadableFile() {
        return expect("file exists", File::canRead);
    }

    public static Expectation<File> isWriteableFile() {
        return expect("file exists", File::canWrite);
    }

    public static Expectation<String> path(Expectation<? super File> expectation) {
        return has((String s) -> new File(s)).matching(expectation);
    }

    public static Expectation<String> pathExists() {
        return path(fileExists());
    }

    public static Expectation<String> isReadablePath() {
        return path(isReadableFile());
    }

    public static Expectation<String> isWriteablePath() {
        return path(isWriteableFile());
    }

}
