package de.greensurvivors;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public interface PublicUserReply {
    @NotNull String getId();

    @NotNull String getName();

    @NotNull URL getAvatarURL() throws MalformedURLException;

    String getDisplayName();
}
