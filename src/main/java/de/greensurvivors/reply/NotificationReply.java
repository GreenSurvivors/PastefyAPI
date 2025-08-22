package de.greensurvivors.reply;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

@SuppressWarnings("unused") // api no further test reasonable
public interface NotificationReply {
    int getId();

    @NotNull String getMessage();

    @NotNull String getUserId();

    @NotNull URL getUrl() throws MalformedURLException;

    boolean wasAlreadyRead();

    boolean wasReceived();

    @NotNull Instant getCreatedAt();

    @Nullable Instant getUpdatedAt();
}
