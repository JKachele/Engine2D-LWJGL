/******************************************
 *Project-------Engine2D-LWJGL
 *File----------LevelEditorScene.java
 *Author--------Justin Kachele
 *Date----------9/25/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.scene;

import com.jkachele.game.components.*;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.Prefabs;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Constants;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class LevelEditorSceneInitializer extends SceneInitializer {
    private final String spritesPath = "assets/images/spritesheets/decorationsAndBlocks.png";
    private Spritesheet sprites;
    private final String marioSpritesPath = "assets/images/spritesheets/characters.png";
    private Spritesheet marioSprites;
    private final String gizmosPath = "assets/images/gizmos.png";
    private Spritesheet gizmos;
    private final String filePath = "assets/levels/levelEditor.txt";
    private GameObject levelEditorComponents;

    @Override
    public void init(Scene scene) {
        sprites = AssetPool.getSpritesheet(spritesPath);
        marioSprites = AssetPool.getSpritesheet(marioSpritesPath);
        gizmos = AssetPool.getSpritesheet(gizmosPath);


        levelEditorComponents = scene.createGameObject("Level Editor");
        levelEditorComponents.setTransient(true);
        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(scene.getCamera()));
        levelEditorComponents.addComponent(new GizmoSystem(gizmos));
        scene.addGameObject(levelEditorComponents);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet(spritesPath, new Spritesheet(AssetPool.getTexture(spritesPath),
                        16, 16, 81, 0));
        AssetPool.addSpritesheet(marioSpritesPath, new Spritesheet(AssetPool.getTexture(marioSpritesPath),
                        16, 16, 26, 0));
        AssetPool.addSpritesheet(gizmosPath, new Spritesheet(AssetPool.getTexture(gizmosPath),
                        24, 48, 3, 0));

        for (GameObject gameObject : scene.getGameObjects()) {
            if(gameObject.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
                if (sprite.getTexture() != null) {
                    sprite.setTexture((AssetPool.getTexture(sprite.getTexture().getFilepath())));
                }
            }
        }
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

    public String getFilePath() {
        return filePath;
    }
}
