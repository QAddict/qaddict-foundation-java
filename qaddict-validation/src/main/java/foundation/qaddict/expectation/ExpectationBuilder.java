package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.functions.Transformation;

import java.util.ArrayList;
import java.util.List;

public class ExpectationBuilder<D> implements Expectation<D> {

    private final List<Expectation<? super D>> operands;

    public ExpectationBuilder(List<Expectation<? super D>> operands) {
        this.operands = operands;
    }

    public ExpectationBuilder() {
        this(new ArrayList<>());
    }

    @Override
    public EvaluationNode evaluate(D data) {
        return operands.size() == 1
                ? operands.getFirst().evaluate(data)
                : new OperatorExpectation<>(operands, OperatorExpectation.AND).evaluate(data);
    }

    /*
     * Auto casting builder
     */
    public <E extends D> ExpectationBuilder<E> and(Expectation<? super E> expectation) {
        List<Expectation<? super E>> o = new ArrayList<>(operands);
        o.add(expectation);
        return new ExpectationBuilder<>(o);
    }

    public <E extends D, V> FluentTransformation<V, ExpectationBuilder<E>> and(String name, Transformation<? super E, V> transformation) {
        return expectation -> and(new ExpectationDescription<>(name, new TransformedExpectation<>(transformation, expectation)));
    }

    public <E extends D, V> FluentTransformation<V, ExpectationBuilder<E>> and(Transformation<? super E, V> transformation) {
        return expectation -> and(new TransformedExpectation<>(transformation, expectation));
    }

    @Override
    public String toString() {
        return "Item to match " + operands.size() + " expectations";
    }
}
