/******************************************
 *Project-------Engine2D-LWJGL
 *File----------PropertiesWindow.java
 *Author--------Justin Kachele
 *Date----------10/21/2022
 *License-------MIT License
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

    private float debounceTime = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        currentGameObject = null;
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        debounceTime -= dt;

        if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT) && debounceTime < 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();

            int gameObjectID = pickingTexture.readPixel(x, y);
            currentGameObject = currentScene.getGameObject(gameObjectID);

            this.debounceTime = 0.2f;
        }
    }

    public void imGui() {
        if(currentGameObject != null) {
            ImGui.begin("Properties");
            currentGameObject.imGui();
            ImGui.end();
        }
    }

    public GameObject getCurrentGameObject() {
        return currentGameObject;
    }
}
