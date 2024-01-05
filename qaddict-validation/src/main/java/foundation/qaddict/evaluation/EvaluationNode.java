package foundation.qaddict.evaluation;

/**
 * Representation of subtree of detailed information about the evaluation (application of an expectation on provided
 * data).
 * As the expectation as such can consist of partial ones, each of them providing piece of information into the full
 * detail, also the evaluation detail is structured as a tree of nodes, where each of them provides its piece of
 * evaluation detail.
 */
public sealed interface EvaluationNode

        permits

        /*
         * Actual value node keeps the information in the evaluation details about actual value used by the expectation
         * further.
         * Such node is created at the top level (responsibility of Assert.that() method), and then at each transformation
         * as well as iteration over elements in iterable.
         */
        ActualValueNode,

        /*
         * Expectation node keeps description of the expectation (typically leaf expectation, e.g. expected value,
         * pattern to be matched, range to fall in etc.).
         */
        ExpectationNode,

        /*
         * Path node keeps naming of a transformation node (e.g. property access via getter).
         */
        PathNode,

        /*
         * Result node keeps just information about leaf result.
         */
        ResultNode,

        /*
         * Throwable node keeps information about any throwable thrown by any of the pieces.
         */
        ThrowableNode,

        /*
         * Composed node keeps evaluation nodes from multiple children (e.g. general or binary AND, OR operations, or
         * iterable results).
         */
        ComposedNode,

        /*
         * Candidate inversion node is marker indication, that from now on, the failure cause candidates are indicated
         * by negated value (initially failure candidate is indicated by result = false, and every negation inverts it).
         */
        CandidateInversionNode

{

    /**
     * Overall boolean result of the expectation evaluation.
     * @return Boolean indicating, if tested data matched the expectation of not.
     */
    boolean result();

}
