package de.greensurvivors.implementation;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

class InstantAdapter extends TypeAdapter<Instant> {
    // TimeStamp - as used by the web api - or Instant, as used by this lib, is always utc.
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n").withZone(ZoneOffset.UTC);

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        boolean originalSerializeNulls = out.getSerializeNulls();
        out.setSerializeNulls(false);

        try {
            out.value(DATE_TIME_FORMATTER.format(value));
        } finally {
            // Restore original behavior for the rest of the data.
            out.setSerializeNulls(originalSerializeNulls);
        }
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return Instant.from(DATE_TIME_FORMATTER.parse(in.nextString()));
    }
}
