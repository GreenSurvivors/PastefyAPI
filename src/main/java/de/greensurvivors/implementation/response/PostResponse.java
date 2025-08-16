package de.greensurvivors.implementation.response;

import de.greensurvivors.implementation.PasteReplyImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/// technical class, since the api wraps the getPaste for whatever reason...
public class PostResponse extends SuccessResponse{
    protected final @NotNull PasteReplyImpl paste;

    public PostResponse(boolean success, @NotNull PasteReplyImpl paste) {
        super(success);
        this.paste = paste;
    }

    public @NotNull PasteReplyImpl getPaste() {
        return paste;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PostResponse) obj;
        return this.success == that.success &&
            Objects.equals(this.paste, that.paste);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, paste);
    }

    @Override
    public String toString() {
        return "PostResponse[" +
            "success=" + success + ", " +
            "getPaste=" + paste + ']';
    }

}
