package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Instant;

public interface PasteReply extends Paste<String> {
    @NotNull String getId();

    @NotNull Instant getCreatedAtInstance();

    @Nullable String getUserId();

    @NotNull URI getRawURL();

    boolean exists ();
}
