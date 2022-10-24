/******************************************
 *Project-------Engine2D-LWJGL
 *File----------ComponentDeserializer.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.components;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String type = jsonObj.get("type").getAsString();
        JsonElement properties = jsonObj.get("properties");

        try {
            return context.deserialize(properties, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }
}
