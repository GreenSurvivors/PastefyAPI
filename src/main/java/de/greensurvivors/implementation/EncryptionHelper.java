package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.modes.AEADCipher;
import org.bouncycastle.crypto.modes.GCMSIVBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

// Note: this class is cluttered with links about encryption I have referenced while writing it, and learning about encrypting.
// Since it's easy to trip and
// https://medium.com/@johnvazna/implementing-local-aes-gcm-encryption-and-decryption-in-java-ac1dacaaa409
public final class EncryptionHelper {
    private static final int AES_IV_LENGTH = 12;
    private static final int AES_TAG_LENGTH = 16;
    private static final int ARGON2_ITERATIONS = 10;
    private static final int ARGON2_MEMORY_KB = 66536;
    private static final int ARGON2_PARALLELISM = 1;
    private static final int ARGON2_KEY_SIZE = 32;

    private static final @NotNull Base64.Encoder b64encoder = Base64.getEncoder().withoutPadding();
    private static final @NotNull Base64.Decoder b64decoder = Base64.getDecoder();
    private static final Gson gson = new Gson();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // private constructor making this class even less extendable and doesn't allow any instances of it.
    private EncryptionHelper () {}

    /// clears passkey array.
    public static @NotNull HashedPasskey hashPasskey(final byte @NotNull [] passkey) throws NoSuchAlgorithmException {
        // create salt
        final byte[] salt = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(salt);

        final byte[] hash = hashPasskeyRaw(passkey,
            Argon2Parameters.ARGON2_id, Argon2Parameters.ARGON2_VERSION_13,
            ARGON2_ITERATIONS, ARGON2_MEMORY_KB, ARGON2_PARALLELISM, ARGON2_KEY_SIZE,
            salt);

        clearArray(passkey);

        return new HashedPasskey(hash,
            Argon2Parameters.ARGON2_id, Argon2Parameters.ARGON2_VERSION_13,
            ARGON2_ITERATIONS, ARGON2_MEMORY_KB, ARGON2_PARALLELISM, ARGON2_KEY_SIZE,
            salt);
    }

    private static byte @NotNull [] hashPasskeyRaw (final byte @NotNull [] passkey,
                                            final int pwAlgoType, final int pwAlgoVersion,
                                            final int pwAlgoIterations, final int pwAlgoMemory, final int pwAlgoParallelism,
                                            final int keySize,
                                            final byte @NotNull [] salt) {
        // prepare password hashing via argon2-id
        // https://ssojet.com/compare-hashing-algorithms/sha-256-vs-argon2/
        final Argon2Parameters argon2Parameters = new Argon2Parameters.Builder(pwAlgoType)
            .withVersion(pwAlgoVersion)
            .withIterations(pwAlgoIterations)
            .withMemoryAsKB(pwAlgoMemory)
            .withParallelism(pwAlgoParallelism)
            .withSalt(salt)
            .build();

        final Argon2BytesGenerator argon2 = new Argon2BytesGenerator();
        argon2.init(argon2Parameters);

        // now create the hash.
        // https://en.wikipedia.org/wiki/PBKDF2
        final byte[] hashKey = new byte[keySize];
        argon2.generateBytes(passkey, hashKey, 0, hashKey.length);

        return hashKey;
    }

    public static @NotNull String encrypt (final @NotNull String content, final @NotNull HashedPasskey hashedPasskey) throws NoSuchAlgorithmException, InvalidCipherTextException {
        // get bytes as fast as possible bytes
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        final String encrypted = encrypt(bytes, hashedPasskey);
        clearArray(bytes);

        return encrypted;
    }

