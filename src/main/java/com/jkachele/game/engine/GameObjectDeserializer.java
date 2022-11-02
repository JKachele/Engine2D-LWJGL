/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GameObjectDeserializer.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.engine;

import com.google.gson.*;
import com.jkachele.game.components.Component;
import com.jkachele.game.components.Transform;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject gameObject = new GameObject(name);
        for (JsonElement e : components) {
            Component c = context.deserialize(e, Component.class);
            gameObject.addComponent(c);
        }
        gameObject.transform = gameObject.getComponent(Transform.class);
        return gameObject;
    }
}
