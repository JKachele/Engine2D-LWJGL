/******************************************
 *Project-------Engine2D-LWJGL
 *File----------LevelEditorScene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.scene;

import com.jkachele.game.components.*;
import com.jkachele.game.engine.*;
import com.jkachele.game.physics2d.PhysicsSystem2D;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Constants;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {
    Spritesheet sprites;
    Spritesheet marioSprites;
    GameObject levelEditorComponents;
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 20.0f, new Vector2f(0, -20));
    DebugObject obj1;
    DebugObject obj2;

    @Override
    public void init(boolean reset) {
        levelEditorComponents = new GameObject("LevelEditor", new Transform(), 0);
        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new GridLines());

//        obj1 = new DebugObject("Box2D-1", new Transform(new Vector2f(100, 1000)), 0);
//        Rigidbody2D rigidBody1 = new Rigidbody2D();
//        rigidBody1.setRawTransform(obj1.transform);
//        rigidBody1.setMass(100);
//        obj2 = new DebugObject("Box2D-2", new Transform(new Vector2f(500, 1000)), 0);
//        Rigidbody2D rigidBody2 = new Rigidbody2D();
//        rigidBody2.setRawTransform(obj2.transform);
//        rigidBody2.setMass(200);
//        rigidBody2.addVelocity(new Vector2f(100, 0));
//
//        physics.addRigidBody(rigidBody1);
//        physics.addRigidBody(rigidBody2);

        loadResources();
        this.camera = new Camera(new Vector2f());
        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");
        marioSprites = AssetPool.getSpritesheet("assets/images/spritesheets/characters.png");
        if (levelLoaded && !reset) {
            if (!gameObjects.isEmpty()) {
                this.currentGameObject = gameObjects.get(0);
            }
            return;
        }
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.addSpritesheet("assets/images/spritesheets/characters.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/characters.png"),
                        16, 16, 26, 0));
        AssetPool.getTexture("assets/images/blendImageG.png");

        for (GameObject gameObject : gameObjects) {
            if(gameObject.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
                if (sprite.getTexture() != null) {
                    sprite.setTexture((AssetPool.getTexture(sprite.getTexture().getFilepath())));
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        levelEditorComponents.update(dt);

        // Update all game objects in the scene
        for (GameObject gameObject : this.gameObjects) {
            gameObject.update(dt);
        }

        // Render the scene
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Scene Sprites");

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
                    uvCoords[2].x, uvCoords[0].y, uvCoords[0].x, uvCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
                // Attach this to the mouse cursor
                levelEditorComponents.getComponent(MouseControls.class).pickupObject(object);
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
