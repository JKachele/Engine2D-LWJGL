/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GameObject.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.components.Component;
import com.jkachele.game.components.Transform;
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

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER;
        ID_COUNTER++;
    }

    public static void init(int maxID) {
        ID_COUNTER = maxID;
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

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void start() {
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

    public int getUid() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }
}
