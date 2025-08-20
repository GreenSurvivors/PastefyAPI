package de.greensurvivors.reply;

import de.greensurvivors.AccountStaus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PrivateUserReply extends PublicUserReply {
    boolean isLoggedIn(); // always true if the user is valid
    @Nullable String getFavoriteColor(); // always #f52966
    /// interaapps, google, github,  twitch, oidc aka custom, discord
    @NotNull String AuthenticationProviderName ();
    //@NotNull Set<@NotNull String> getAvailableProviderNamess();
    @NotNull AccountStaus getStatus();

}
