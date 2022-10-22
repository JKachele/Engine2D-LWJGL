/******************************************
 *Project-------Engine2D-LWJGL
 *File----------PropertiesWindow.java
 *Author--------Justin Kachele
 *Date----------10/21/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.editor;

import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.MouseListener;
import com.jkachele.game.renderer.PickingTexture;
import com.jkachele.game.scene.Scene;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject currentGameObject;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture) {
        currentGameObject = null;
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();

            int gameObjectID = pickingTexture.readPixel(x, y);
            currentGameObject = currentScene.getGameObject(gameObjectID);
        }
    }

    public void imGui() {
        if(currentGameObject != null) {
            ImGui.begin("Properties");
            currentGameObject.imgui();
            ImGui.end();
        }
    }
}
