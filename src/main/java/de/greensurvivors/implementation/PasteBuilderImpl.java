package de.greensurvivors.implementation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import de.greensurvivors.PasteBuilder;
import de.greensurvivors.PasteContent;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

// sorry for the final, but I can't take encryption lightly.
public final class PasteBuilderImpl<T> implements PasteBuilder<T> {
    private @Nullable String title;
    private @NotNull PasteContent<T> content;
    private @NotNull PasteVisibility visibility = PasteVisibility.UNLISTED;
    private transient @Nullable EncryptionHelper.HashedPasskey hashedPasskey = null;
    @SerializedName("expire_at")
    private @Nullable Instant expirationTime = null;
    private @NotNull Collection<@NotNull String> tags = Collections.emptyList();
    @SerializedName("folder")
    private @Nullable String folderId = null;
    @SerializedName("forkedFrom")
    private @Nullable String pasteIdForkedFrom = null;
    @SerializedName("ai")
    private boolean useAI = false;

    public PasteBuilderImpl (final @NotNull PasteContent<T> content) {
        this.content = content;
    }

    @Override
    public @NotNull PasteBuilder<T> setTitle(final @Nullable String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setContent(final @NotNull PasteContent<T> content) {
        this.content = content;
        return this;
    }

    @Override
    public @NotNull PasteContent<T> getPackagedContent() {
        return content;
    }

    @Override
    public @NotNull PasteBuilder<T> setVisibility(final @NotNull PasteVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> encryptWhenSending(final byte @Nullable [] password) throws NoSuchAlgorithmException {
        if (password != null) {
            this.hashedPasskey = EncryptionHelper.hashPasskey(password);
        } else {
            this.hashedPasskey = null;
        }
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setExpirationTime(final @Nullable Instant expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setTags(final @NotNull Collection<@NotNull String> tags) {
        this.tags = new LinkedHashSet<>(tags); // copy tags into new collection to prevent dumb issues
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> addTag(final @NotNull String tag) {
        if (tags == Collections.EMPTY_LIST){
            tags = new LinkedHashSet<>();
        }

        tags.add(tag);

        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setFolderId(final @Nullable String folderId) {
        this.folderId = folderId;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setPasteIdForkedFrom(final @Nullable String pasteIdForkedFrom) {
        this.pasteIdForkedFrom = pasteIdForkedFrom;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> useAI(boolean useAI) {
        this.useAI = useAI;
        return this;
    }

    @Override
    public boolean doesUseAI() {
        return useAI;
    }

    @Override
    public @NotNull PasteType getType() {
        return content.getPasteType();
    }

    @Override
    public @Nullable String getTitle() {
        return title;
    }

    @Override
    public @NotNull T getContent() {
        return content.getContent();
    }

    @Override
    public @NotNull PasteVisibility getVisibility() {
        return visibility;
    }

    @Override
    public boolean isEncrypted() {
        return hashedPasskey != null;
    }

    @Override
    public @Nullable Instant getExpirationTime() {
        return expirationTime;
    }

    @Override
    public @NotNull @Unmodifiable Collection<String> getTags() {
        return List.copyOf(tags); // copy tags into new collection to prevent dumb issues
    }

    @Override
    public @Nullable String getFolderId() {
        return folderId;
    }

    @Override
    public @Nullable String getPasteIdForkedFrom() {
        return pasteIdForkedFrom;
    }

    private @Nullable EncryptionHelper.HashedPasskey getHashedPasskey() {
        return hashedPasskey;
    }

    public final static class PasteBuilderJsonSerializer implements JsonSerializer<PasteBuilderImpl<?>> {

        @Override
        public JsonElement serialize(PasteBuilderImpl<?> src, Type typeOfSrc, JsonSerializationContext context) {
            final @NotNull JsonObject resultObj = new JsonObject();

            if (src.isEncrypted()) {
                try {
                    resultObj.add("title",
                        context.serialize(EncryptionHelper.encrypt(src.getTitle(), src.getHashedPasskey())));
                    resultObj.add("content",
                        context.serialize(EncryptionHelper.encrypt(src.getPackagedContent().serialize(), src.getHashedPasskey())));
                } catch (NoSuchAlgorithmException | InvalidCipherTextException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                resultObj.add("title", context.serialize(src.getTitle()));
                try {
                    resultObj.add("content", context.serialize(src.getPackagedContent().serialize()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            resultObj.add("visibility", context.serialize(src.getVisibility()));
            resultObj.add("encrypted", context.serialize(src.isEncrypted()));
            resultObj.add("expire_at", context.serialize(src.getExpirationTime()));
            resultObj.add("tags", context.serialize(src.getTags()));
            resultObj.add("folder", context.serialize(src.getFolderId()));
            resultObj.add("forkedFrom", context.serialize(src.getPasteIdForkedFrom()));
            resultObj.add("ai", context.serialize(src.doesUseAI()));

            return resultObj;
        }
    }
}
