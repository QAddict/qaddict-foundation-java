package foundation.qaddict.evaluation;

public record ActualValueNode(boolean result, Object actualValue, EvaluationNode child) implements EvaluationNode { }
