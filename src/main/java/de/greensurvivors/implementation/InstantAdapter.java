package de.greensurvivors.implementation;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

class InstantAdapter extends TypeAdapter<Instant> { // todo don't use Timestamp here!
    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(Timestamp.from(value).toString());
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return Timestamp.valueOf(in.nextString()).toInstant();
    }
}
