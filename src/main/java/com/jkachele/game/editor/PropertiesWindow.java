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
import com.jkachele.game.physics2d.components.Box2DCollider;
import com.jkachele.game.physics2d.components.CircleCollider;
import com.jkachele.game.physics2d.components.Rigidbody2D;
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
            GameObject pickedObject = currentScene.getGameObject(gameObjectID);
            if (pickedObject != null && pickedObject.isPickable()) {
                currentGameObject = pickedObject;
            } else if (pickedObject == null && !MouseListener.isDragging()) {
                currentGameObject = null;
            }

            this.debounceTime = 0.2f;
        }
    }

    public void imGui() {
        if(currentGameObject != null) {
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigidbody2D")) {
                    if (currentGameObject.getComponent(Rigidbody2D.class) == null) {
                        currentGameObject.addComponent(new Rigidbody2D());
                    }
                }
                if (ImGui.menuItem("Add Box2DCollider")) {
                    if (currentGameObject.getComponent(Box2DCollider.class) == null &&
                            currentGameObject.getComponent(CircleCollider.class) == null) {
                        currentGameObject.addComponent(new Box2DCollider());
                    }
                }
                if (ImGui.menuItem("Add CircleCollider")) {
                    if (currentGameObject.getComponent(CircleCollider.class) == null &&
                            currentGameObject.getComponent(Box2DCollider.class) == null) {
                        currentGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            currentGameObject.imGui();
            ImGui.end();
        }
    }

    public GameObject getCurrentGameObject() {
        return currentGameObject;
    }

    public void setCurrentGameObject(GameObject gameObject) {
        this.currentGameObject = gameObject;
    }
}
