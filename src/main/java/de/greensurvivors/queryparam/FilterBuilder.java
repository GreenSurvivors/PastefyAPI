package de.greensurvivors.queryparam;

import de.greensurvivors.Paste;
import de.greensurvivors.admin.queryparam.AdminFilterBuilder;
import de.greensurvivors.exception.NestedFilterException;
import de.greensurvivors.exception.UnsupportedFilterException;
import org.jetbrains.annotations.NotNull;

import java.util.SequencedSet;

public sealed interface FilterBuilder permits AdminFilterBuilder {

    @NotNull SequencedSet<@NotNull FilterParameter<?>> build() throws UnsupportedFilterException;

    @NotNull FilterBuilder pasteVisibility(final @NotNull Paste.@NotNull PasteVisibility visibility);

    @NotNull FilterBuilder isEncrypted(final boolean isEncrypted);

    @NotNull FilterBuilder pasteFolder(final @NotNull String folderId);

    @NotNull FilterBuilder userId(final @NotNull String userId);

    @NotNull FilterBuilder forkedFromPaste(final @NotNull String pasteId);

    @NotNull FilterBuilder pasteType(final @NotNull Paste.PasteType pasteType);

    @NotNull FilterBuilder starredBy(final @NotNull String userId);

    @NotNull FilterBuilder folderParent(final @NotNull String folderId);

    @NotNull FilterBuilder and(final @NotNull FilterBuilder other);

    @NotNull FilterBuilder or(final @NotNull FilterBuilder other);


    @NotNull FilterBuilder not(final @NotNull FilterBuilder other) throws NestedFilterException;

    @NotNull FilterBuilder isNull(final @NotNull FilterBuilder other) throws NestedFilterException;

    @NotNull FilterBuilder notNull(final @NotNull FilterBuilder other) throws NestedFilterException;

    @NotNull FilterBuilder greaterThan(final @NotNull FilterBuilder other) throws NestedFilterException;

    @NotNull FilterBuilder greaterThenOrEquals(final @NotNull FilterBuilder other) throws NestedFilterException;

    @NotNull FilterBuilder lowerThan(final @NotNull FilterBuilder other) throws NestedFilterException;

    @NotNull FilterBuilder lowerThenOrEquals(final @NotNull FilterBuilder other) throws NestedFilterException;
}
