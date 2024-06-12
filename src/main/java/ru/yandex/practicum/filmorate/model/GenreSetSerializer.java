package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

public class GenreSetSerializer extends StdSerializer<Set<Genre>> {
    protected GenreSetSerializer(Class<Set<Genre>> t) {
        super(t);
    }

    protected GenreSetSerializer() {
        this(null);
    }

    @Override
    public void serialize(Set<Genre> genres, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        int[] array = genres.stream().mapToInt(Genre::getId).toArray();
        jsonGenerator.writeStartArray();
        genres.stream().sorted(Comparator.comparing(Genre::getId)).forEach(genre -> {
            try {
                jsonGenerator.writeObject(genre);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        jsonGenerator.writeEndArray();
    }
}
