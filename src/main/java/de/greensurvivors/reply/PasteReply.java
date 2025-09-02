package de.greensurvivors.reply;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteBuilder;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Instant;

public interface PasteReply extends Paste<String> {
    @NotNull String getId();

    @NotNull Instant getCreatedAt();

    @Nullable PublicUserReply getUser();

    @NotNull URI getRawURL();

    @Nullable Boolean isStarred();

    /// decrypts itself
    void decrypt(final byte @NotNull [] password) throws InvalidCipherTextException;

    @NotNull PasteBuilder<String> toPasteBuilder();
}
