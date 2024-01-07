package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.functions.Transformation;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractExpectationBuilder<T, D> implements Expectation<D> {

    private final List<Expectation<? super D>> operands;
    private final OperatorExpectation<D> operator;

    public AbstractExpectationBuilder(List<Expectation<? super D>> operands) {
        this.operands = operands;
        this.operator = new OperatorExpectation<>(operands, OperatorExpectation.AND);
    }

    public AbstractExpectationBuilder() {
        this(new ArrayList<>());
    }

    @Override
    public EvaluationNode evaluate(D data) {
        return operands.size() == 1 ? operands.getFirst().evaluate(data) : operator.evaluate(data);
    }

    @Override
    public Object description() {
        return operands.size() > 5 ? "Item to match " + operands.size() + " expectations" : operator.description();
    }

    public T and(Expectation<? super D> expectation) {
        operands.add(expectation);
        return self();
    }

    public <V> FluentTransformation<V, T> and(String name, Transformation<? super D, V> transformation) {
        return expectation -> and(new ExpectationDescription<>(name, new TransformedExpectation<>(transformation, expectation)));
    }

    public <V> FluentTransformation<V, T> and(Transformation<? super D, V> transformation) {
        return expectation -> and(new TransformedExpectation<>(transformation, expectation));
    }

    abstract protected T self();

}
