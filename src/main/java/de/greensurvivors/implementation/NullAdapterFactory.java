package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NullAdapterFactory implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(final @NotNull Gson gson, final TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getAdapter(type);

        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                boolean originalSerializeNulls = out.getSerializeNulls();
                // Only change behavior if it's null, otherwise it'll affect nested complex type's serialization.
                if (value == null) {
                    // Make com.google.gson.stream.JsonWriter.nullValue skip the deferred name if null value.
                    out.setSerializeNulls(false);
                    out.value((String) null);
                } else {
                    try {

                        //   System.out.println(delegate.getClass());

                        delegate.write(out, value);
                    } finally {
                        // Restore original behavior for the rest of the data.
                        out.setSerializeNulls(originalSerializeNulls);
                    }

                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return delegate.read(in);
            }
        };
    }
}
