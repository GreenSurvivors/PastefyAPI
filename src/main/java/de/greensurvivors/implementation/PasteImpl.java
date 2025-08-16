package de.greensurvivors.implementation;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import de.greensurvivors.Paste;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;

public class PasteImpl<T> implements Paste<T> {
    private @NotNull String title;
    private @NotNull T content;
    private @NotNull PasteType type;
    private @NotNull PasteVisibility visibility;
    @SerializedName("encrypted")
    private boolean isEncrypted;
    @JsonAdapter(value = NullAdapterFactory.class, nullSafe = false)
    @SerializedName("expire_at")
    private @Nullable Instant expirationTime;
    private @NotNull Collection<@NotNull String> tags;

    public PasteImpl(final @NotNull String title, final @NotNull T content, final @NotNull PasteType type,
                     final @NotNull PasteVisibility visibility, final boolean isEncrypted,
                     final @Nullable Instant expirationTime,
                     final @NotNull Collection<@NotNull String> tags) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.visibility = visibility;
        this.isEncrypted = isEncrypted;
        this.expirationTime = expirationTime;
        this.tags = tags;
    }

    @Override
    public @NotNull PasteType getType() {
        return type;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public @NotNull T getContent() {
        return content;
    }

    @Override
    public @NotNull PasteVisibility getVisibility() {
        return visibility;
    }

    @Override
    public boolean isEncrypted() {
        return isEncrypted;
    }

    @Override
    public @Nullable Instant getExpirationTime() {
        return expirationTime;
    }

    @Override
    public @NotNull Collection<@NotNull String> getTags() {
        return tags;
    }
}
