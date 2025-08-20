package de.greensurvivors.admin;

import de.greensurvivors.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AdminSession extends Session {
    @NotNull CompletableFuture<@Nullable Set<@NotNull AdminUserReply>> getUsers();
    @NotNull CompletableFuture<@Nullable AdminUserReply> getUser(final @NotNull String userId);
    @NotNull CompletableFuture<@NotNull Boolean> deleteUser(final @NotNull String userId);
    @NotNull CompletableFuture<@NotNull Boolean> editUser(final @NotNull String userId, final @NotNull UserEditBuilder userEditBuilder);
}
