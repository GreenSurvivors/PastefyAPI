package de.greensurvivors;

import de.greensurvivors.implementation.content.SimpleStringContentWrapper;
import org.jetbrains.annotations.NotNull;

/// T is NOT nullable!
public interface PasteContent<T> extends Serializable {
    T getContent();

    @NotNull Paste.PasteType getPasteType ();

    static @NotNull PasteContent<@NotNull String> fromString(final @NotNull String content) {
        return new SimpleStringContentWrapper(content);
    }

    // todo name -> content map
    // todo deal with decryption
}
