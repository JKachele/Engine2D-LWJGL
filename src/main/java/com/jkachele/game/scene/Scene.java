/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Scene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkachele.game.components.Component;
import com.jkachele.game.components.ComponentDeserializer;
import com.jkachele.game.engine.Camera;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.GameObjectDeserializer;
import com.jkachele.game.renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected boolean levelLoaded = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public abstract void update(float dt);
    public abstract void render();

    public Scene() {
    }

    public void init(boolean reset) {

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

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public void imGui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        try {
            FileWriter writer = new FileWriter("assets/levels/levelEditor.txt", false);
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("assets/levels/levelEditor.txt")));
        } catch (IOException e) {
            System.err.println("File: " + inFile + " does not exist, New file created.");
        }

        if (!inFile.equals("")) {
            int maxObjectID = -1;
            int maxComponentID = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (GameObject obj : objs) {
                addGameObject(obj);

                for (Component c : obj.getComponents()) {
                    if (c.getUid() > maxComponentID) {
                        maxComponentID = c.getUid();
                    }
                }
                if (obj.getUid() > maxObjectID) {
                    maxObjectID = obj.getUid();
                }
            }

            maxObjectID++;
            maxComponentID++;
            GameObject.init(maxObjectID);
            Component.init(maxComponentID);
            this.levelLoaded = true;
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
