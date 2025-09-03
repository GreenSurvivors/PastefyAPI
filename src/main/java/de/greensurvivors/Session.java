package de.greensurvivors;

import de.greensurvivors.implementation.AdminSessionImpl;
import de.greensurvivors.queryparam.QueryParameter;
import de.greensurvivors.reply.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.SequencedSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Session extends AutoCloseable {

    static Session newSession() {
        return new AdminSessionImpl();
    }

    /// Note: there isn't much to keep your api safe, beyond just using TLS (https).
    /// Please change your API on a regular basis!
    static Session newSession(final @Nullable String apiKey) {
        return new AdminSessionImpl(apiKey);
    }

    /// Note: there isn't much to keep your api safe, beyond just using TLS (https).
    /// Please change your API on a regular basis!
    static Session newSession(final @NotNull String serverAddress, final @Nullable String apiKey) {
        return new AdminSessionImpl(serverAddress, apiKey);
    }


    <T> @NotNull CompletableFuture<@NotNull PasteReply> createPaste(final @NotNull PasteBuilder<T> builder);

    @NotNull CompletableFuture<@NotNull PasteReply> getPaste(final @NotNull String pasteID);

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getPastes();

    /// needs an api key, if webservice is configured so - and pastify.app is.
    /// non-admin users have access to all public pastes, own pastes or all pastes starred by the user
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getPastes(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull <T> CompletableFuture<@NotNull Boolean> editPaste(final @NotNull String pasteID, final @NotNull PasteBuilder<T> builder);

    /// Note: needs an api key of the user who has created this paste (or is admin)
    @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull Boolean> starPaste(final @NotNull String pasteID);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull Boolean> unstarPaste(final @NotNull String pasteID);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getMyStarredPastes();

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getMyStarredPastes(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getMySharedPastes();

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getMySharedPastes(final @NotNull Set<@NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getPublicPastes();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getPublicPastes(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getTrendingPastes();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getTrendingPastes(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getLatestPastes();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull PasteReply>> getLatestPastes(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    // not implemented by the web api.
    //@NotNull CompletableFuture<@NotNull Boolean> addFriend(final @NotNull String pasteID, final @NotNull String friendID);

    @NotNull CompletableFuture<@NotNull FolderReply> createFolder(final @NotNull FolderBuilder builder);

    @NotNull CompletableFuture<@NotNull FolderReply> getFolder(final @NotNull String folderId);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull FolderReply> getFolder(final @NotNull String folderId, final @NotNull Set<? extends @NotNull QueryParameter<?>> queryParameters);

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull FolderReply>> getFolders();

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull FolderReply>> getFolders(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    /// Note: needs an api of the user who has created this folder (or is admin)
    /// Also deletes all sub folders and contained pastes recursively.
    @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID);

    @NotNull CompletableFuture<@NotNull PublicUserReply> getPublicUserInformation(final @NotNull String userName);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull PrivateUserReply> getMyAccountInfo();

    @NotNull CompletableFuture<@NotNull String> createNewAPIKey();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull String>> getMyAPIKeys();

    @NotNull CompletableFuture<@NotNull Boolean> deleteAPIKey(final @NotNull String keyToDelete);

    @NotNull CompletableFuture<@NotNull List<@NotNull NotificationReply>> getNotifications();

    @NotNull CompletableFuture<@NotNull Boolean> markAllNotificationsRead();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull TagReply>> getAllTags();

    @NotNull CompletableFuture<@NotNull SequencedSet<@NotNull TagReply>> getAllTags(final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters);

    @NotNull CompletableFuture<@NotNull TagReply> getTag(final @NotNull String tag);

    @NotNull CompletableFuture<@NotNull PlatformInfoReply> getPlatformInfo();

    @NotNull CompletableFuture<@NotNull StatsReply> getPlatformStats();
}
