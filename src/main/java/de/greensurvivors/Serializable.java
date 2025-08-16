package de.greensurvivors;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Serializable {

    @NotNull String serialize (final @NotNull Gson gson) throws IOException;

    // todo javadocs
    //static PasteContent<T> deserialize (final @NotNull String serializedState, final @NotNull Gson gson);
}
