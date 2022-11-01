/******************************************
 *Project-------Engine2D-LWJGL
 *File----------LevelEditorScene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.scene;

import com.jkachele.game.components.*;
import com.jkachele.game.engine.*;
import com.jkachele.game.physics2d.PhysicsSystem2D;
import com.jkachele.game.physics2d.rigidbody.Rigidbody2D;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Constants;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {
    String spritesPath = "assets/images/spritesheets/decorationsAndBlocks.png";
    Spritesheet sprites;
    String marioSpritesPath = "assets/images/spritesheets/characters.png";
    Spritesheet marioSprites;
    String gizmosPath = "assets/images/gizmos.png";
    Spritesheet gizmos;
    GameObject levelEditorComponents;
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -50));
    DebugObject obj1;
    DebugObject obj2;
    Rigidbody2D rigidBody1;
    Rigidbody2D rigidBody2;

    @Override
    public void init(boolean reset) {
        loadResources();
        sprites = AssetPool.getSpritesheet(spritesPath);
        marioSprites = AssetPool.getSpritesheet(marioSpritesPath);
        gizmos = AssetPool.getSpritesheet(gizmosPath);

        this.camera = new Camera(new Vector2f(0, 0));
        levelEditorComponents = new GameObject("LevelEditor", new Transform(), 0);
        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(this.camera));
        levelEditorComponents.addComponent(new GizmoSystem(gizmos));
        levelEditorComponents.start();

//        obj1 = new DebugObject("Circle-1", new Transform(new Vector2f(100, 1000)), 0);
//        rigidBody1 = new Rigidbody2D();
//        rigidBody1.setRawTransform(obj1.transform);
//        rigidBody1.setMass(100);
//        Circle c1 = new Circle();
//        c1.setRadius(10.0f);
//        c1.setRigidbody(rigidBody1);
//        rigidBody1.setCollider(c1);
//
//        obj2 = new DebugObject("Circle-2", new Transform(new Vector2f(100, 800)), 0);
//        rigidBody2 = new Rigidbody2D();
//        rigidBody2.setRawTransform(obj2.transform);
//        rigidBody2.setMass(200);
//        Circle c2 = new Circle();
//        c2.setRadius(20.0f);
//        c2.setRigidbody(rigidBody2);
//        rigidBody2.setCollider(c2);
//
//        physics.addRigidBody(rigidBody1, true);
//        physics.addRigidBody(rigidBody2, false);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet(spritesPath, new Spritesheet(AssetPool.getTexture(spritesPath),
                        16, 16, 81, 0));
        AssetPool.addSpritesheet(marioSpritesPath, new Spritesheet(AssetPool.getTexture(marioSpritesPath),
                        16, 16, 26, 0));
        AssetPool.addSpritesheet(gizmosPath, new Spritesheet(AssetPool.getTexture(gizmosPath),
                        24, 48, 3, 0));

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

//        DebugDraw.addCircle(obj1.transform.position, 10.0f, Color.RED.toVector());
//        DebugDraw.addCircle(obj2.transform.position, 20.0f, Color.BLUE.toVector());
//        physics.update(dt);
    }

    @Override
    public void render() {
        // Render the scene
        this.renderer.render();
    }

    @Override
    public void imGui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorComponents.imGui();
        ImGui.end();

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
