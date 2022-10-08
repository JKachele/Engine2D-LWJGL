/******************************************
 *Project-------Learn-LWJGL
 *File----------LevelEditorScene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.components.SpriteRenderer;
import com.jkachele.game.components.Spritesheet;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Color;
import imgui.ImGui;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene{

    private GameObject obj1;
    private GameObject obj2;
    private GameObject obj3;
    private GameObject obj4;
    Spritesheet marioSprites;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        marioSprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(10, 10),
                new Vector2f(256, 256)), 2);
        obj1.addComponent(new SpriteRenderer(marioSprites.getSprite(1)));
        this.addGameObject(obj1);

        obj2 = new GameObject("Object 2", new Transform(new Vector2f(10, 300),
                new Vector2f(256, 256)), 1);
        obj2.addComponent(new SpriteRenderer(marioSprites.getSprite(14)));
        this.addGameObject(obj2);

        obj3 = new GameObject("Object 3", new Transform(new Vector2f(500, 10),
                new Vector2f(256, 256)), -1);
        obj3.addComponent(new SpriteRenderer(new Color(1f, 0f, 0f, 0.5f).getVector()));
        this.addGameObject(obj3);
        this.currentGameObject = obj3;

        obj4 = new GameObject("Object 4", new Transform(new Vector2f(700, 10),
                new Vector2f(256, 256)), -2);
        obj4.addComponent(new SpriteRenderer(new Color(0f, 1f, 0f, 0.5f).getVector()));
        this.addGameObject(obj4);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
    }

    int spriteIndex = 2;
    float spriteFlipTime = 0.2f;
    float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.movePosition(100f * dt, 0);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.movePosition(-100f * dt, 0);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.movePosition(0, 100f * dt);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.movePosition(0, -100f * dt);
        }



        spriteFlipTimeLeft -= dt;
        if(spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 3) {
                spriteIndex = 2;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(marioSprites.getSprite(spriteIndex));
            obj1.transform.position.x += 100f;
            if (obj1.transform.position.x > camera.getCamRangeX().y) {
                obj1.transform.position.x = camera.getCamRangeX().x - 100;
            }
            if (obj1.transform.position.y < camera.getCamRangeY().x + 10 || obj1.transform.position.y > camera.getCamRangeY().x + 10) {
                obj1.transform.position.y = camera.getCamRangeY().x + 10;
            }
        }

        // Update all game objects in the scene
        for (GameObject gameObject : this.gameObjects) {
            gameObject.update(dt);
        }

        // Render the scene
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test Window");
        ImGui.text("Test Text");
        ImGui.end();
    }
}
