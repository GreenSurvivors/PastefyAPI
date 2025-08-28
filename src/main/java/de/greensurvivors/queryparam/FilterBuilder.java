package de.greensurvivors.queryparam;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.Paste;
import de.greensurvivors.implementation.queryparam.filter.FilterBuilderImpl;
import org.jetbrains.annotations.NotNull;

import java.util.SequencedSet;

public sealed interface FilterBuilder permits FilterBuilderImpl {

    static FilterBuilder newFilterBuilder() {
        return new FilterBuilderImpl();
    }

    @NotNull SequencedSet<@NotNull FilterParameter<?>> build();

    @NotNull FilterBuilder pasteVisibility(@NotNull Paste.@NotNull PasteVisibility visibility);

    @NotNull FilterBuilder isEncrypted(boolean isEncrypted);

    @NotNull FilterBuilder pasteFolder(@NotNull String folderId);

    @NotNull FilterBuilder userId(@NotNull String userId);

    @NotNull FilterBuilder forkedFromPaste(final @NotNull String pasteId);

    @NotNull FilterBuilder pasteType(final @NotNull Paste.PasteType pasteType);

    @NotNull FilterBuilder starredBy(final @NotNull String userId);

    @NotNull FilterBuilder folderParent(final @NotNull String folderId);

    @NotNull FilterBuilder accountStatus(final @NotNull AccountStaus accountStaus);

    @NotNull FilterBuilder and(@NotNull FilterBuilder other);

    @NotNull FilterBuilder or(@NotNull FilterBuilder other);
}
