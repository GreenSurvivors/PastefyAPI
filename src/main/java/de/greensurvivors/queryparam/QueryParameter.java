package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public sealed interface QueryParameter<T extends @NotNull Object> permits AQueryParameter, FilterParameter, FilterTagsParameter, HideSubFoldersParameter, PageLimitParameter, PageParameter, SearchParameter, ShortenContentParameter, SortParameter {
    T getValue();

    static @NotNull FilterBuilder newFilterBuilder() {
        return FilterBuilder.newFilterBuilder();
    }

    static @NotNull FilterTagsParameter newFilterTagsParameter(final @NotNull Set<@NotNull String> value) {
        return new FilterTagsParameterImpl(value);
    }

    static @NotNull HideSubFoldersParameter newHideSubFoldersParameter() {
        return new HideSubFoldersParameterImpl(true);
    }

    static @NotNull HideSubFoldersParameter newHideSubFoldersParameter(final @NotNull Boolean value) {
        return new HideSubFoldersParameterImpl(value);
    }

    static @NotNull PageLimitParameter newPageLimitParameter (final int value){
        return new PageLimitParameterImpl(value);
    }

    static @NotNull PageParameter newPageParameter(final int value) {
        return new PageParameterImpl(value);
    }

    static @NotNull SearchParameter newSearchParameter (final @NotNull String value) {
        return new SearchParameterImpl(value);
    }

    static @NotNull ShortenContentParameter newShortenContentParameter() {
        return new ShortenContentParameterImpl();
    }

    static @NotNull ShortenContentParameter newShortenContentParameter(final boolean value) {
        return new ShortenContentParameterImpl(value);
    }

    static @NotNull SortParameter newSortParameter(final @NotNull Map<SortParameter.@NotNull SortType, @NotNull Boolean> value) {
        return new SortParameterImpl(value);
    }
}
