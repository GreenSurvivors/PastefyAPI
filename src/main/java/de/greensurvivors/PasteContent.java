package de.greensurvivors;

import de.greensurvivors.implementation.content.SimpleStringContentWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/// T is NOT nullable!
public interface PasteContent<T> {
    T getContent();

    @NotNull Paste.PasteType getPasteType ();

    @NotNull String serialize () throws IOException;

    static @NotNull PasteContent<@NotNull String> fromString(final @NotNull String content) {
        return new SimpleStringContentWrapper(content);
    }

    // todo name -> content map
}
