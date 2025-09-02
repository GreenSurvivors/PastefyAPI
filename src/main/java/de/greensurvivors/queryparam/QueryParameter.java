package de.greensurvivors.queryparam;

import de.greensurvivors.Paste;
import de.greensurvivors.admin.queryparam.AdminQueryParameter;
import de.greensurvivors.implementation.queryparam.*;
import de.greensurvivors.implementation.queryparam.filter.AdminFilterBuilderImpl;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public sealed interface QueryParameter<T extends @NotNull Object> permits AdminQueryParameter, AQueryParameter, FilterParameter, FilterTagsParameter, HideSubFoldersParameter, MultiPastePartParameter, PageLimitParameter, PageParameter, SearchParameter, ShortenContentParameter, SortParameter {
    T getValue();

    static @NotNull FilterBuilder newFilterBuilder() {
        return new AdminFilterBuilderImpl();
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

    static @NotNull PageLimitParameter newPageLimitParameter(final int value) {
        return new PageLimitParameterImpl(value);
    }

    static @NotNull PageParameter newPageParameter(final int value) {
        return new PageParameterImpl(value);
    }

    /// note: all searchable aspects (like paste content, enclosing folder, etc.) will get searched at once.
    static @NotNull SearchParameter newSearchParameter(final @NotNull String value) {
        return new SearchParameterImpl(value);
    }

    /// note: all searchable aspects (like paste content, enclosing folder, etc.) will get searched at once.
    /// this is purely a convenient method to provide the correct formatting
    static @NotNull SearchParameter newSearchParameter(final @NotNull Paste.PasteType type) {
        return new SearchParameterImpl.PasteTypeSearchParameterImpl(type);
    }

    /// note: all searchable aspects (like paste content, enclosing folder, etc.) will get searched at once.
    /// this is purely a convenient method to provide the correct formatting
    static @NotNull SearchParameter newSearchParameter(final @NotNull Instant createdAt) {
        return new SearchParameterImpl.CreatedAtSearchParameterImpl(createdAt);
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
