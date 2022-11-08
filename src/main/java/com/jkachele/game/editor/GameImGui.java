/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GameImGui.java
 *Author--------Justin Kachele
 *Date----------11/1/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@SuppressWarnings("DuplicatedCode")
public class GameImGui {
    private static final float DEFAULT_WIDTH = 220.0f;

    public static int dragInt(String label, int value, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valueArray = {value};
        ImGui.dragInt("##value", valueArray, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valueArray[0];
    }

    public static int dragInt(String label, int value) {
        return dragInt(label, value, DEFAULT_WIDTH);
    }

    public static float dragFloat(String label, float value, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valueArray = {value};
        ImGui.dragFloat("##value", valueArray, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valueArray[0];
    }

    public static float dragFloat(String label, float value) {
        return dragFloat(label, value, DEFAULT_WIDTH);
    }

    public static float dragFloatButton(String label, float value, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 1.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("R", buttonSize.x, buttonSize.y)) {
            value = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] valueArray = {value};
        ImGui.dragFloat("##x", valueArray, 0.1f);
        ImGui.popItemWidth();

        ImGui.nextColumn();

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();

        return valueArray[0];
    }

    public static float dragFloatButton(String label, float value, float resetValue) {
        return dragFloatButton(label, value, resetValue, DEFAULT_WIDTH);
    }

    public static float dragFloatButton(String label, float value) {
        return dragFloatButton(label, value, 0.0f, DEFAULT_WIDTH);
    }

    public static boolean checkbox(String label, boolean value, float columnWidth) {
        boolean res;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        boolean[] valueArray = {value};
        res = ImGui.checkbox("##value", valueArray[0]);

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static boolean checkbox(String label, boolean value) {
        return checkbox(label, value, DEFAULT_WIDTH);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 1.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("x", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, DEFAULT_WIDTH);
    }

    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, 0.0f, DEFAULT_WIDTH);
    }

    public static void drawVec3Control(String label, Vector3f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 1.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("x", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.2f, 0.7f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.3f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("z", buttonSize.x, buttonSize.y)) {
            values.z = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesZ = {values.z};
        ImGui.dragFloat("##z", vecValuesZ, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];
        values.z = vecValuesZ[0];

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();
    }

    public static void drawVec3Control(String label, Vector3f values, float resetValue) {
        drawVec3Control(label, values, resetValue, DEFAULT_WIDTH);
    }

    public static void drawVec3Control(String label, Vector3f values) {
        drawVec3Control(label, values, 0.0f, DEFAULT_WIDTH);
    }

    public static void drawVec4Control(String label, Vector4f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 1.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("x", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text("");
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.1f, 0.1f, 0.7f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.1f, 0.7f, 1.0f);
        if (ImGui.button("z", buttonSize.x, buttonSize.y)) {
            values.z = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesZ = {values.z};
        ImGui.dragFloat("##z", vecValuesZ, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.7f, 0.1f, 0.7f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.8f, 0.2f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.7f, 0.1f, 0.7f, 1.0f);
        if (ImGui.button("w", buttonSize.x, buttonSize.y)) {
            values.w = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesW = {values.w};
        ImGui.dragFloat("##w", vecValuesW, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];
        values.z = vecValuesZ[0];
        values.w = vecValuesW[0];

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();
    }

    public static void drawVec4Control(String label, Vector4f values, float resetValue) {
        drawVec4Control(label, values, resetValue, DEFAULT_WIDTH);
    }

    public static void drawVec4Control(String label, Vector4f values) {
        drawVec4Control(label, values, 0.0f, DEFAULT_WIDTH);
    }

    public static boolean colorPicker4(String label, Vector4f color, float columnWidth) {
        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4("##ColorPicker", imColor)) {
            color.set(imColor);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static boolean colorPicker4(String label, Vector4f color) {
        return colorPicker4(label, color, DEFAULT_WIDTH);
    }

    public static ImInt enumCombo(String label, String[] enumValues, String enumType, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImInt index = new ImInt(indexOf(enumType, enumValues));
        ImGui.combo("##value", index, enumValues, enumValues.length);

        ImGui.columns(1);
        ImGui.popID();

        return index;
    }

    public static ImInt enumCombo(String label, String[] enumValues, String enumType) {
        return enumCombo(label, enumValues,enumType, DEFAULT_WIDTH);
    }

    private static int indexOf(String enumType, String[] enumValues) {
        for (int i = 0; i < enumValues.length; i++) {
            if (enumValues[i].equals(enumType)) {
                return i;
            }
        }
        return -1;
    }
}
