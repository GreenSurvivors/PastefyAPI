package de.greensurvivors.admin;

import de.greensurvivors.Session;
import de.greensurvivors.queryparam.QueryParameter;
import org.jetbrains.annotations.NotNull;

import java.util.SequencedSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AdminSession extends Session {
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull AdminUserReply>> getUsers();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull AdminUserReply>> getUsers(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull AdminUserReply> getUser(final @NotNull String userId);

    @NotNull CompletableFuture<@NotNull Boolean> deleteUser(final @NotNull String userId);

    @NotNull CompletableFuture<@NotNull Boolean> editUser(final @NotNull String userId, final @NotNull UserEditBuilder userEditBuilder);
}
