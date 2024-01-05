package foundation.qaddict.evaluation;

public record ExpectationNode(boolean result, Object expectation, EvaluationNode child) implements EvaluationNode {
}
