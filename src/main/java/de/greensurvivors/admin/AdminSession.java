package de.greensurvivors.admin;

import de.greensurvivors.Session;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AdminSession extends Session {
    @NotNull CompletableFuture<@NotNull Set<@NotNull AdminUserReply>> getUsers();
    @NotNull CompletableFuture<@NotNull AdminUserReply> getUser(final @NotNull String userId);
    @NotNull CompletableFuture<@NotNull Boolean> deleteUser(final @NotNull String userId);
    @NotNull CompletableFuture<@NotNull Boolean> editUser(final @NotNull String userId, final @NotNull UserEditBuilder userEditBuilder);
}
