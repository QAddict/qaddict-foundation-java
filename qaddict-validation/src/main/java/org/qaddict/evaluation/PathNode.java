package org.qaddict.evaluation;

public record PathNode(boolean result, Object name, EvaluationNode child) implements EvaluationNode {
}
