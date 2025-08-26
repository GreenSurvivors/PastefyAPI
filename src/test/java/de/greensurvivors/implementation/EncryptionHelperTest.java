package de.greensurvivors.implementation;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionHelperTest {
    private static final String PASSWORD = "S4f³ pÄ?5w°r|)"; // note: NEVER use Strings for real passwords, if you can avoid it in any matter!
    private static final String CONTENT = "encrypted";

    @Test
    public void checkCrypto() throws NoSuchAlgorithmException, InvalidCipherTextException {
        final EncryptionHelper.HashedPasskey hashedPasskey = EncryptionHelper.hashPasskey(PASSWORD.getBytes(StandardCharsets.UTF_8));

        assertNotEquals(PASSWORD.getBytes(StandardCharsets.UTF_8), hashedPasskey.hash());
        assertTrue(16 <= hashedPasskey.salt().length);

        final String encryptedContent = EncryptionHelper.encrypt(CONTENT, hashedPasskey);

        // ---------------------------- decrypting ----------------------------

        assertEquals(CONTENT, EncryptionHelper.decrypt(encryptedContent, PASSWORD.getBytes(StandardCharsets.UTF_8)));
    }
}
