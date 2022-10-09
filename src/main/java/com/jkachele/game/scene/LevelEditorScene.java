/******************************************
 *Project-------Learn-LWJGL
 *File----------LevelEditorScene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.scene;

import com.jkachele.game.components.RigidBody;
import com.jkachele.game.components.Sprite;
import com.jkachele.game.components.SpriteRenderer;
import com.jkachele.game.components.Spritesheet;
import com.jkachele.game.engine.Camera;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.Transform;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Color;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private GameObject obj2;
    Spritesheet sprites;

    public LevelEditorScene() {
    }

    @Override
    public void init(boolean reset) {
        loadResources();
        this.camera = new Camera(new Vector2f());
        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");
        if (levelLoaded && !reset) {
            this.currentGameObject = gameObjects.get(0);
            return;
        }

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(500, 10),
                new Vector2f(256, 256)), -1);
        SpriteRenderer obj3Sprite = new SpriteRenderer();
        obj3Sprite.setColor(new Color(1f, 0f, 0f, 0.5f).getVector());
        obj1.addComponent(obj3Sprite);
        obj1.addComponent(new RigidBody());
        this.addGameObject(obj1);
        this.currentGameObject = obj1;

        obj2 = new GameObject("Object 2", new Transform(new Vector2f(700, 10),
                new Vector2f(256, 256)), -2);
        SpriteRenderer obj4SpriteRenderer = new SpriteRenderer();
        Sprite obj4Sprite = new Sprite();
        obj4Sprite.setTexture(AssetPool.getTexture("assets/images/blendImageG.png"));
        obj4SpriteRenderer.setSprite(obj4Sprite);
        obj2.addComponent(obj4SpriteRenderer);
        this.addGameObject(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.getTexture("assets/images/blendImageG.png");
    }

    int spriteIndex = 2;
    float spriteFlipTime = 0.2f;
    float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
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

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexID();
            Vector2f[] uvCoords = sprite.getUvCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight,
                    uvCoords[0].x, uvCoords[0].y, uvCoords[2].x, uvCoords[2].y)) {
                System.out.println("Button " + i + " clicked");
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }

        }

        ImGui.end();
    }
}
