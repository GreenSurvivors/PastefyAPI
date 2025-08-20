package de.greensurvivors;

import de.greensurvivors.implementation.PasteBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;

public interface Paste<T> {
    @NotNull PasteType getType ();
    @Nullable String getTitle ();
    @NotNull T getContent();
    @NotNull PasteVisibility getVisibility();
    boolean isEncrypted ();
    @Nullable Instant getExpirationTime();
    @NotNull Collection<String> getTags();

    @Nullable String getFolderId();
    @Nullable String getPasteIdForkedFrom();

    static <T> PasteBuilder<T> newBuilder(@NotNull PasteContent<T> content) {
        return new PasteBuilderImpl<T>(content);
    }

    enum PasteType {
        /// default
        PASTE,
        MULTI_PASTE
    }

    enum PasteVisibility {
        PUBLIC,
        /// default
        UNLISTED,
        PRIVATE
    }
}
