package de.greensurvivors;

import de.greensurvivors.implementation.SessionImpl;
import de.greensurvivors.reply.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Session extends AutoCloseable {

    static Session newSession() {
        return new SessionImpl();
    }

    /// Note: there isn't much to keep your api safe, beyond just using TLS (https).
    /// Please change your API on a regular basis!
    static Session newSession(final @Nullable String apiKey) {
        return new SessionImpl(apiKey);
    }

    /// Note: there isn't much to keep your api safe, beyond just using TLS (https).
    /// Please change your API on a regular basis!
    static Session newSession (final @NotNull String serverAddress, final @Nullable String apiKey) {
        return new SessionImpl(serverAddress, apiKey);
    }


    <T> @NotNull CompletableFuture<@NotNull PasteReply> createPaste(final @NotNull PasteBuilder<T> builder);

    @NotNull CompletableFuture<@NotNull PasteReply> getPaste(final @NotNull String pasteID);

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull @Unmodifiable Set<@NotNull PasteReply>> getPastes(); // todo optional parameters -> filters (complex! map String -> ??) <-either not both. filters over filter.> filter (complex! map String -> ?? ) visibility -> Paste.Visibility; encrypted -> bool; createdAt -> ???, userId -> String; starredBy -> String (also userid); encrypted -> bool

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull @Unmodifiable Set<@NotNull PasteReply>> getPastes(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull <T> CompletableFuture<@NotNull Boolean> editPaste(final @NotNull String pasteID, final @NotNull PasteBuilder<T> builder);

    /// Note: needs an api key of the user who has created this paste (or is admin)
    @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull Boolean> starPaste(final @NotNull String pasteID);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull Boolean> unstarPaste(final @NotNull String pasteID);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getMyStarredPastes();

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getMyStarredPastes(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getMySharedPastes();

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getMySharedPastes(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getPublicPastes();

    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getPublicPastes(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getTrendingPastes();

    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getTrendingPastes(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getLatestPastes();

    @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getLatestPastes(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    // not implemented by the web api.
    //@NotNull CompletableFuture<@NotNull Boolean> addFriend(final @NotNull String pasteID, final @NotNull String friendID);

    @NotNull CompletableFuture<@NotNull FolderReply> createFolder (final @NotNull FolderBuilder builder);

    // todo check if you can get public folders.
    @NotNull CompletableFuture<@NotNull FolderReply> getFolder(final @NotNull String folderId);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull FolderReply> getFolder(final @NotNull String folderId, final @NotNull Set<? extends @NotNull QueryParameter<?>> queryParameters);

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull @Unmodifiable Set<@NotNull FolderReply>> getFolders();

    // todo can I overwrite the user via formdata filter?
    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull @Unmodifiable Set<@NotNull FolderReply>> getFolders(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters); // todo same as getPastes;; BUT going the userController way may would allow to hide_children, hide_sub_children and hide_pastes get set.

    /// Note: needs an api of the user who has created this folder (or is admin)
    /// Also deletes all sub folders and contained pastes recursively.
    @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID);

    @NotNull CompletableFuture<@NotNull PublicUserReply> getPublicUserInformation(final @NotNull String userName);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull PrivateUserReply> getMyAccountInfo();

    @NotNull CompletableFuture<@NotNull String> createNewAPIKey();

    @NotNull CompletableFuture<@NotNull Set<@NotNull String>> getMyAPIKeys();

    @NotNull CompletableFuture<@NotNull Boolean> deleteAPIKey(final @NotNull String keyToDelete);

    @NotNull CompletableFuture<@NotNull List<@NotNull NotificationReply>> getNotifications();

    @NotNull CompletableFuture<@NotNull Boolean> markAllNotificationsRead();

    @NotNull CompletableFuture<@NotNull Set<@NotNull TagReply>> getAllTags();

    @NotNull CompletableFuture<@NotNull Set<@NotNull TagReply>> getAllTags(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull TagReply> getTag(final @NotNull String tag);

    @NotNull CompletableFuture<@NotNull PlatformInfoReply> getPlatformInfo();

    @NotNull CompletableFuture<@NotNull StatsReply> getPlatformStats();
}
