package de.greensurvivors;

import de.greensurvivors.implementation.SessionImpl;
import de.greensurvivors.reply.*;
import org.bouncycastle.crypto.CryptoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Session {

    static Session newSession() {
        return new SessionImpl();
    }

    static Session newSession(final @Nullable String apiKey) {
        return new SessionImpl(apiKey);
    }

    static Session newSession (final @NotNull String serverAddress, final @Nullable String apiKey) {
        return new SessionImpl(serverAddress, apiKey);
    }


    <T> @NotNull CompletableFuture<@Nullable PasteReply> createPaste(final @NotNull PasteBuilder<T> builder) throws IOException, CryptoException;

    @NotNull CompletableFuture<@Nullable PasteReply> getPaste(final @NotNull String pasteID);

    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@Nullable @Unmodifiable Set<@NotNull PasteReply>> getPastes(); // todo optional parameters -> filter_tags(list as in: =foo,bar,buzz,...), shorten_content (bool), page (int), page_limit (int), search(string), sort(string), filters (complex! map String -> ??) <-either not both. filters over filter.> filter (complex! map String -> ?? ) visibility -> Paste.Visibility; encrypted -> bool; createdAt -> ???, userId -> String; starredBy -> String (also userid)

    <T> @NotNull CompletableFuture<@Nullable PasteReply> editPaste(final @NotNull PasteBuilder<T> builder) throws IOException, CryptoException;

    /// Note: needs an api of the user who has created this paste (or is admin)
    @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull Boolean> starPaste(final @NotNull String pasteID);

    /// needs an api key.
    @NotNull CompletableFuture<@NotNull Boolean> unstarPaste(final @NotNull String pasteID);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getMyStarredPastes();

    @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getPublicPastes();

    @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getTrendingPastes();

    @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getLatestPastes();

    // not implemented.
    //@NotNull CompletableFuture<@NotNull Boolean> addFriend(final @NotNull String pasteID, final @NotNull String friendID);

    // todo check if you can create public folders.
    @NotNull CompletableFuture<@Nullable FolderReply> createFolder (final @NotNull FolderBuilder builder);

    // todo check if you can get public folders.
    @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId);

    /// needs an api key.
    @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId, final boolean hideSubFolder);

    // todo can I overwrite the user via formdata filter?
    /// needs an api key, if webservice is configured so - and pastify.app is.
    @NotNull CompletableFuture<@Nullable @Unmodifiable Set<@NotNull FolderReply>> getFolders(); // todo same as getPastes;; BUT going the userController way may would allow to hide_children, hide_sub_children and hide_pastes get set.

    /// Note: needs an api of the user who has created this folder (or is admin)
    /// Also deletes all sub folders and contained pastes recursively.
    @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID);

    @NotNull CompletableFuture<@Nullable PublicUserReply> getPublicUserInformation(final @NotNull String userName);

    /// needs an api key. obviously.
    @NotNull CompletableFuture<@NotNull PrivateUserReply> getMyAccountInfo();

    @NotNull CompletableFuture<@Nullable String> createNewAPIKey();

    @NotNull CompletableFuture<@Nullable Set<@NotNull String>> getMyAPIKeys();

    @NotNull CompletableFuture<@NotNull Boolean> deleteAPIKey(final @NotNull String keyToDelete);

    @NotNull CompletableFuture<@Nullable List<@NotNull NotificationReply>> getNotifications();

    @NotNull CompletableFuture<@NotNull Boolean> markAllNotificationsRead();

    @NotNull CompletableFuture<@Nullable Set<TagReply>> getAllTags();

    @NotNull CompletableFuture<@Nullable TagReply> getTag(final @NotNull String tag);

    @NotNull CompletableFuture<@Nullable PlatformInfoReply> getPlatformInfo();
}
