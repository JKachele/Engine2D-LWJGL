/******************************************
 *Project-------Learn-LWJGL
 *File----------Scene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.renderer.Renderer;
import imgui.ImGui;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject currentGameObject = null;

    public abstract void update(float dt);

    public Scene() {
    }

    public void init() {

    }

    public void start() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public void sceneImGui() {
        if(currentGameObject != null) {
            ImGui.begin("Inspector");
            currentGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }
}