    public static @NotNull String encrypt (final byte @NotNull [] content, final @NotNull HashedPasskey hashedPasskey) throws NoSuchAlgorithmException, InvalidCipherTextException {
        final KeyParameter encodeKeyParameter = new KeyParameter(hashedPasskey.hash());

        // generate a random nonce
        // https://stackoverflow.com/questions/73557246/in-java-how-to-use-aes-encryption-so-that-a-static-string-text-will-result-in-si
        // https://security.stackexchange.com/questions/278045/how-can-i-ensure-nonrepeating-iv-with-aes-gcm-encryption
        // https://crypto.stackexchange.com/questions/41601/aes-gcm-recommended-iv-size-why-12-bytes
        final byte[] nonce = new byte[AES_IV_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(nonce);

        // prepare cipher
        // https://stackoverflow.com/questions/36760973/why-is-random-iv-fine-for-aes-cbc-but-not-for-aes-gcm
        // https://cryptobook.nakov.com/symmetric-key-ciphers/popular-symmetric-algorithms
        // https://en.wikipedia.org/wiki/Padding_oracle_attack
        final AEADCipher cipher = new GCMSIVBlockCipher();
        cipher.init(true, new AEADParameters(encodeKeyParameter, AES_TAG_LENGTH * 8, nonce));

        // prepare output
        final int encryptOutputSize = cipher.getOutputSize(content.length);
        final byte[] encryptedData = new byte[encryptOutputSize];
        final int alreadyEncryptedBytes = cipher.processBytes(content, 0, content.length, encryptedData, 0); // should result in 0

        // start encrypting
        cipher.doFinal(encryptedData, alreadyEncryptedBytes);

        // link all metadata required to decode this to a string
        final Map<String, Object> encryptOutMap = new LinkedHashMap<>();
        encryptOutMap.put("dataVersion", "1.0.0");
        encryptOutMap.put("pwAlgoType", hashedPasskey.pwAlgoType());
        encryptOutMap.put("pwAlgoVersion", hashedPasskey.pwAlgoVersion());
        encryptOutMap.put("pwAlgoMemory", hashedPasskey.pwAlgoMemory());
        encryptOutMap.put("pwAlgoIterations", hashedPasskey.pwAlgoIterations());
        encryptOutMap.put("pwAlgoParallelism", hashedPasskey.pwAlgoParallelism());
        encryptOutMap.put("pwAlgoSalt", b64encoder.encodeToString(hashedPasskey.salt()));
        encryptOutMap.put("cipherIV", b64encoder.encodeToString(nonce));
        encryptOutMap.put("cipherMacSize", AES_TAG_LENGTH);
        encryptOutMap.put("keySize", ARGON2_KEY_SIZE);
        encryptOutMap.put("data", b64encoder.encodeToString(encryptedData));

        return gson.toJson(encryptOutMap);
    }

    /// does NOT clear passkey array. Please do it yourself via EncryptionHelper.clearArray(passkey);
    public static @NotNull String decrypt (final @NotNull String encryptedData, final byte @NotNull [] passkey) throws InvalidCipherTextException {
        Map<String, Object> decodeInMap = gson.fromJson(encryptedData, TypeToken.getParameterized(LinkedHashMap.class, String.class, Object.class).getType());

        final byte[] decodeKey = EncryptionHelper.hashPasskeyRaw(
            passkey,
            ((Number) decodeInMap.get("pwAlgoType")).intValue(),
            ((Number) decodeInMap.get("pwAlgoVersion")).intValue(),
            ((Number) decodeInMap.get("pwAlgoIterations")).intValue(),
            ((Number) decodeInMap.get("pwAlgoMemory")).intValue(),
            ((Number) decodeInMap.get("pwAlgoParallelism")).intValue(),
            ((Number) decodeInMap.get("keySize")).intValue(),
            b64decoder.decode((String) decodeInMap.get("pwAlgoSalt"))
        );
        final KeyParameter decodekeyParameter = new KeyParameter(decodeKey);

        final byte[] decodeCipherTextData = b64decoder.decode((String)decodeInMap.get("data"));

        final byte[] decodeNonce = b64decoder.decode((String)decodeInMap.get("cipherIV"));

        final AEADCipher cipher = new GCMSIVBlockCipher();
        cipher.init(false, new AEADParameters(decodekeyParameter, ((Number) decodeInMap.get("cipherMacSize")).intValue() * 8, decodeNonce));
        final int decryptOutputSize = cipher.getOutputSize(decodeCipherTextData.length);
        final byte[] decryptOutputData = new byte[decryptOutputSize];
        final int alreadyDecryptedBytes = cipher.processBytes(decodeCipherTextData, 0, decodeCipherTextData.length, decryptOutputData, 0);
        cipher.doFinal(decryptOutputData, alreadyDecryptedBytes);

        return new String(decryptOutputData, StandardCharsets.UTF_8);
    }

    public static void clearArray(final byte @Nullable [] array) {
        if (array != null) {
            Arrays.fill(array, (byte) 0);
        }
    }

    public record HashedPasskey(byte [] hash,
                                int pwAlgoType, int pwAlgoVersion,
                                int pwAlgoIterations, int pwAlgoMemory, int pwAlgoParallelism,
                                int keySize,
                                byte @NotNull [] salt) {
    }
}
