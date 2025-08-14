package de.greensurvivors;

import de.greensurvivors.implementation.PasteBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;

@SuppressWarnings("unused") // this is API
public interface Paste<T> {
    @NotNull PasteType getType ();
    @NotNull String getTitle ();
    @NotNull T getContent();
    @NotNull PasteVisibility getVisibility();
    boolean isEncrypted ();
    @Nullable Instant getExpiration();
    @NotNull Collection<String> getTags();

    static <T> PasteBuilder<T> newBuilder(@NotNull String title, @NotNull PasteContent<T> content) {
        return new PasteBuilderImpl<T>(title, content);
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
