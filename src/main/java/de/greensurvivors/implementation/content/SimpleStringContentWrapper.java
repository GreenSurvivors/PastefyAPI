package de.greensurvivors.implementation.content;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;

public record SimpleStringContentWrapper(@NotNull String getContent) implements PasteContent<@NotNull String> {

    @Override
    public Paste.@NotNull PasteType getPasteType() {
        return Paste.PasteType.PASTE;
    }

    @Override
    public @NotNull String serialize() {
        return getContent;
    }
}
