package de.greensurvivors.implementation.reply.replywrapper;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.PasteReply;
import de.greensurvivors.implementation.reply.PasteReplyImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/// technical class, since the api wraps the created Paste for whatever reason...
public class PasteReplyWrapper extends SuccessReply {
    @SerializedName("paste")
    protected final @NotNull PasteReplyImpl pasteReply;

    protected PasteReplyWrapper(boolean success, @NotNull PasteReplyImpl pasteReply) {
        super(success);
        this.pasteReply = pasteReply;
    }

    public @NotNull PasteReply getPaste() {
        return pasteReply;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final PasteReplyWrapper that = (PasteReplyWrapper) obj;
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
