/******************************************
 *Project-------Learn-LWJGL
 *File----------GameObject.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;

        this.uid = ID_COUNTER;
        ID_COUNTER++;
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

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public void imgui() {
        for (Component component : components) {
            component.imgui();
        }
    }

    public int zIndex() {
        return zIndex;
    }

    public int getUid() {
        return this.uid;
    }

    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }
}
