package de.greensurvivors;

import de.greensurvivors.exception.HttpRequestFailedException;
import de.greensurvivors.implementation.queryparam.filter.AFilterImpl;
import de.greensurvivors.reply.*;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    @AfterAll
    public static void close() throws Exception {
        session.close();
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
        assertEquals(hasAPIKey, (pasteReply.getUser() != null));
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
    public void deleteStringNoAPIKey() throws Exception {
        try (final Session sessionNoKey = Session.newSession()){
            Boolean success = sessionNoKey.createPaste(Paste.newBuilder(PasteContent.fromString(CONTENT)).
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
    public void postBytes() throws IOException { // from https://iconarchive.com
        try (final InputStream in = getClass().getResourceAsStream("/Cute-Cow-icon.png")) {
            assumeTrue(in != null);

            final PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromBytes(in.readAllBytes())).
                setTitle("cute_cow.png").
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).join();

            assertNotNull(pasteReply);

            final byte [] out = PasteContent.decodeBase64ToBytes(pasteReply.getContent());
            assertNotNull(out);

            assertEquals("iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAQTUlEQVRo3uVaaXRUZbbd57u3qpJUUkkqJCEkjEkgxIAkQJgcEGwbB2xwaByeot2+VhpaVNqpe6k82+nZLhWcZ5/6HLp5rxVwABVBcEBBJiHEkACGzCSpJFWpVNW9334/qipBtH1q8FdXVta669a3bp1pn7P3qQL+1V6G4ZirlLEOQOKxeqaIOAzDfEApo/hnNV6JUTIsNzc4rqiQgIw6Zg4oNbI4v4AD0tKalGHk/WwOmIbzvVt/d6leeMnFWhnGI//UUaVON5TxrlJGpoi6TERc3xsYQz265LeX6SXzL9SmMlf8LMaLSNHYUaP043+6mv946lFOLi3VyjDuFRH1rTIT4/XJhTl0mI7DM6eN10oZD3x3ORqJhmE8dVL5eP3Wc4/zqVuu00MH5UQA8fzgoP7QgySmjB6eB2jCd/gQfn/JBbD/y75+667do0S4hmQZIGUOQxVkJCe489Md8DgHZFww5xdMzRq0ePeeykX19fWNfn/31wB3A7JFibFgzswTx805fSa7Og5D2zbyB+eYB+sb8wFsO6YOAEhwmQqaNj/6bBs2bN0NO+znuDzPbBE1OyvFhRQn4DIMCACtbWa6DRyq2oNXX1oGoTY0VG5DU3Purl2VU7Zu3XVFsicZK/62mk+++gZmji/mgGQ3RIQiEPIHVsZ33EtXSp0mkHFaI4+gQ4QNIuKaWFJ01WFfh0yYOA5XXTqXNbu2ywcrXwc0CUDY96kUEUn1ZmD+kj+y7ITJQgJKKYIiEAMgqbUWTY31H27mjTffI0Mz0/nlvgNyoK7+Tm1zEKG9AAJKqQMktxN4H2TbdzpgKMMBwe0el1xdmGEmJScqOgARatqktASIysOaZ589S26+Zj6CnR189bFHpbWxCSSPjhqVUjJ64iQsvusuuhwiAAEoRuEkIONOK0QiFusONcqpZ1zEjtZmGTPQhNspJCHUGhEt7OyxZV+b3R0Iy/2a/A+tbeubDhjmI8dlORZkuYHmLhu2RvQzqUESCU6F1ETBIb9CxpAijEvX8PtaQcSs5rdAD1MJLrp2CWbMng1Cf1djgMBEVU0tps84ByOSw0hxhNHYaSNsR00TmwAIl0OQ7TFQHwD2NFmPWra1qA8DIg6nwm+0Danv0AiGNXw9oAVTDGUwHLEk0SRSg2TQ0tK1rwrDCtJpgEKJRVxE+gAfiy4EbY0NFDEFiIACCqNBi5+BEEOGZLOkpFgqtm6mU2np7NHo0QadpikRy0KCQXoJOdSuYRqKDoXLLRt/AEAzFj0xBCBt9FgCpycbLyyahRMKc5GggNZACM9u+AL/vXEHOjv8mFGQBlMIEoAmREnccAgkmlchIBp5BfkQ0YDIN1paX9o0XA4Dr73yKE6cfh4SwgHcdvY0nFVWBDcIXzCCt7+sxo0vvIV0FQK1jlksABnvQgx3W/qlrrD81mVqBAIBfNXQgsLsNKYmOrB66242tLSivbMbJxV4aYqO20CJlTfjdwQQIQRkRnYexp1wIjRtUhMQgfQGHzHcEyKCNE8SV7z6GGbN+jV37K2GAnFm6Uj6QkE0+jphCmiIhj+kENZ8BrGHSB9NUB5lGOuzElmaYgKdYTJoiWiCGpRgBJiU72V+ulO0Zm8JxCqHIhJFnQBKke6kZFl83zLkjy5kLCcxsMfPCXHEfaWUAOCDD78oN978F7hNRYk9P9EJehxKukIaTSFZR9pn2pbd8602qpTyCOQJtynzUl1AoglQBCGL0O4MPP6fN6D85Gm4a9EiHK6rB/sgjDgERBFutxsL774HxWOOP+LEdwA42o0Qz4KIwLKJKSefj/aaPXAYhAIQtIDWoNbdFh6h8AbaOvR9cwCi1CRQrjCFJ7pMGRHWcDzz9DJccN4vCVDWv/kmXnngQUYilmitwRiIRQjTobDwrvs5rrw0htdo1I/MQB8AKCSOzAZFlLy9diPOP+8KmgpWj6WrbOAdAE9qrSt/yCA7OlI5Bfn5tTu3rTVMZfeWg6+tjbu3bJGaigq0NzdTlEhmziAcVz4BJRPKo+XVh9QjjI5eixjUoICxAMQdUEosrVA64YzQ3r2VeZr6cH+pxFkXXTRXGYaiUMcjisTkFGyubOSll/4GA7yeKA5inSgOUKi+zoq+uwQEhMLeyhp+tmUH5l80G6SONgSQhtI4//wznX+546uzQDzfTxZqvPjJprcY7q5hJFClrUAVIz3VfOyRezSguPTWm2gFq7UVrGGk+0D8X/deBw/qSPAgI8Gjrrur9axZM6iUwf1Vn+hwdxWt2PPtYDU3bXhdixgv9pvMuVyu0rElowDaAHR89qClpRUA0eXvhGUTO3btwY4de1Gz/xAOHDyE9tY2hCN2jAW6kO5Nw+DBAzF0cB7Gji1CaelotLd1gCQ6uwLI5YC+MaE1xo0ZBdNQxRHL7p8DI0YMzXE6DQoIagFEUQiUlY0FAK5Zux5/+/tqFI0ezXFjj0PBwGxMHz0KaSkJdCc4AQrCEYttgRBqm9tQc6iFq97agD17diMQ6GZWViaGDx8WHwmIkSa4XA4MHpw3pGb//n45oJKcjmTbssU0FURAQMnLf1+Npbc/yPPOnSOXzJqOk4ry6PI1iPL7IDoCoJnogSBEgCpqlwiQrclsj3DyL2GlXc7NNXXy0rpPMHHKGbx96fVyzq9OJWiLALAtzVSP293fElIUbbQ2NSArLw+RcAi/X7wUX1fXYsUd16LI0QMV8kFqO2Lg7evpvbQiOqbjkx8CDQm0w9Xtw0kuYtrssdh76gRce/+TWLt2PR5edhtMA2isr4Nt2/0WNFZ3sCfQ2dqU4vF6seSme6Aa6vjGwjPg0K1AD3rneTz1iBn/Db4m0fdiwyr+ZpQEWhEc54hg1dWzueSFd/C7BX/mX++4Br6WZnT5A4F+g/hQXVO91ix6e/UabNuwkeuWXi6mHQYZpQ8iCqt3VnNjxdey6LRyDPUmQ9t2NA8S50tKAIGArGn3y/Prt2FUzgBeWD5K4hPNJZTll83iaXe+KCv/dw3Ky0azoaHlYL8d8PsDO+vrDhe9vup9XHby8XBSQx/FDtwJiVj+xkY8tGoTZo4bhdeXzINitGNFkRllrP/22BtYsXE7tCaeWHAu4lORjDFZEnMmFeOddZ8i3ZuMUNj6/BhoYnl3wydfnN/Y2IzBI0ZCExRInOOQJE4amYfpYwv4wa59qKhtAg0DtGIMFL3QQMXBJmhqTi3Ox3mTSwDqPmJN0NIauZlpbKmow4ebtoOw1/0UTXz0kbRRBUNrS4sLk2fmpnLK6CFS6E0Fo3mIykOAtqZsrm5Gfo4X2UkOasYkFSRWaoLm7h5WNfpkUn4uHNBktC/30qSD7Z1cu/ugfNoewYebtvgONTTmgQz0MwP0VdXULi87vuRPr378JYYNTANJjMxIO4LhEIYIphUOBKChtcYRAi1GiohsdyKyC9xAlJKgd6VE4uv2LtT5uvDaRztRUFqG+samZf+f8T8wA4CIJLiTkj477dTpJaG6A3LVaeOR4UliQVaGpCW4eiN4tKSMNaYjGKgcoSGEokQ6gj3Y39LOho4uWb76U2YeVyYrV66u6A50jyd18Jg4ENUKxvDklJQPJ0+akFu160ucWz4S08eMgCfRhezUZHiTk+BxuSAkGAMw+qhnb8QpAn84BF8ghKbOADp7QnhvexVWfLYX5VOnYe176+o6OjpO1lrX/NS90Pdsps0hAD+eOm1KrtPhYsWOHTKxIBvj83M5ZnC2uE1BUkICEpxOOoSiDAMGSMumhG2NMDR7LEtC4Qh27G/k5qo6+bymCeXTptDWWt58ay21bRVrrff+YLL5Y9lpZmZm9ZLrFoxY/tDTzM/Pl7S0NGitWVmxV8JdPuQNSEFmipspiU5xOUxAhKGwJYGeEFq6gqxv65KE1AwUFo2iy+WQUE8PPv98Cy+++Dz5clcF31n7wfGk3vVzOWCWlJQEv9i8yugK+LF4/kJ8vGUHWgJhjD5uDIYNGwbTNOFKcME0TQCCSCQCrW0opWBZNiwrgqqqfaj9+iAmTyrD3Dmn4+yzTkFKSjL+eNPdeOjhZ2eReu0xX+7GXtmZA7ymiEaKO4Fz554mPFwNUBgI1cnhnQcQjBAF5VMZjNgSsS3YWtPpdEqqx428QRkcMiRPFi/4NYqLCuhMcIlhRjcVIiI5A7NIMutHRfRHypvMVE9yr8CaPOMUrHzhedjBINwmkexwYNqZs3HhokWxrVsvihHVyyYcphGdaiSitIgAou001ZMCgAN/PgdEkpwuJxUVKDayc7J4x7PP4d0VK9BQU8WMnIGYt+BKJLkTIGLyiEKlQMc7VO8KKcanADHA6N4UIiol2sV+BgcEhG1Z0tHeBk9GGpWIDMrLwfxrFhNCIXUsOYpaRyQStuFwOdnW0iwDsrKja7wjdklxHquh2dnaKsFgkAD9xzwDSqkEAH82Tcf85uZmNNYeREp6WjTzIiA0JPZHUSAU6g7sR0Z2DhCx0NZ4GBmZ2eiVBzHN0DsrtEZTfT0a6uqQ7vXe3eHzXQDgNtu2V/fbARExlDLevOH6RadcMG825s27khSBZVkQ0+j7QiC2rQAEoZ4edPsDTB9gg7aGpsVgtx/upMSYvOnTAyKCiG2BVoT1hxrxyEN3qZH5w0qvuPL6N3bu2nOZbVsv9peNnn/BvHNmLL3lGlBH2O7rlEhEo6e7mw5PynesDIG9ldV0aC3NdbUQwyCpZfv2CkydUsr4Ei+uJwBBMBAmSdm+u5I33DxCiovysfJ/nsHosdPv8wfsVwBa/XHglHN+dSqVRBX31CkT+cXOSqR505HiSelVWn0bWwtvr1mPHG8qp00cAw2NQCDEh594DVOnjge1HV3Y9e6EBZ3tPrT7unm4tQPFRQUUJRiYMwDlE8sy163fVAxg5092gFDpEvZLW0sjtu+u4f4Dh2Ttex9j0riR9PlSJc2bjs4OP1OSk2ImEYZp8t7lz8nIwqHQWrOhoUWGDh8WLx/RBHwdfnrT08Xf5YO/vZXrP94ilm3zgeXPyqKFl6K7q42AFgBG/zAA/dXuiv0Idgdx/c33oiTbjXferca/z58L4gAsK4KnnvsHzjx9BsaOKQAhSPMkY9qUUpx7xnSQgn0H67Dhk20xewQbN23Btm0VuOySOWiqrYWmjVdWvIXjsxLwzEMP4bMtX+DmP1yIHbsq2iHYA/ZPDzy6/ImXF44YlpdWkp0Eb6KBrGQHbly6DMvuug4rX74Pa9Zswvur38aMsnwEAgFs3VaBxKFFGDEsFySxfU8l9n6+GbdceSWSU9Ow6sPtSAoHMNCMYMyUCXj6xdfR3tSE4wszkJWajnfWrEO33492X+edIEPHQA+oE0RkxeC0xKyMZCdCWmFPXRu8HjcKvQlwuwx8ur8NvxidBQERtBTWVTZGcgYNtBSB+sam7uLslIzhGYkADKzZ04CTR2bDMA3IwHysWrMOQ7xuDEhJRMiyUdPst8Na32PZ1q3/dD//Y8mciCQAmE6gEEBAIB+J4BRSZgqYS8IVHUtCAm0ieFhre2VsjgwH8VeIDCVpxL/UFEGAwD4IXgPgosYIEbaTXAfg63+JX8/8H3MbqPPdn6VgAAAAAElFTkSuQmCC",
                Base64.getEncoder().encodeToString(out));

        }
    }

    @Test
    public void postStream() throws IOException { // from https://iconarchive.com
        try (final InputStream in = getClass().getResourceAsStream("/Cute-Cow-icon.png")) {
            assumeTrue(in != null);

            final PasteReply pasteReply = session.createPaste(Paste.newBuilder(PasteContent.fromStream(in)).
                setTitle("cute_cow.png").
                setExpirationTime(Instant.now().plus(24, ChronoUnit.HOURS))).join();

            assertNotNull(pasteReply);

            try (final InputStream out = PasteContent.decodeBase64ToInputStream(pasteReply.getContent())) {
                assertNotNull(out);

                assertEquals("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",
                    Base64.getEncoder().encodeToString(out.readAllBytes()));
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
    public void createFolderWithoutAPIKey() throws Exception {
        try (final Session sessionNoKey = Session.newSession()){
            sessionNoKey.createFolder(Folder.newBuilder("public-test")).join();
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

    @Test
    public void publicPastesTest () {
        final Set<PasteReply> publicPastes = session.getPublicPastes().join();

        assertNotNull(publicPastes);
        assertFalse(publicPastes.isEmpty());

        final Optional<PasteReply> pasteWithUser = publicPastes.stream().filter(reply -> reply.getUser() != null).findAny();

        if (pasteWithUser.isPresent()) {
            final Set<PasteReply> publicFilteredPastes = session.getPublicPastes(Set.of(new AFilterImpl.UserIdFilterImpl(List.of("filter"), pasteWithUser.get().getUser().getId()))).join();

            assertNotNull(publicFilteredPastes);
            assertTrue(publicFilteredPastes.stream().allMatch(it -> it.getId().equals(pasteWithUser.get().getId())));

            if (publicPastes.size() <= publicFilteredPastes.size()) {
                System.out.println("Something may be wrong with filtering public pastes. Size before filtering: " + publicPastes.size() + ", size after: " + publicFilteredPastes.size());
            }
        }
    }
}
