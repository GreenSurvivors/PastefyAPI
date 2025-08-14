package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;

public interface PasteBuilder<T> extends Paste<T> {
    void setTitle(@NotNull String title);

    void setContent(@NotNull PasteContent<T> content);

    PasteContent<T> getPackagedContent();

    void setVisibility(@NotNull PasteVisibility visibility);

    void encrypt(byte @Nullable [] password) throws NoSuchAlgorithmException;

    void setExpiration(@Nullable Instant expiration); // note: I have no Idea what timezone they use. Maybe german?? Maybe GMT???

    void setTags(@NotNull Collection<String> tags);

    void addTag(@NotNull String tag);
}
