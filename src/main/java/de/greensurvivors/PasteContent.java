package de.greensurvivors;

import de.greensurvivors.implementation.EncryptionHelper;
import de.greensurvivors.implementation.content.BundledContentImpl;
import de.greensurvivors.implementation.content.BytesToBase64ContentIml;
import de.greensurvivors.implementation.content.SimpleStringContentImpl;
import de.greensurvivors.implementation.content.StreamToGZIPToBase64ContentImpl;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface PasteContent<T extends @NotNull Object> {
    T getContent();

    @NotNull Paste.PasteType getPasteType ();

    @NotNull String serialize () throws IOException;

    static @NotNull SimpleStringContent fromString(final @NotNull String content) {
        return new SimpleStringContentImpl(content);
    }

    static @NotNull BytesToBase64Content fromBytes(final byte @NotNull [] content) {
        return new BytesToBase64ContentIml(content);
    }

    /// note: creating a PasteBuilder with this encrypted content will NOT set the encrypted flag!
    static @NotNull SimpleStringContent enctypt (final byte @NotNull [] content, final byte @NotNull [] password) throws NoSuchAlgorithmException, InvalidCipherTextException {
        return new SimpleStringContentImpl(EncryptionHelper.encrypt(content, EncryptionHelper.hashPasskey(password)));
    }

    /// note: creating a PasteBuilder with this encrypted content will NOT set the encrypted flag!
    static @NotNull SimpleStringContent enctypt (final @NotNull String content, final byte @NotNull [] password) throws NoSuchAlgorithmException, InvalidCipherTextException {
        return new SimpleStringContentImpl(EncryptionHelper.encrypt(content, EncryptionHelper.hashPasskey(password)));
    }

    /// deflates all incoming bytes to gzip and formats the output to a base64 string
    static @NotNull StreamToGZIPToBase64Content fromStream(final @NotNull InputStream inputStream) {
        return new StreamToGZIPToBase64ContentImpl(inputStream);
    }

    static <T> @NotNull BundledContent<T> newBundledContent() {
        return new BundledContentImpl<>();
    }

    static byte @NotNull [] decodeBase64ToBytes(final @NotNull String strToDecode) {
        return BytesToBase64ContentIml.decode(strToDecode);
    }

    static @NotNull InputStream decodeBase64ToInputStream(final @NotNull String strToDecode) {
        return StreamToGZIPToBase64ContentImpl.decode(strToDecode);
    }

    /// note: keeps all the content still encoded. You might want to chain the decode process
    static @NotNull Map<String, String> decodeBundledContent(final @NotNull String strToDecode) {
        return BundledContentImpl.decode(strToDecode);
    }

    interface SimpleStringContent extends PasteContent<@NotNull String> {
    }

    interface BytesToBase64Content extends PasteContent<byte @NotNull []> {
    }

    interface StreamToGZIPToBase64Content extends PasteContent<@NotNull String> {
    }

    interface BundledContent<T> extends PasteContent<@NotNull Map<@NotNull String, @NotNull PasteContent<@NotNull T>>> {
        @Nullable PasteContent<@NotNull T> addContent(@NotNull String name, @NotNull PasteContent<@NotNull T> content);

        void addAllContent(@NotNull Map<@NotNull String, @NotNull PasteContent<@NotNull T>> contentMap);

        @Nullable PasteContent<@NotNull T> getContentByName(@NotNull String name);

        @Nullable PasteContent<@NotNull T> removeContent(@NotNull String name);

        int size();
    }
}
