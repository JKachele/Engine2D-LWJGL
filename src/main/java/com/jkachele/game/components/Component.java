/******************************************
 *Project-------Learn-LWJGL
 *File----------Component.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.GameObject;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    public transient GameObject gameObject = null;

    public void update(float dt) {

    }

    public void start() {

    }

    public void imgui() {
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

                Class<?> type = field.getType();
                Object value = field.get(this);
                String name = field.getName();
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);

                if (type == int.class) {
                    int val = (int)value;
                    int[] imInt = {val};
                    ImGui.text(name);
                    ImGui.sameLine();
                    if (ImGui.dragInt("", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (type == float.class) {
                    float val = (float) value;
                    float[] imFloat = {val};
                    ImGui.text(name);
                    ImGui.sameLine();
                    if (ImGui.dragFloat("", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    ImGui.text(name);
                    ImGui.sameLine();
                    if (ImGui.checkbox("", val)) {
                        field.set(this, !val);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    ImGui.text(name);
                    ImGui.sameLine();
                    if (ImGui.dragFloat3("", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                        field.set(this, val);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    ImGui.text(name);
                    ImGui.sameLine();
                    if (ImGui.dragFloat4("", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                        field.set(this, val);
                    }
                }


                if (isPrivate) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
