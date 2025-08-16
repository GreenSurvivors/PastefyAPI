package de.greensurvivors.implementation;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptionHelperTest {
    private static final String PASSWORD = "S4f³ pÄ?5w°r|)";
    private static final String CONTENT = "encrypted";

    @Test
    public void checkCrypto() throws NoSuchAlgorithmException, InvalidCipherTextException {
        final String encryptedContent = EncryptionHelper.encrypt(CONTENT,
            EncryptionHelper.hashPasskey(PASSWORD.getBytes(StandardCharsets.UTF_8)));

        // ---------------------------- decrypting ----------------------------

        assertEquals(CONTENT, EncryptionHelper.decrypt(encryptedContent, PASSWORD.getBytes(StandardCharsets.UTF_8)));
    }
}
