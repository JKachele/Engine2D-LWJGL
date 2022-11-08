/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GameObject.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkachele.game.components.Component;
import com.jkachele.game.components.ComponentDeserializer;
import com.jkachele.game.components.SpriteRenderer;
import com.jkachele.game.components.Transform;
import com.jkachele.game.util.AssetPool;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid;

    private String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean isTransient = false;
    private boolean pickable = true;
    private boolean isDead = false;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER;
        ID_COUNTER++;
    }

    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }

    public void destroy() {
        this.isDead = true;
        for (Component component : components) {
            component.destroy();
        }
    }

    public void addComponent(Component component) {
        component.generateID();
        this.components.add(component);
        component.gameObject = this;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component";
                }
            }
        }
        return null;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void update(float dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void editorUpdate(float dt) {
        for (Component component : components) {
            component.editorUpdate(dt);
        }
    }

    public GameObject copy() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String objAsJson = gson.toJson(this);
        GameObject newGameObject = gson.fromJson(objAsJson, GameObject.class);
        newGameObject.generateUid();
        for (Component component : newGameObject.getComponents()) {
            component.generateID();
        }

        SpriteRenderer sprite = newGameObject.getComponent(SpriteRenderer.class);
        if (sprite!= null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }

        return newGameObject;
    }

    public void start() {
        //noinspection ForLoopReplaceableByForEach
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void imGui() {
        for (Component component : components) {
            if (ImGui.collapsingHeader(component.getClass().getSimpleName())) {
                component.imGui();
            }
        }
    }

    public void generateUid() {
        this.uid = ID_COUNTER;
        ID_COUNTER++;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public boolean isPickable() {
        return pickable;
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getUid() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }
}
