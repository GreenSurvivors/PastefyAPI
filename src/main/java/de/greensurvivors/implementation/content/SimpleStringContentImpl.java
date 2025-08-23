package de.greensurvivors.implementation.content;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class SimpleStringContentImpl implements PasteContent.SimpleStringContent {
    private final @NotNull String getContent;
    private @NotNull Paste.PasteType type = Paste.PasteType.PASTE;

    public SimpleStringContentImpl(@NotNull String getContent) {
        this.getContent = getContent;
    }

    @Override
    public @NotNull Paste.PasteType getPasteType() {
        return type;
    }

    /// note: This is not part of the API since a plain string should always be
    /// an also flat paste.
    /// However, when creating a pasteBuilder from a paste reply it may also be string containing multiple pastes.
    /// So the type has to get overwritten here internally.
    public void setPasteType(final @NotNull Paste.PasteType newType) {
        this.type = newType;
    }

    @Override
    public @NotNull String serialize() {
        return getContent;
    }

    @Override
    public @NotNull String getContent() {
        return getContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SimpleStringContentImpl) obj;
        return Objects.equals(this.getContent, that.getContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent);
    }

    @Override
    public String toString() {
        return "SimpleStringContent[" +
            "getContent=" + getContent +
            ", type=" + type + ']';
    }

}
