package io.quarkus.deployment.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.builder.item.EmptyBuildItem;

/**
 * Declare that this step comes before the given build items are consumed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Produce.List.class)
public @interface Produce {
    /**
     * The build item type whose consumption is preceded by this step.
     *
     * @return the build item
     */
    Class<? extends EmptyBuildItem> value();

    /**
     * The repeatable holder for {@link Produce}.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         * The {@link Produce} instances.
         *
         * @return the instances
         */
        Produce[] value();
    }
}