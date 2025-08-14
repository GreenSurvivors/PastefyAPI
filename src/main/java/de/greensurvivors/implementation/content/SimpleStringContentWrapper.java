package de.greensurvivors.implementation.content;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;

public class SimpleStringContentWrapper implements PasteContent<@NotNull String> {
    private @NotNull String content;

    public SimpleStringContentWrapper(final @NotNull String content) {
        this.content = content;
    }

    @Override
    public @NotNull String getContent() {
        return content;
    }

    @Override
    public Paste.PasteType getPasteType() {
        return Paste.PasteType.PASTE;
    }
}
