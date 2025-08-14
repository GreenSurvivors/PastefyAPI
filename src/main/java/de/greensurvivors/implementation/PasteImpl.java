package de.greensurvivors.implementation;

import de.greensurvivors.Paste;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;

public class PasteImpl<T> implements Paste<T> {
    @Override
    public @NotNull PasteType getType() {
        return ;
    }

    @Override
    public @NotNull String getTitle() {
        return ;
    }

    @Override
    public @NotNull T getContent() {
        return ;
    }

    @Override
    public @NotNull PasteVisibility getVisibility() {
        return ;
    }

    @Override
    public boolean isEncrypted() {
        return ;
    }

    @Override
    public @Nullable Instant getExpiration() {
        return ;
    }

    @Override
    public @NotNull Collection<String> getTags() {
        return ;
    }
}
