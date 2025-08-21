package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.FolderReply;
import de.greensurvivors.reply.PasteReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class FolderReplyImpl implements FolderReply {
    private final @NotNull String id;
    private final @NotNull String name;
    @SerializedName("user_id")
    private final @Nullable String userId; // the web api is a mess. Why does the paste reply contain the fleshed out public user but the folder reply just the user id?
    @SerializedName("children")
    private final @NotNull Set<@NotNull FolderReplyImpl> subFolders;
    private final  @NotNull Set<@NotNull PasteReplyImpl> pastes;
    @SerializedName("created_at")
    private final @NotNull Instant createdAt;
    private final boolean exists;

    private FolderReplyImpl(@NotNull String id, @NotNull String name,
                            @Nullable String userId,
                            @NotNull Set<@NotNull FolderReplyImpl> subFolders,
                            @NotNull Set<@NotNull PasteReplyImpl> pastes,
                            @NotNull Instant createdAt,
                            boolean exists) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.subFolders = subFolders;
        this.pastes = pastes;
        this.createdAt = createdAt;
        this.exists = exists;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @Nullable String getUserId() {
        return userId;
    }

    @Override
    public @NotNull Set<? extends @NotNull FolderReply> getSubFolders() {
        return subFolders;
    }

    @Override
    public @NotNull Set<? extends @NotNull PasteReply> getPastes() {
        return pastes;
    }

    @Override
    public @NotNull Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean exists() {
        return exists;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }
}
