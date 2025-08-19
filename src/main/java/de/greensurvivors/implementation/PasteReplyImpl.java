package de.greensurvivors.implementation;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.PasteBuilder;
import de.greensurvivors.PasteReply;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Instant;

public class PasteReplyImpl extends PasteImpl<String> implements PasteReply {
    private final @NotNull String id;
    private final boolean exists;
    @SerializedName("raw_url")
    private final @NotNull URI rawURL;
    @SerializedName("created_at")
    private final @NotNull Instant createdAt;
    @SerializedName("user_id")
    private final @Nullable String userID;

    // unused, this class gets instanced by gson!
    private PasteReplyImpl(@NotNull PasteBuilder<String> pasteBuilder,
                           @NotNull String id, boolean exists,
                           @NotNull URI rawURL, @NotNull Instant createdAt,
                           @Nullable String userID) {
        super(pasteBuilder);
        this.id = id;
        this.exists = exists;
        this.rawURL = rawURL;
        this.createdAt = createdAt;
        this.userID = userID;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public @Nullable String getUserId() {
        return userID;
    }

    @Override
    public @NotNull URI getRawURL() {
        return rawURL;
    }

    @Override
    public @NotNull PasteReply decrypt(byte @NotNull [] password) throws InvalidCipherTextException {
        this.title = EncryptionHelper.decrypt(this.getTitle(), password);
        this.content = EncryptionHelper.decrypt(this.getContent(), password);

        return this;
    }

    public boolean exists() {
        return exists;
    }
}
