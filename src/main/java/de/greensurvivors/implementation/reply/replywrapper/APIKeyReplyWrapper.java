package de.greensurvivors.implementation.reply.replywrapper;

import org.jetbrains.annotations.NotNull;

public class APIKeyReplyWrapper extends SuccessReply {
    private final @NotNull String key;

    protected APIKeyReplyWrapper(final boolean success, @NotNull String key) {
        super(success);
        this.key = key;
    }

    public @NotNull String getKey() {
        return key;
    }
}
