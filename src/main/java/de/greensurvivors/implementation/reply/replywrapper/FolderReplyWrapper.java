package de.greensurvivors.implementation.reply.replywrapper;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.implementation.reply.FolderReplyImpl;
import de.greensurvivors.reply.FolderReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/// technical class, since the api wraps the created Folder for whatever reason...
public class FolderReplyWrapper extends SuccessReply {
    @SerializedName("folder")
    protected final @NotNull FolderReplyImpl folderReply;

    protected FolderReplyWrapper(final boolean success, final @NotNull FolderReplyImpl folderReply) {
        super(success);
        this.folderReply = folderReply;
    }

    public @NotNull FolderReply getFolder() {
        return folderReply;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final FolderReplyWrapper that = (FolderReplyWrapper) obj;
        return this.success == that.success &&
            Objects.equals(this.folderReply, that.folderReply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, folderReply);
    }

    @Override
    public String toString() {
        return "FolderResponse[" +
            "success=" + success + ", " +
            "folderReply=" + folderReply + ']';
    }
}
