package ru.javawebinar.basejava.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public class JsonAbstractSectionAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    public static final String CLASSNAME = "CLASSNAME";
    public static final String INSTANCE = "INSTANCE";

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = primitive.getAsString();
        try {
            Class clazz = Class.forName(className);
            return context.deserialize(jsonObject.get(INSTANCE), clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    @Override
    public JsonElement serialize(T section, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject returnValue = new JsonObject();
        returnValue.addProperty(CLASSNAME, section.getClass().getName());
        JsonElement element = context.serialize(section);
        returnValue.add(INSTANCE, element);
        return returnValue;
    }
}
