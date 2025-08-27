package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.implementation.queryparam.filter.AFilterImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public class FilterParameter extends AQueryParameter<@NotNull Set<@NotNull AFilterImpl<@NotNull Object>>> {

    protected FilterParameter(final @NotNull Set<@NotNull AFilterImpl<@NotNull Object>> filters) {
        super("filter", filters);
    }

    @Override
    public @NotNull @Unmodifiable Set<@NotNull AFilterImpl<@NotNull Object>> getValue() {
        return Set.copyOf(super.getValue());
    }

    @Override
    public @NotNull String getFormData() { // todo
        return ""; // super.getValue().stream().map().getFormData();
    }
}
