package de.greensurvivors;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

@SuppressWarnings("unused") // api
public interface Folder {
    @NotNull String getName();

    @NotNull Set<@NotNull Paste<?>> getPastes();

    @NotNull Set<Folder> getChildren();

}
