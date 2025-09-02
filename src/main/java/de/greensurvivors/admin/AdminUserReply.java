package de.greensurvivors.admin;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.reply.PublicUserReply;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface AdminUserReply extends PublicUserReply {
    /// interaapps, google, github,  twitch, oidc aka custom, discord
    @NotNull String AuthenticationProviderName();

    @NotNull AccountStaus getStatus();

    @NotNull Instant getCreatedAt();

    @NotNull Instant getLastUpdatedAt();
}
