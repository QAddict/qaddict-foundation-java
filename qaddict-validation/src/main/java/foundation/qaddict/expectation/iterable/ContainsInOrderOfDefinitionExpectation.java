package foundation.qaddict.expectation.iterable;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.evaluation.ResultNode;

import java.util.Collection;

public record ContainsInOrderOfDefinitionExpectation<D>(Collection<Expectation<? super D>> expectations) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        var iterator = data.iterator();
        if(expectations.stream().allMatch(expectation -> iterator.hasNext() && expectation.evaluate(iterator.next()).result()))
            return new ResultNode(true);
        return new ResultNode(false);
    }

}
