package de.greensurvivors.implementation.response;

import de.greensurvivors.FolderReply;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/// technical class, since the api wraps the created Folder for whatever reason...
public class FolderResponse extends SuccessResponse {
    protected final @NotNull FolderReply folderReply;

    public FolderResponse(final boolean success, final @NotNull FolderReply folderReply) {
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
