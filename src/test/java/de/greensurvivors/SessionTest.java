package de.greensurvivors;

import de.greensurvivors.exception.HttpRequestFailedException;
import de.greensurvivors.reply.*;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SessionTest { // todo test ai, paste fork
    private static final String TITLE = "test-title";
    private static final String CONTENT = "This is an api test.";

    private static Session session;
    private static boolean hasAPIKey;

    @BeforeAll
    public static void Setup() {
        final String apiKey = System.getenv("PastefyAPIKey");
        session = Session.newSession(apiKey);
        hasAPIKey = apiKey != null;
    }

    @Test
    public void postString () {
        final Instant instantBefore = Instant.now();

         final PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
             setTitle(TITLE).
             setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).join();

        assertNotNull(pasteReply);
        assertEquals(TITLE, pasteReply.getTitle());
        assertEquals(CONTENT, pasteReply.getContent());
        assertEquals(Paste.PasteVisibility.UNLISTED, pasteReply.getVisibility());
        assertEquals(Paste.PasteType.PASTE, pasteReply.getType());
        assertFalse(pasteReply.isEncrypted());
        assertTrue(pasteReply.exists());
        assertEquals(hasAPIKey, (pasteReply.getUser() != null)); // todo better test
        assertNotNull(pasteReply.getRawURL());
        assertNotNull(pasteReply.getId());
        assertNotNull(pasteReply.getExpirationTime());

        // check if it took less than 5 minutes to post the paste.
        // this is more of a sanity check then one of function.
        assertTrue(pasteReply.getCreatedAt().getEpochSecond() - instantBefore.getEpochSecond() < 5 * 60);
    }

    @Test
    public void getString () {
        PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
                setTitle(TITLE).
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
            thenCompose(postResponse -> session.getPaste(postResponse.getId())).join();

        assertNotNull(pasteReply);
        assertEquals(TITLE, pasteReply.getTitle());
        assertEquals(CONTENT, pasteReply.getContent());
        assertEquals(Paste.PasteVisibility.UNLISTED, pasteReply.getVisibility());
        assertEquals(Paste.PasteType.PASTE, pasteReply.getType());
        assertFalse(pasteReply.isEncrypted());
        assertTrue(pasteReply.exists());
        assertEquals(hasAPIKey, (pasteReply.getUser() != null));
        assertNotNull(pasteReply.getRawURL());
        assertNotNull(pasteReply.getId());
        assertNotNull(pasteReply.getExpirationTime());
    }

    @Test
    public void editString() throws NoSuchAlgorithmException, InvalidCipherTextException {
        PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
            setTitle(TITLE).
            encryptWhenSending("v3Ry-5/\\f3_pAs5WÖrd".getBytes(StandardCharsets.UTF_8)).
            setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).join();

        assertTrue(pasteReply.getTags().isEmpty());

        final Boolean success = session.editPaste(pasteReply.getId(), pasteReply.toPasteBuilder().addTag("test")).join();

        assertNotNull(success);
        assertTrue(success);

        pasteReply = session.getPaste(pasteReply.getId()).join();

        assertNotNull(pasteReply);
        assertTrue(pasteReply.getTags().contains("test"));
        assertNotEquals(TITLE, pasteReply.getTitle());
        assertNotEquals(CONTENT, pasteReply.getContent());

        pasteReply.decrypt("v3Ry-5/\\f3_pAs5WÖrd".getBytes(StandardCharsets.UTF_8));

        assertEquals(TITLE, pasteReply.getTitle());
        assertEquals(CONTENT, pasteReply.getContent());
    }

    @Test
    public void getNonExistent () {
        try {
            final PasteReply pasteReply = session.getPaste("8DMWU9tV").join();

            assertNull(pasteReply); // should never happen!
        } catch (CompletionException e) {
            if (e.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                assertEquals(HttpURLConnection.HTTP_NOT_FOUND, httpRequestFailedException.getStatusCode());
                assertEquals("NotFoundException", httpRequestFailedException.getExceptionThrownName());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void deleteString () {
        assumeTrue(hasAPIKey); // the api needs to verify you are indeed the owner of this paste in order to delete it.

        Boolean success = session.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
                setTitle(TITLE).
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
            thenCompose(pasteReply -> session.deletePaste(pasteReply.getId())).join();

        assertTrue(success);
    }

    @Test
    public void deleteStringNoAPIKey() {
        try {
            Boolean success = Session.newSession().createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
                    setTitle(TITLE).
                    setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
                thenCompose(pasteReply -> session.deletePaste(pasteReply.getId())).join();

            assertFalse(success); // should never happen!
        } catch (CompletionException e) {
            if (e.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                if (hasAPIKey) {
                    assertEquals("PermissionsDeniedException", httpRequestFailedException.getExceptionThrownName());
                    assertEquals(HttpURLConnection.HTTP_FORBIDDEN, httpRequestFailedException.getStatusCode());
                } else {
                    assertEquals("AuthenticationException", httpRequestFailedException.getExceptionThrownName());
                    assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, httpRequestFailedException.getStatusCode());
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void starTest() throws InterruptedException {
        assumeTrue(hasAPIKey);

        final PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
            setTitle(TITLE).
            setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).join();

        assertNotNull(pasteReply);
        assertNotNull(pasteReply.getId());

        final Boolean starSuccess = session.starPaste(pasteReply.getId()).join();

        assertNotNull(starSuccess);
        assumeTrue(starSuccess);

        // note without this delay sometimes the web api doesn't include the latest starred pastes, ruining our tests.
        TimeUnit.SECONDS.sleep(2);

        session.getMyStarredPastes().join();
        final Set<PasteReply> setStaredPastes = session.getMyStarredPastes().join();

        assertNotNull(setStaredPastes);
        assertFalse(setStaredPastes.isEmpty());

        assertTrue(setStaredPastes.stream().anyMatch(it -> it.getId().equals(pasteReply.getId())));

        final Boolean unstarSuccess = session.unstarPaste(pasteReply.getId()).join();

        assertNotNull(unstarSuccess);
        assumeTrue(unstarSuccess);

        // note without this delay sometimes the web api still includes the latest starred pastes, ruining our tests.
        TimeUnit.SECONDS.sleep(2);

        final Set<PasteReply> unsetStaredPastes = session.getMyStarredPastes().join();

        assertNotNull(unsetStaredPastes);

        assumeTrue(unsetStaredPastes.stream().noneMatch(it -> it.getId().equals(pasteReply.getId())));
    }

    // creates a folder, puts a paste and another folder into it. delete folder. everything should get deleted
    @Test
    public void folderTest() throws InterruptedException {
        assumeTrue(hasAPIKey);

        final FolderReply createFolderReply = session.createFolder(Folder.newBuilder("test-folder")).join();

        assertNotNull(createFolderReply);
        assertEquals("test-folder", createFolderReply.getName());
        assertTrue(createFolderReply.getSubFolders().isEmpty());
        assertTrue(createFolderReply.getPastes().isEmpty());
        assertNotNull(createFolderReply.getId());
        assertNotNull(createFolderReply.getUserId());

        TimeUnit.SECONDS.sleep(1);

        final FolderReply getFolderReply1 = session.getFolder(createFolderReply.getId()).join();

        assertNotNull(getFolderReply1);
        assertEquals(createFolderReply.getName(), getFolderReply1.getName());
        assertTrue(getFolderReply1.getSubFolders().isEmpty());
        assertTrue(getFolderReply1.getPastes().isEmpty());
        assertEquals(createFolderReply.getId(), getFolderReply1.getId());
        assertEquals(createFolderReply.getUserId(), getFolderReply1.getUserId());

        final PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
            setTitle(TITLE).
            setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS)).
            setFolderId(createFolderReply.getId())).join();

        assertNotNull(pasteReply);
        assertEquals(createFolderReply.getUserId(), pasteReply.getUser().getId());
        assertEquals(createFolderReply.getId(), pasteReply.getFolderId());

        TimeUnit.SECONDS.sleep(1);

        final FolderReply getFolderReply2 = session.getFolder(createFolderReply.getId()).join();
        assertEquals(1, getFolderReply2.getPastes().size());
        assertEquals(pasteReply.getId(), getFolderReply2.getPastes().iterator().next().getId());

        final FolderReply subFolderReply = session.createFolder(Folder.newBuilder("subfolder").
            setParent(createFolderReply.getId())).join();

        assertNotNull(subFolderReply);

        final FolderReply getFolderReply3 = session.getFolder(createFolderReply.getId()).join();
        assertEquals(1, getFolderReply3.getSubFolders().size());
        assertEquals(subFolderReply.getId(), getFolderReply3.getSubFolders().iterator().next().getId());

        TimeUnit.SECONDS.sleep(1);

        final Boolean deleteSuccess = session.deleteFolder(createFolderReply.getId()).join();

        assertNotNull(deleteSuccess);
        assertTrue(deleteSuccess);

        TimeUnit.SECONDS.sleep(1);

        try {
            final FolderReply getFolderReply4 = session.getFolder(createFolderReply.getId()).join();

            assertNull(getFolderReply4); // should never happen!
        } catch (CompletionException e) {
            if (e.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                assertEquals("NotFoundException", httpRequestFailedException.getExceptionThrownName());
                assertEquals(404, httpRequestFailedException.getStatusCode());
            } else {
                throw new RuntimeException(e);
            }
        }

        try {
            final FolderReply subFolderReply2 = session.getFolder(subFolderReply.getId()).join();

            assertNull(subFolderReply2); // should never happen!
        } catch (CompletionException e) {
            if (e.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                assertEquals("NotFoundException", httpRequestFailedException.getExceptionThrownName());
                assertEquals(404, httpRequestFailedException.getStatusCode());
            } else {
                throw new RuntimeException(e);
            }
        }

        try {
            final PasteReply pasteReply2 = session.getPaste(pasteReply.getId()).join();

            assertNull(pasteReply2); // should never happen!
        } catch (CompletionException e) {
            if (e.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                assertEquals("NotFoundException", httpRequestFailedException.getExceptionThrownName());
                assertEquals(404, httpRequestFailedException.getStatusCode());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void createFolderWithoutAPIKey() {
        try {
            Session.newSession().createFolder(Folder.newBuilder("public-test")).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                assertEquals(401, httpRequestFailedException.getStatusCode());
                assertEquals("AuthenticationException", httpRequestFailedException.getExceptionThrownName());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void userInformationTest() throws MalformedURLException {
        assumeTrue(hasAPIKey);

        final PrivateUserReply privateUserReply = session.getMyAccountInfo().join();
        assertNotNull(privateUserReply);
        assertTrue(privateUserReply.isLoggedIn());
        assertNotNull(privateUserReply.getName());
        assertNotNull(privateUserReply.getAuthenticationProviderName());
        assertNotNull(privateUserReply.getStatus());

        if (privateUserReply.getStatus() != AccountStaus.VALID_USER) {
            System.out.println("Warning: your account is " + privateUserReply.getStatus() + " this might be a bug with this api. Please verify manually!");
        }

        assertNotNull(privateUserReply.getId());
        assertNotNull(privateUserReply.getName());
        assertNotNull(privateUserReply.getAvatarURL());
        assertNotNull(privateUserReply.getDisplayName());

        final PublicUserReply publicUserReply = session.getPublicUserInformation(privateUserReply.getName()).join();

        assertEquals(privateUserReply.getId(), publicUserReply.getId());
        assertEquals(privateUserReply.getName(), publicUserReply.getName());
        assertEquals(privateUserReply.getAvatarURL(), publicUserReply.getAvatarURL());
        assertEquals(privateUserReply.getDisplayName(), publicUserReply.getDisplayName());
    }

    @Test
    public void apiKeyTest() throws InterruptedException {
        assumeTrue(hasAPIKey);

        final String newAPIKey = session.createNewAPIKey().join();

        assertNotNull(newAPIKey);

        TimeUnit.SECONDS.sleep(1);

        final Set<String> myAPIKeys = session.getMyAPIKeys().join();

        assertNotNull(myAPIKeys);
        assertTrue(myAPIKeys.contains(newAPIKey));
        assertTrue(myAPIKeys.contains(System.getenv("PastefyAPIKey")));

        final Boolean deleteSuccess1 = session.deleteAPIKey(newAPIKey).join();

        assertNotNull(deleteSuccess1);
        assumeTrue(deleteSuccess1);

        final Boolean deleteSuccess2 = session.deleteAPIKey("invalidAPIKey99").join();

        assertNotNull(deleteSuccess2);
        assumeTrue(deleteSuccess2); // Note: The api always returns true. No matter if the api could get deleted - or not (because it doesn't exist)
    }

    @Test
    public void notificationsTest() throws MalformedURLException {
        assertTrue(hasAPIKey);

        final List<NotificationReply> notificationReplies = session.getNotifications().join();

        assertNotNull(notificationReplies);
        assertTrue(notificationReplies.stream().noneMatch(Objects::isNull));

        if (!notificationReplies.isEmpty()) {
            NotificationReply notificationReply = notificationReplies.getFirst();

            assertNotNull(notificationReply.getMessage());
            assertNotNull(notificationReply.getUrl());
            assertNotNull(notificationReply.getCreatedAt());
        }

        // commented out because I don't want to disturb your notifications if this isn't just a test account.
        // session.markAllNotificationsRead().join();
    }

    @Test
    public void plattformInfoTest() {
        final PlatformInfoReply platformInfoReply = session.getPlatformInfo().join();

        assertNotNull(platformInfoReply);
        System.out.println("Platform info: " + platformInfoReply);
    }

    @Test
    public void tagTest() throws MalformedURLException {
        final TagReply tagReply = session.getTag("script").join();

        assertNotNull(tagReply);
        assertEquals("script", tagReply.getTag());
        assertEquals("A script is a written document that contains the dialogue, stage directions, and other information needed to produce a play, film, or television program.", tagReply.getDescription());
        assertEquals(URI.create("https://storage.interaapps.de/interaapps/pastefy-script.png").toURL(), tagReply.getImageUrl());
        assertEquals("script", tagReply.getIcon());
        assertNull(tagReply.getWebsite());
        assertTrue(tagReply.getPasteCount() >= 100000);

        final Set<TagReply> tagReplies = session.getAllTags().join();

        assertNotNull(tagReplies);
        assertFalse(tagReplies.isEmpty());
        assertTrue(tagReplies.contains(tagReply));
    }

    @Test
    public void statsTest() {
        assumeTrue(hasAPIKey);

        final StatsReply statsReply = session.getPlatformStats().join();

        assertNotNull(statsReply);
        assertTrue(statsReply.getCreatedPatesCount() > 1);
        assertTrue(statsReply.getPastesWithOwnersCount() > 1);
        assertTrue(statsReply.getIndexedPastesCount() > 1);
        assertTrue(statsReply.getUserCount() > 1);
        assertTrue(statsReply.getTagCount() > 1);
        assertTrue(statsReply.getFolderCount() > 1);
        assertTrue(statsReply.getS3pasteCount() > 1);
    }
}
