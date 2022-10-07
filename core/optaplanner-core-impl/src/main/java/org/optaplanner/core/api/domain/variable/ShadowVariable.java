package org.optaplanner.core.api.domain.variable;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.ShadowVariable.List;

/**
 * Specifies that a bean property (or a field) is a custom shadow variable of 1 or more source variables.
 * The source variable may be a genuine {@link PlanningVariable}, {@link PlanningListVariable}, or another shadow variable.
 * <p>
 * It is specified on a getter of a java bean property (or a field) of a {@link PlanningEntity} class.
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface ShadowVariable {

    /**
     * A {@link VariableListener} or {@link ListVariableListener} gets notified after a source planning variable has changed.
     * That listener changes the shadow variable (often recursively on multiple planning entities) accordingly.
     * Those shadow variables should make the score calculation more natural to write.
     * <p>
     * For example: VRP with time windows uses a {@link VariableListener} to update the arrival times
     * of all the trailing entities when an entity is changed.
     *
     * @return a variable listener class
     */
    Class<? extends AbstractVariableListener> variableListenerClass();

    /**
     * The {@link PlanningEntity} class of the source variable.
     * <p>
     * Specified if the source variable is on a different {@link Class} than the class that uses this referencing annotation.
     *
     * @return {@link NullEntityClass} when the attribute is omitted (workaround for annotation limitation).
     *         Defaults to the same {@link Class} as the one that uses this annotation.
     */
    Class<?> sourceEntityClass() default NullEntityClass.class;

    /**
     * The source variable name.
     *
     * @return never null, a genuine or shadow variable name
     */
    String sourceVariableName();

    /**
     * Defines several {@link ShadowVariable} annotations on the same element.
     */
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {

        ShadowVariable[] value();
    }

    /** Workaround for annotation limitation in {@link #sourceEntityClass()}. */
    interface NullEntityClass {
    }
}
