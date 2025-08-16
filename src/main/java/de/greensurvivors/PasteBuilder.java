package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;

// sorry for the sealed, but I can't take encryption lightly
public sealed interface PasteBuilder<T> extends Paste<T> permits de.greensurvivors.implementation.PasteBuilderImpl {
    PasteBuilder<T> setTitle(@NotNull String title);

    PasteBuilder<T> setContent(@NotNull PasteContent<T> content);

    PasteContent<T> getPackagedContent();

    PasteBuilder<T> setVisibility(@NotNull PasteVisibility visibility);

    PasteBuilder<T> encryptWhenSending(byte @Nullable [] password) throws NoSuchAlgorithmException;

    PasteBuilder<T> setExpirationTime(@Nullable Instant expirationTime); // note: I have no Idea what timezone they use. Maybe german?? Maybe GMT???

    PasteBuilder<T> setTags(@NotNull Collection<String> tags);

    PasteBuilder<T> addTag(@NotNull String tag);
}
