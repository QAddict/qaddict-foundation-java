package foundation.qaddict;

import foundation.qaddict.evaluation.ActualValueNode;
import foundation.qaddict.evaluation.CandidateInversionNode;
import foundation.qaddict.evaluation.ComposedNode;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.evaluation.ExpectationNode;
import foundation.qaddict.evaluation.PathNode;
import foundation.qaddict.evaluation.ResultNode;
import foundation.qaddict.evaluation.ThrowableNode;

import java.util.StringJoiner;

import static foundation.qaddict.evaluation.EvaluationNodes.actualValue;
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
        return describe(node.child(), new StringJoiner(" "), "", node.actualValue(), false, "");
    }

    private static String describe(EvaluationNode node, StringJoiner path, Object expectation, Object actualValue, boolean failureCandidate, String prefix) {
        return switch (node) {
            case CandidateInversionNode n -> describe(n.child(), path, expectation, actualValue, failureCandidate, prefix);
            case ResultNode n -> prefix + "Expected: " + path + expectation + ", but was: " + actualValue;
            case PathNode n -> describe(n.child(), path.add(String.valueOf(n.name())), expectation, actualValue, failureCandidate, prefix);
            case ActualValueNode n -> describe(n.child(), path, expectation, n.actualValue(), failureCandidate, prefix);
            case ExpectationNode n -> describe(n.child(), path, n.expectation(), actualValue, failureCandidate, prefix);
            case ThrowableNode n -> prefix + "Expected: " + path + expectation + ", but has thrown: " + n.throwable();
            case ComposedNode n -> prefix + node(expectation, actualValue) + "\n" + n.children().stream().filter(child -> child.result() == failureCandidate)
                    .map(child -> describe(child, new StringJoiner(" "), "", actualValue, failureCandidate, prefix + "\t")).collect(joining("\n"));
        };
    }

    private static String node(Object expectation, Object actualValue) {
        return isNull(expectation) || expectation.toString().isBlank() ? "Item: " + actualValue : "Expected: " + expectation;
    }
}
