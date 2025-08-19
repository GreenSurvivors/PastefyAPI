package de.greensurvivors.implementation.response;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.FolderReply;
import de.greensurvivors.implementation.FolderReplyImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/// technical class, since the api wraps the created Folder for whatever reason...
public class FolderResponse extends SuccessResponse {
    @SerializedName("folder")
    protected final @NotNull FolderReplyImpl folderReply;

    protected FolderResponse(final boolean success, final @NotNull FolderReplyImpl folderReply) {
        super(success);
        this.folderReply = folderReply;
    }

    public @NotNull FolderReply getFolder() {
        return folderReply;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FolderResponse) obj;
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
