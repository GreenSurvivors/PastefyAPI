package de.greensurvivors.implementation;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import de.greensurvivors.Paste;
import de.greensurvivors.PasteBuilder;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;

public class PasteImpl<T> implements Paste<T> {
    protected @NotNull String title;
    protected @NotNull T content;
    private final @NotNull PasteType type;
    private final @NotNull PasteVisibility visibility;
    @SerializedName("encrypted")
    private final boolean isEncrypted;
    @JsonAdapter(value = NullAdapterFactory.class, nullSafe = false)
    @SerializedName("expire_at")
    private final @Nullable Instant expirationTime;
    private final @NotNull Collection<@NotNull String> tags;
    @SerializedName("folder")
    private final @Nullable String folderId;
    private final @Nullable String pasteIdForkedFrom;

    public PasteImpl(final @NotNull PasteBuilder<T> pasteBuilder) {
        this.title = pasteBuilder.getTitle();
        this.content = pasteBuilder.getContent();
        this.type = pasteBuilder.getType();
        this.visibility = pasteBuilder.getVisibility();
        this.isEncrypted = pasteBuilder.isEncrypted();
        this.expirationTime = pasteBuilder.getExpirationTime();
        this.tags = pasteBuilder.getTags();
        this.folderId = pasteBuilder.getFolderId();
        this.pasteIdForkedFrom = pasteBuilder.getPasteIdForkedFrom();
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

    @Override
    public @Nullable String getFolderId() {
        return folderId;
    }

    @Override
    public @Nullable String getPasteIdForkedFrom() {
        return pasteIdForkedFrom;
    }

    @Override
    public @NotNull <NewT> PasteBuilder<NewT> newTypedBuilder(@NotNull PasteContent<NewT> newTypedPasteContent) {
        return Paste.newBuilder(getTitle(), newTypedPasteContent).
            setTitle(getTitle()).
            setVisibility(getVisibility()).
            setExpirationTime(getExpirationTime()).
            setTags(getTags()).
            setFolderId(getFolderId()).
            setPasteIdForkedFrom(getPasteIdForkedFrom());
    }
}
