package de.greensurvivors.implementation.reply.replywrapper;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class ErrorReply {
    private final boolean success; // always false
    private final boolean exists; // always false
    @SerializedName("exception")
    private final @NotNull String exceptionName; // simple exception name

    private ErrorReply(final boolean success, final boolean exists, final @NotNull String exceptionName) {
        this.success = success;
        this.exists = exists;
        this.exceptionName = exceptionName;
    }

    public @NotNull String getExceptionName() {
        return exceptionName;
    }
}
