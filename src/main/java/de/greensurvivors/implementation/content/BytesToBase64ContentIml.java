package de.greensurvivors.implementation.content;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

@SuppressWarnings("ClassCanBeRecord") // shut up! I know this implementation can be a record. But all the other PasteContents can't. Don't screw with my naming conventions!
public class BytesToBase64ContentIml implements PasteContent.BytesToBase64Content {
    private final byte @NotNull [] content;

    public BytesToBase64ContentIml(final byte @NotNull [] content) {
        this.content = content;
    }

    public static byte @NotNull [] decode(final @NotNull String strToDecode) throws IllegalArgumentException {
        return Base64.getDecoder().decode(strToDecode);
    }

    @Override
    public byte @NotNull [] getContent() {
        return content;
    }

    @Override
    public @NotNull Paste.PasteType getPasteType() {
        return Paste.PasteType.PASTE;
    }

    @Override
    public @NotNull String serialize() {
        return Base64.getEncoder().encodeToString(content);
    }
}
