# QAddict validation org

> ,,Assurance of quality is about precise validation, that actual behavior meets `Expectation`''

_QAddict_ brings 

## 1. Quick start
### 1.1 Dependency
The library is available in Maven central repository. So with maven or gradle simply use the following dependency.

```xml

<dependency>
    <groupId>org.qaddict</groupId>
    <artifactId>qaddict-validation</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 1.2 First validation

To start immediately using the validations using this library, one needs two classes:
- Ready to use `Expectations` a.k.a. predicates to be applied on the validated data, and to be combined to more complex ones.
- Mechanism to `Assert` that the data meet expectation, and fails the test if not.

Simple validation by comparing actual value to expected value:

```java
import org.qaddict.Assert;

import static org.qaddict.Expectations.equalTo;

public class SimpleTest {

    @Test
    public void test() {
        String actualValue = getActualValue();

        Assert.that(actualValue, equalTo("A"));
    }

}
```
The class `Expectations` is source of many useful methods. You can explore them later.
To demonstrate quickly the power of the available tools see following examples.

```java
import org.qaddict.Assert;

import java.time.LocalDate;

import static org.qaddict.Expectations.allOf;
import static org.qaddict.Expectations.has;
import static java.time.DayOfWeek.MONDAY;
import static java.time.Month.JANUARY;

public class PowerTest {

    @Test
    public void test() {
        LocalDate actualDate = getActualDate();
        Assert.that(actualDate,
                has(LocalDate::getDayOfWeek).equalTo(MONDAY)
                        .and(LocalDate::getMonth).equalTo(JANUARY));
    }

}
```

## 2. Separation of concerns

Validations play key role in testing, and the more in test automation. It's not only very important
to validate the tested behavior, but actually every step even if it's preparation or cleanup.
We have to be sure, that the system got into a state, where we can trigger the behavior to be tested,
and also potentially verify, that the system is back in a state ready for next test.

From that we can sense following roles of validation:

- Ensuring, that the current state meets defined expectations
- Synchronizing the actions to achieve consistent test execution.
  - Keep in mind, that automated test runs extremely fast compared to manual
    testing. Things you may not notice manually (e.g. button gets enabled asynchronously
    after e.g. 50ms), but can make the automated action fail (clicking disabled button will not work).


The idea behind the tools here, is to help in following areas

1. Allow composition of multiple expectations together to build complex ones.
2. Provide ready to use expectations.
3. Consider asynchronous behavior of the tested system.
4. Separate evaluation, making the test fail on mismatch.
5. Provide detailed information about evaluation of data against expectation.

Individual areas in more detail:

### 2.1 Basic interface `Expectation`

Main interface for preparing expectations of any complexity in _QAddict_ is `Expectation`.
You can think of it as a predicate.

It's generic, and it contains 1 method (plus one default shortcut method):

```java
import org.qaddict.evaluation.EvaluationNode;

public interface Expectation<D> {

    EvaluationNode evaluate(D data);

}
```
It accepts the data to be evaluated, and returns full detail about the evaluation.
`EvaluationNode` (the result) besides other contains boolean result, which indicates,
if the data meet the expectation or not.

The "composite design pattern" is used here.

The library provides some implementations, which are simple, and directly implement the logic
to be applied (leafs).

Examples are:
```java
equalTo("A");
isNull();
nonNull();
moreThan(10);
matchesPattern("\\d+");
```

Other implementation provided, are used to compose other expectations (both simple and composed) into more complex ones (nodes).

Examples are:
```java
not(expectation);
allOf(expectations);
anyOf(expectations);
has(transformation).matching(expectation)
```

### 2.2 Ready to use expectations

Rich source of ready to use expectations and expectation factory methods is the class

```java
org.qaddict.Expectations;
```

Always check this class first for existing methods.

### 2.3 Ready for asynchronous behavior

In test automation of higher level than unit tests, the asynchronous behavior is pretty much daily bread.
E.g. click in the Web GUI triggers asynchronous JSON call, and response is only visible after it's finished.
So immediate check may fail.

There is support for 2 ways, how to handle asynchonicity.

#### 2.3.1 Polling for state updates

One way is regular checking (polling) for a state.



#### 2.3.2 Asynchronous events / messages.

Another way suits cases, when there is some source of asynchronous events, and we may
be receiving them at any time.

Solution for this has 2 parts.
There typically needs to be a dedicated thread, which is responsible only for receiving the events, and passing
them to registered listener.

On the other hand, we need to validate received messages.

The connection between those two, is already in Java. It's `BlockingQueue` / `BlockingDequeue`.
_QAddict_ provides many ready to use expectations for `Iterable`. Although `BlockingQueue` extends `Iterable`,
it only iterates over currently present elements.

In order to apply those expectations as expected for asynchronous events (wait for next event, if not yet received),
then we need to have `Iterable`, which can block execution until next event comes.

The solution is an adapter of `BlockingQueue` to `Iterable`:

```java
org.qaddict.adapters.BlockingIterable;
```

### 2.4 Failing the test

We have learned about `Expectation`, that it simply returns evaluation detail (or shortcut method `test()` returns boolean).
That on its own doesn't fail the test as such (convention in Java test runners is to throw an exception).
Even more. Evaluation of expectation __must not throw any exception__. Any code throwing an exception within
expectation evaluation, is caught and turned into result value indicating failure and keeping the exception in the details.

Failing the test is responsibility of the class `Assert`.

Example:
```java
Assert.that(data, expectation);
```

The method `Assert.that()` will evaluate the expectation on provided data. In case of mismatch
it extracts from returned details all pieces of information, related to the failure (all mismatches causing overall failure).

### 2.5 Evaluation details (a.k.a. transparency)

Evaluation details are represented via hierarchy of classes implementing `EvaluationNode`.
Each expectation is responsible for returning relevant node hierarchy.

Node expectations additionally need to incorporate partial nodes from evaluation of their children.
