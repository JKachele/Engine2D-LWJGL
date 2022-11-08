/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Component.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.editor.GameImGui;
import com.jkachele.game.engine.GameObject;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;

    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }

    public void update(float dt) {

    }

    public void editorUpdate(float dt) {

    }

    public void start() {

    }

    public void destroy() {

    }

    // Uses Java reflection to get the fields of components to be able to edit them in ImGUI
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void imGui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());

                if (isTransient) {
                    continue;
                }
                if (isPrivate) {
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);

                if (type == int.class) {
                    int val = (int)value;
                    field.set(this, GameImGui.dragInt(name, val));
                } else if (type == float.class) {
                    float val = (float) value;
                    field.set(this, GameImGui.dragFloat(name, val));
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (GameImGui.checkbox(name, val)) {
                        field.set(this, !val);
                    }
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f) value;
                    GameImGui.drawVec2Control(name, val);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    GameImGui.drawVec3Control(name, val);
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    GameImGui.drawVec4Control(name, val);
                } else if (type.isEnum()) {
                    String[] enumValues = getEnumValues(type);
                    String enumType = ((Enum<?>)value).name();
                    ImInt index = GameImGui.enumCombo(name, enumValues, enumType);
                    field.set(this, type.getEnumConstants()[index.get()]);
//                    ImInt index = new ImInt(indexOf(enumType, enumValues));
//                    if (ImGui.combo(name, index, enumValues, enumValues.length)) {
//                        field.set(this, type.getEnumConstants()[index.get()]);
//                    }
                }


                if (isPrivate) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T extends Enum<T>> String[] getEnumValues(Class<T> type) {
         String[] enumValues = new String[type.getEnumConstants().length];
         int i = 0;
         for (T val : type.getEnumConstants()) {
             enumValues[i] = val.name();
             i++;
         }
         return enumValues;
    }

    private int indexOf(String enumType, String[] enumValues) {
        for (int i = 0; i < enumValues.length; i++) {
            if (enumValues[i].equals(enumType)) {
                return i;
            }
        }
        return -1;
    }

    public void generateID() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER;
            ID_COUNTER++;
        }
    }

    public int getUid() {
        return this.uid;
    }
}
