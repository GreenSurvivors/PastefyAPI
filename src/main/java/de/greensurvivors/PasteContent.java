package de.greensurvivors;

import de.greensurvivors.implementation.content.SimpleStringContentWrapper;
import org.jetbrains.annotations.NotNull;

/// T is NOT nullable!
public interface PasteContent<T> {
    T getContent ();

    Paste.PasteType getPasteType ();

    static PasteContent<String> fromString(@NotNull String content) {
        return new SimpleStringContentWrapper(content);
    }

    // todo name -> content map
    // todo deal with encryption
}
