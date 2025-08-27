package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.queryparam.FilterBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.SequencedMap;

public class FilterBuilderImpl implements FilterBuilder {
    private final @NotNull SequencedMap<@NotNull FilterConnection, @NotNull AFilterImpl<?>> filters = new LinkedHashMap<>();


    public enum FilterConnection {
        AND("$and"),
        OR("$OR"),
        EQUALS("$EQ");

        private final @NotNull String internalName;

        FilterConnection(final @NotNull String internalName) {
            this.internalName = internalName;
        }
    }
}
