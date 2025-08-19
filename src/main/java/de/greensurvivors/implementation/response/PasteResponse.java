package de.greensurvivors.implementation.response;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.PasteReply;
import de.greensurvivors.implementation.PasteReplyImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/// technical class, since the api wraps the created Paste for whatever reason...
public class PasteResponse extends SuccessResponse {
    @SerializedName("paste")
    protected final @NotNull PasteReplyImpl pasteReply;

    protected PasteResponse(boolean success, @NotNull PasteReplyImpl pasteReply) {
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
        var that = (PasteResponse) obj;
        return this.success == that.success &&
            Objects.equals(this.pasteReply, that.pasteReply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, pasteReply);
    }

    @Override
    public String toString() {
        return "PastResponse[" +
            "success=" + success + ", " +
            "paste=" + pasteReply + ']';
    }
}
