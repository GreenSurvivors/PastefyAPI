package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SessionImplTest {
    static SessionImpl session;
    static Gson gson;

    @BeforeAll
    public static void Setup() {
        session = new SessionImpl();
        gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new InstantAdapter()).
            create();
    }

    @Test
    public void postString () throws IOException, CryptoException {
         PasteReply pasteReply = session.createPaste(Paste.newBuilder("test-title", new SimpleStringContentWrapper("This is an api test.")).
                 setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
            whenComplete( (response, throwable) -> {
                if (throwable != null) {
                    if (throwable instanceof HttpRequestFailedException httpRequestFailedException) {
                        System.out.println("status code: " + httpRequestFailedException.getStatusCode());
                    }

                    System.out.println(Arrays.toString(throwable.getStackTrace()));
                }
            }).join();

        assertNotNull(pasteReply);
        assertEquals("test-title", pasteReply.getTitle());
        assertEquals("This is an api test.", pasteReply.getContent());
        assertEquals(Paste.PasteVisibility.UNLISTED, pasteReply.getVisibility());
        assertEquals(Paste.PasteType.PASTE, pasteReply.getType());
        assertFalse(pasteReply.isEncrypted());
        assertTrue(pasteReply.exists());
        assertNull(pasteReply.getUserId());
        assertNotNull(pasteReply.getRawURL());
        assertNotNull(pasteReply.getId());
        assertNotNull(pasteReply.getExpirationTime());
    }

    @Test
    public void getString () throws IOException, CryptoException {

        PasteReply pasteReply = session.createPaste(Paste.newBuilder("test-title", new SimpleStringContentWrapper("This is an api test.")).
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).
            whenComplete((response, throwable) -> {
                if (throwable != null) {
                    if (throwable.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                        System.out.println("status code: " + httpRequestFailedException.getStatusCode());
                    }
                }
            }).thenCompose(postResponse -> session.getPaste(postResponse.getId()).
                whenComplete((getResponse, throwable) -> {
                    if (throwable != null) {
                        if (throwable.getCause() instanceof HttpRequestFailedException httpRequestFailedException) {
                            System.out.println("status code: " + httpRequestFailedException.getStatusCode());
                        }
                    }
                })
            ).join();

        assertNotNull(pasteReply);
        assertEquals("test-title", pasteReply.getTitle());
        assertEquals("This is an api test.", pasteReply.getContent());
        assertEquals(Paste.PasteVisibility.UNLISTED, pasteReply.getVisibility());
        assertEquals(Paste.PasteType.PASTE, pasteReply.getType());
        assertFalse(pasteReply.isEncrypted());
        assertTrue(pasteReply.exists());
        assertNull(pasteReply.getUserId());
        assertNotNull(pasteReply.getRawURL());
        assertNotNull(pasteReply.getId());
        assertNotNull(pasteReply.getExpirationTime());
    }
}
