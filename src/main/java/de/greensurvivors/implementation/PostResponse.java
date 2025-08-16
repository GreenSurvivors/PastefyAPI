package de.greensurvivors.implementation;

import org.jetbrains.annotations.NotNull;

/// technical class, since the api wraps the paste for whatever reason...
public record PostResponse (boolean success, @NotNull PasteReplyImpl paste) {
}
