package org.qaddict.evaluation;

public record CandidateInversionNode(boolean result, EvaluationNode child) implements EvaluationNode { }
