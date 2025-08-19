package de.greensurvivors.implementation.response;

import de.greensurvivors.PasteReply;
import de.greensurvivors.implementation.PasteReplyImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/// technical class, since the api wraps the created Paste for whatever reason...
public class PostResponse extends SuccessResponse {
    protected final @NotNull PasteReply pasteReply;

    public PostResponse(boolean success, @NotNull PasteReplyImpl pasteReply) {
        super(success);
        this.pasteReply = pasteReply;
    }

    public @NotNull PasteReply getPaste() {
        return pasteReply;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PostResponse) obj;
        return this.success == that.success &&
            Objects.equals(this.pasteReply, that.pasteReply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, pasteReply);
    }

    @Override
    public String toString() {
        return "PostResponse[" +
            "success=" + success + ", " +
            "paste=" + pasteReply + ']';
    }
}
