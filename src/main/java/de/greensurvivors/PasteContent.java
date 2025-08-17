package de.greensurvivors;

import com.google.gson.Gson;
import de.greensurvivors.implementation.content.SimpleStringContentWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/// T is NOT nullable!
public interface PasteContent<T> {
    T getContent();

    @NotNull Paste.PasteType getPasteType ();

    @NotNull String serialize (final @NotNull Gson gson) throws IOException;

    static @NotNull PasteContent<@NotNull String> fromString(final @NotNull String content) {
        return new SimpleStringContentWrapper(content);
    }

    // todo name -> content map
    // todo deal with decryption
}
