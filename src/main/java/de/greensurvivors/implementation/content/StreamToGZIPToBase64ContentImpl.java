package de.greensurvivors.implementation.content;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StreamToGZIPToBase64ContentImpl implements PasteContent.StreamToGZIPToBase64Content {// todo the day this lib upgrades to Java 22 I will extending BytesToBase64ContentImpl here.
    private final @NotNull String encodedContent;

    public StreamToGZIPToBase64ContentImpl(final @NotNull InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available())) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) { // close stream to write all bytes!
                // input stream to output!
                inputStream.transferTo(gzipOutputStream);
            }

            encodedContent = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull InputStream decode(final @NotNull String strToDecode) {
        byte[] bytes = Base64.getDecoder().decode(strToDecode);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            return new GZIPInputStream(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull String getContent() {
        return encodedContent;
    }

    @Override
    public @NotNull Paste.PasteType getPasteType() {
        return Paste.PasteType.PASTE;
    }

    @Override
    public @NotNull String serialize() {
        return encodedContent;
    }
}
