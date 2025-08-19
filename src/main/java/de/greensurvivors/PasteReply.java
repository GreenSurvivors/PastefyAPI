package de.greensurvivors;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Instant;

public interface PasteReply extends Paste<String> {
    @NotNull String getId();

    @NotNull Instant getCreatedAt();

    @Nullable String getUserId();

    @NotNull URI getRawURL();

    @NotNull PasteReply decrypt(final byte @NotNull [] password) throws InvalidCipherTextException;

    // I really have no idea why the api has this field, if the api answers with status code 404 if a paste doesn't exist...
    boolean exists ();
}
