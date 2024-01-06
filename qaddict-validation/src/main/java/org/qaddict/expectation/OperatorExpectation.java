package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.ComposedNode;
import org.qaddict.evaluation.EvaluationNode;

import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public record OperatorExpectation<D>(Collection<Expectation<? super D>> operands, BinaryOperator<Boolean> operator) implements Expectation<D> {

    public static final BinaryOperator<Boolean> AND = (l, r) -> l && r;
    public static final BinaryOperator<Boolean> OR = (l, r) -> l || r;

    @Override
    public EvaluationNode evaluate(D data) {
        List<EvaluationNode> nodes = operands.stream().map(operand -> operand.evaluate(data)).collect(Collectors.toList());
        return new ComposedNode(nodes.stream().map(EvaluationNode::result).reduce(operator).orElse(AND.equals(operator)), nodes);
    }

}
