package org.qaddict;

import org.qaddict.evaluation.ActualValueNode;
import org.qaddict.evaluation.CandidateInversionNode;
import org.qaddict.evaluation.ComposedNode;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.evaluation.ExpectationNode;
import org.qaddict.evaluation.PathNode;
import org.qaddict.evaluation.ResultNode;
import org.qaddict.evaluation.ThrowableNode;

import java.util.StringJoiner;

import static org.qaddict.evaluation.EvaluationNodes.actualValue;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

/**
 * Assertion entry point
 */
public class Assert {

    /**
     * Assert, that provided data satisfies the expectation.
     * In case, that it satisfies the expectation, then the method returns the data itself.
     * If not, then AssertionError is thrown with description, which extracts the mismatched details.
     * @param data Data to be exercised by the expectation.
     * @param expectation Expectation to apply on the data.
     * @return Tha data, if it meets the expectation.
     * @param <D> Type of the validated data.
     */
    public static <D> D that(D data, Expectation<? super D> expectation) {
        ActualValueNode actualValueNode = actualValue(data, expectation.evaluate(data));
        if(!actualValueNode.result()) throw new AssertionError(describe(actualValueNode));
        return data;
    }

    /**
     * Describe the mismatched parts from the evaluation details.
     * @param node Node representing the full detail of information about the expectation evaluation.
     * @return String with listed mismatches causing the validation failure.
     */
    public static String describe(ActualValueNode node) {
        return describe(node.child(), new StringJoiner(" ", "", " "), Described.description(""), node.actualValue(), false, "");
    }

    private static String describe(EvaluationNode node, StringJoiner path, Described expectation, Object actualValue, boolean failureCandidate, String prefix) {
        return switch (node) {
            case CandidateInversionNode n -> describe(n.child(), path, expectation, actualValue, failureCandidate, prefix);
            case ResultNode ignored -> prefix + "Expected: " + path + expectation + ", but was: " + actualValue;
            case PathNode n -> describe(n.child(), path.add(String.valueOf(n.name())), expectation, actualValue, failureCandidate, prefix);
            case ActualValueNode n -> describe(n.child(), path, expectation, n.actualValue(), failureCandidate, prefix);
            case ExpectationNode n -> describe(n.child(), path, n.description(), actualValue, failureCandidate, prefix);
            case ThrowableNode n -> prefix + "Expected: " + path + expectation + ", but has thrown: " + n.throwable();
            case ComposedNode n -> prefix + node(path, expectation, actualValue) + "\n" + n.children().stream().filter(child -> child.result() == failureCandidate)
                    .map(child -> describe(child, new StringJoiner(" ", "", " "), Described.description(""), actualValue, failureCandidate, prefix + "\t")).collect(joining("\n"));
        };
    }

    private static String node(StringJoiner path, Described expectation, Object actualValue) {
        return isNull(expectation) || expectation.toString().isBlank() ? "Item: " + actualValue : "Expected: " + path + expectation;
    }

}
