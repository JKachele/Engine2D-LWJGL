/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Scene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkachele.game.components.Component;
import com.jkachele.game.components.ComponentDeserializer;
import com.jkachele.game.components.Transform;
import com.jkachele.game.engine.Camera;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.GameObjectDeserializer;
import com.jkachele.game.physics2d.Physics2D;
import com.jkachele.game.renderer.Renderer;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private boolean levelLoaded;
    private List<GameObject> gameObjects;

    private SceneInitializer sceneInitializer;
    private Physics2D physics2D;

    public Scene(SceneInitializer sceneInitializer) {
        this.renderer = new Renderer();
        this.isRunning = false;
        this.levelLoaded = false;
        this.gameObjects = new ArrayList<>();
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
    }

    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    public void update(float dt) {
        this.camera.adjustProjection();

        this.physics2D.update(dt);

        // Update all game objects in the scene
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.update(dt);

            if (gameObject.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObject);
                this.physics2D.destroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void editorUpdate(float dt) {
        this.camera.adjustProjection();

        // Update all game objects in the scene
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.editorUpdate(dt);

            if (gameObject.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObject);
                this.physics2D.destroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void start() {
        // Avoids Concurrent modification exception
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < this.gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.start();
            this.renderer.add(gameObject);
            this.physics2D.add(gameObject);
        }
        isRunning = true;
    }

    public void destroy() {
        for (GameObject gameObject : this.gameObjects) {
            gameObject.destroy();
        }
    }

    public void render() {
        this.renderer.render();
    }

    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
            this.physics2D.add(gameObject);
        }
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public void imGui() {
        this.sceneInitializer.imGui();
    }

    public GameObject createGameObject(String name) {
        GameObject gameObject = new GameObject(name);
        gameObject.addComponent(new Transform());
        gameObject.transform = gameObject.getComponent(Transform.class);
        return gameObject;
    }

    public void save(String filePath) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter(filePath, false);
            // Only serialize objects that are not transient
            List<GameObject> gameObjectsToSerialize = new ArrayList<>();
            for (GameObject gameObject : this.gameObjects) {
                if (!gameObject.isTransient()) {
                    gameObjectsToSerialize.add(gameObject);
                }
            }
            writer.write(gson.toJson(gameObjectsToSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String filePath) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(filePath)));
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

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}
