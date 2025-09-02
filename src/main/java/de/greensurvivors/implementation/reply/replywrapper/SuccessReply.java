package de.greensurvivors.implementation.reply.replywrapper;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SuccessReply {
    protected final boolean success; /// always true, as far as I know.

    protected SuccessReply(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        return this.success == ((SuccessReply) obj).success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(success);
    }

    @Override
    public String toString() {
        return "SuccessResponse[" +
            "success=" + success + ']';
    }
}
