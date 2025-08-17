package de.greensurvivors.implementation;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteReply;
import de.greensurvivors.exception.HttpRequestFailedException;
import de.greensurvivors.implementation.content.SimpleStringContentWrapper;
import org.bouncycastle.crypto.CryptoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SessionImplTest { // todo move down to api level
    private static final String TITLE = "test-title";
    private static final String CONTENT = "This is an api test.";

    private static SessionImpl session;
    private static boolean hasAPIKey;

    @BeforeAll
    public static void Setup() {
        final String apiKey = System.getenv("PastefyAPIKey");
        session = new SessionImpl(apiKey);
        hasAPIKey = apiKey != null;
    }

    @Test
    public void postString () throws IOException, CryptoException {
         PasteReply pasteReply = session.createPaste(Paste.newBuilder(TITLE, new SimpleStringContentWrapper(CONTENT)).
                 setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).join();

        assertNotNull(pasteReply);
        assertEquals(TITLE, pasteReply.getTitle());
        assertEquals(CONTENT, pasteReply.getContent());
        assertEquals(Paste.PasteVisibility.UNLISTED, pasteReply.getVisibility());
        assertEquals(Paste.PasteType.PASTE, pasteReply.getType());
        assertFalse(pasteReply.isEncrypted());
        assertTrue(pasteReply.exists());
        assertEquals(hasAPIKey, (pasteReply.getUserId() != null));
        assertNotNull(pasteReply.getRawURL());
        assertNotNull(pasteReply.getId());
        assertNotNull(pasteReply.getExpirationTime());
    }

    @Test
    public void getString () throws IOException, CryptoException {
        PasteReply pasteReply = session.createPaste(Paste.newBuilder(TITLE, new SimpleStringContentWrapper(CONTENT)).
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
            thenCompose(postResponse -> session.getPaste(postResponse.getId())).join();

        assertNotNull(pasteReply);
        assertEquals(TITLE, pasteReply.getTitle());
        assertEquals(CONTENT, pasteReply.getContent());
        assertEquals(Paste.PasteVisibility.UNLISTED, pasteReply.getVisibility());
        assertEquals(Paste.PasteType.PASTE, pasteReply.getType());
        assertFalse(pasteReply.isEncrypted());
        assertTrue(pasteReply.exists());
        assertEquals(hasAPIKey, (pasteReply.getUserId() != null));
        assertNotNull(pasteReply.getRawURL());
        assertNotNull(pasteReply.getId());
        assertNotNull(pasteReply.getExpirationTime());
    }

    @Test
    public void deleteString () throws IOException, CryptoException {
        assumeTrue(hasAPIKey); // the api needs to verify you are indeed the owner of this paste in order to delete it.

        Boolean success = session.createPaste(Paste.newBuilder(TITLE, new SimpleStringContentWrapper(CONTENT)).
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
            thenCompose(pasteReply -> session.deletePaste(pasteReply.getId())).join();

        assertTrue(success);
    }
}
