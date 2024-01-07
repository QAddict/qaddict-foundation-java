package org.qaddict.evaluation;

import org.qaddict.Described;

public record ExpectationNode(boolean result, Described description, EvaluationNode child) implements EvaluationNode { }
