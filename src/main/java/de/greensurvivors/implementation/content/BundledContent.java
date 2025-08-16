package de.greensurvivors.implementation.content;

import com.google.gson.Gson;
import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BundledContent<T> implements PasteContent<Map<String, PasteContent<T>>> {
    private final Map<String, PasteContent<T>> contentMap = new LinkedHashMap<>();

    @Override
    public Map<String, PasteContent<T>> getContent() {
        return contentMap;
    }

    @Override
    public @NotNull Paste.PasteType getPasteType() {
        return Paste.PasteType.MULTI_PASTE;
    }

    @Override
    public @NotNull String serialize(@NotNull Gson gson) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('[');
        for (Iterator<Map.Entry<String, PasteContent<T>>> iterator = contentMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, PasteContent<T>> entry = iterator.next();
            stringBuilder.append('{');

            stringBuilder.append("\\\"name\\\":").append("\\\"").append(entry.getKey()).append("\\\",");
            stringBuilder.append("\\\"contents\\\":").append("\\\"").append(entry.getValue().serialize(gson)).append("\\\",");

            stringBuilder.append('}');

            if (iterator.hasNext()) {
                stringBuilder.append(',');
            }
        }

        stringBuilder.append(']');

        return stringBuilder.toString();
    }
}
