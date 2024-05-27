package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Set;

public class UserSetSerializer extends StdSerializer<Set<User>> {
    protected UserSetSerializer(Class<Set<User>> t) {
        super(t);
    }

    protected UserSetSerializer() {
        this(null);
    }

    @Override
    public void serialize(Set<User> users, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        int[] array = users.stream().mapToInt(User::getId).toArray();
        jsonGenerator.writeArray(array, 0, array.length);
    }
}
