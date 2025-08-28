package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.filter.AProtoFilterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface FilterParameter<T extends @NotNull Object> extends QueryParameter<T> permits AProtoFilterImpl.FilterImpl {
}
