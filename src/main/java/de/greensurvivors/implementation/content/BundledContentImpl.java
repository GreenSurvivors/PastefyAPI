package de.greensurvivors.implementation.content;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BundledContentImpl<T> implements PasteContent.BundledContent<T> {
    private final @NotNull Map<@NotNull String, @NotNull PasteContent<@NotNull T>> contentMap = new LinkedHashMap<>();

    public static @NotNull Map<@NotNull String, @NotNull String> decode(final @NotNull String strToDecode) {
        JsonObject obj = JsonParser.parseString(strToDecode).getAsJsonObject();
        Map<String, String> result = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            // Use Gson's serialization to preserve exact JSON for objects/arrays
            if (entry.getValue().isJsonPrimitive()) {
                result.put(entry.getKey(), entry.getValue().getAsJsonPrimitive().toString()
                    .replaceAll("^\"|\"$", "")); // strip quotes if it's a JSON string
            } else {
                result.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return result;
    }

    @Override
    public @Nullable PasteContent<@NotNull T> addContent(final @NotNull String name, final @NotNull PasteContent<@NotNull T> content) {
        return contentMap.put(name, content);
    }

    @Override
    public void addAllContent(final @NotNull Map<@NotNull String, @NotNull PasteContent<@NotNull T>> contentMap) {
        this.contentMap.putAll(contentMap);
    }

    @Override
    public @Nullable PasteContent<@NotNull T> getContentByName(final @NotNull String name) {
        return contentMap.get(name);
    }

    @Override
    public @Nullable PasteContent<@NotNull T> removeContent(final @NotNull String name) {
        return contentMap.remove(name);
    }

    @Override
    public int size() {
        return contentMap.size();
    }

    @Override
    public @NotNull @Unmodifiable Map<@NotNull String, @NotNull PasteContent<@NotNull T>> getContent() {
        return Map.copyOf(contentMap);
    }

    @Override
    public @NotNull Paste.PasteType getPasteType() {
        return Paste.PasteType.MULTI_PASTE;
    }

    @Override
    public @NotNull String serialize() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('[');
        for (Iterator<Map.Entry<String, PasteContent<@NotNull T>>> iterator = contentMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, PasteContent<@NotNull T>> entry = iterator.next();
            stringBuilder.append('{');

            stringBuilder.append("\\\"name\\\":").append("\\\"").append(entry.getKey()).append("\\\",");
            stringBuilder.append("\\\"contents\\\":").append("\\\"").append(entry.getValue().serialize()).append("\\\",");

            stringBuilder.append('}');

            if (iterator.hasNext()) {
                stringBuilder.append(',');
            }
        }

        stringBuilder.append(']');

        return stringBuilder.toString();
    }
}
