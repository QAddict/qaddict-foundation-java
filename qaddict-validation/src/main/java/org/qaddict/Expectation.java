package org.qaddict;

import org.qaddict.evaluation.EvaluationNode;

/**
 * Basic interface representing an expectation, applicable on validated data.
 * This is the main entry interface, used to implement the Composite design pattern for composing complex validations
 * by re-usable pieces.
 * @param <D> Type of the data to be validated.
 */
public interface Expectation<D> extends Described {

    /**
     * Evaluate the expectation on provided data.
     * This method returns full detail of the evaluation.
     * As various implementations can also compose partial expectations together, each of them may provide partial details,
     * collected to full detail of description, what was happening during the validation.
     * @param data Validated data.
     * @return EvaluationNode containing details of validation using this expectation in the subtree of this expectation.
     */
    EvaluationNode evaluate(D data);

    /**
     * Shortcut method, which performs the method evaluate() and only returns its boolean result. This allows simplified
     * usage, when the full detail is not required. It also allows `Predicate` like simple use.
     * @param data Validated data.
     * @return True if data satisfy the expectation, otherwise false.
     */
    default boolean test(D data) {
        return evaluate(data).result();
    }

}
